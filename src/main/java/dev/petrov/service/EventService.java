package dev.petrov.service;

import dev.petrov.converter.ConverterEvent;
import dev.petrov.converter.ConverterUser;
import dev.petrov.converter.UpdateEventMapper;
import dev.petrov.dto.event.Event;
import dev.petrov.dto.event.EventStatus;
import dev.petrov.dto.event.request.EventSearchRequestDto;
import dev.petrov.dto.locationDto.Location;
import dev.petrov.dto.usersDto.User;
import dev.petrov.dto.usersDto.UserRole;
import dev.petrov.entity.EventEntity;
import dev.petrov.kafka.EventKafkaNotification;
import dev.petrov.kafka.EventSender;
import dev.petrov.kafka.FieldChange;
import dev.petrov.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final Logger log = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final ConverterEvent converterEvent;
    private final ConverterUser converterUser;
    private final LocationService locationService;
    private final EventSender eventSender;


    public EventService(EventRepository eventRepository,
                        ConverterEvent converterEvent,
                        ConverterUser converterUser, LocationService locationService,
                        EventSender eventSender) {
        this.eventRepository = eventRepository;
        this.converterEvent = converterEvent;
        this.converterUser = converterUser;
        this.locationService = locationService;
        this.eventSender = eventSender;
    }

    public Event createEvent(Event event) {
        Location location = locationService.findLocationById(event.getLocationId());

        if (location.capacity() < event.getMaxPlaces()) {
            throw new IllegalArgumentException("Недостаточно мест для всех участников");
        }

        event.setStatus(EventStatus.WAIT_START);
        event.setOwnerId(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId().toString());
        event.setOccupiedPlaces(0);

        EventEntity eventEntity = eventRepository.save(converterEvent.toEntity(event));

        return converterEvent.toDomain(eventEntity);
    }

    public void deleteEventById(Integer eventId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        EventEntity event = validateBeforeAction(eventId, user);

        event.setStatus(EventStatus.CANCELLED.name());
        eventRepository.save(event);
    }

    public Event getEventById(Integer eventId) {
        return converterEvent.toDomain(eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Мероприятие с id = " + eventId + " не найдено"))
        );
    }

    public Event updateEventById(Integer eventId, Event updateEvent) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        EventEntity event = validateBeforeAction(eventId, user);

        if (updateEvent.getMaxPlaces() < event.getMaxPlaces()) {
            throw new IllegalArgumentException("Ошибка: максимальное кол-во мест должно быть больше предыдущего значения");
        }

        eventSender.sendEvent(
                new EventKafkaNotification(
                        event.getId(),
                        user.getId(),
                        event.getOwnerId(),
                        event.getRegistrationEntities()
                                .stream()
                                .map(registrationEntity -> registrationEntity.getUser().getId())
                                .collect(Collectors.toList()),
                        new FieldChange<String>(event.getName(), updateEvent.getName()),
                        new FieldChange<Integer>(event.getMaxPlaces(), updateEvent.getMaxPlaces()),
                        new FieldChange<String>(event.getDate(), updateEvent.getDate()),
                        new FieldChange<Integer>(event.getCost(), updateEvent.getCost()),
                        new FieldChange<Integer>(event.getDuration(), updateEvent.getDuration()),
                        new FieldChange<Long>(event.getLocationId(), updateEvent.getLocationId())

                ));

        UpdateEventMapper.updateEventFromDto(event, updateEvent);

        return converterEvent.toDomain(eventRepository.save(event));
    }

    public List<Event> searchEventByFilter(EventSearchRequestDto searchRequestDto) {
        return eventRepository.findByFilters(
                        searchRequestDto.getName(),
                        searchRequestDto.getPlacesMin(),
                        searchRequestDto.getPlacesMax(),
                        searchRequestDto.getDateStartAfter(),
                        searchRequestDto.getDateStartBefore(),
                        searchRequestDto.getCostMin(),
                        searchRequestDto.getCostMax(),
                        searchRequestDto.getDurationMin(), searchRequestDto.getDurationMax(),
                        searchRequestDto.getLocationId(),
                        searchRequestDto.getEventStatus().name()
                )
                .filter(eventEntities -> !eventEntities.isEmpty()).map(eventEntities -> eventEntities.stream()
                        .map(converterEvent::toDomain)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new EntityNotFoundException("Мероприятий по заданному фильтру не найдено"));
    }

    public List<Event> searchEventByOwnerId() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return eventRepository.findByOwnerId(user.getId().toString())
                .filter(eventEntities -> !eventEntities.isEmpty())
                .map(eventEntities -> eventEntities.stream()
                        .map(converterEvent::toDomain)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new EntityNotFoundException("Данный пользователь мероприятий не создавал"));
    }

    private EventEntity validateBeforeAction(Integer eventId, User user) {
        EventEntity event = Optional.of(eventRepository.getById(eventId))
                .orElseThrow(() -> new EntityNotFoundException("Мероприятия с id=" + eventId + " не существует"));

        if (!(user.getRole() == UserRole.ADMIN || user.getId().toString().equals(event.getOwnerId()))) {
            throw new IllegalArgumentException("Ошибка: доступ имеет только ADMIN либо создатель мероприятия");
        }

        if (event.getStatus().equals(EventStatus.STARTED.name())) {
            throw new IllegalArgumentException("Ошибка: мероприятие уже началось");
        }

        return event;
    }
}