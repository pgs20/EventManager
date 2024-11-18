package dev.petrov.service;

import dev.petrov.converter.ConverterEvent;
import dev.petrov.dto.MessageResponse;
import dev.petrov.dto.event.Event;
import dev.petrov.dto.event.EventStatus;
import dev.petrov.dto.locationDto.Location;
import dev.petrov.dto.usersDto.User;
import dev.petrov.dto.usersDto.UserRole;
import dev.petrov.entity.EventEntity;
import dev.petrov.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ConverterEvent converterEvent;
    private final LocationService locationService;

    public EventService(EventRepository eventRepository, ConverterEvent converterEvent, LocationService locationService) {
        this.eventRepository = eventRepository;
        this.converterEvent = converterEvent;
        this.locationService = locationService;
    }

    @Transactional
    public Event createEvent(Event event) {
        Location location = locationService.findLocationById(event.getLocationId());

        if (location.capacity() < event.getMaxPlaces()) {
            throw new IllegalArgumentException("Недостаточно мест для всех участников");
        }

        event.setStatus(EventStatus.WAIT_START);
        event.setOwnerId(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId().toString());
        event.setOccupiedPlaces(Integer.valueOf(0));

        EventEntity eventEntity = eventRepository.save(converterEvent.toEntity(event));

        return converterEvent.toDomain(eventEntity);
    }

    @Transactional
    public MessageResponse deleteEventById(Integer eventId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        EventEntity event = validateBeforeAction(eventId, user);

        event.setStatus(EventStatus.CANCELLED.name());
        eventRepository.save(event);

        return new MessageResponse("Мероприятие успешно удалено");
    }

    public Event getEventById(Integer eventId) {
        return converterEvent.toDomain(
                Optional.of(eventRepository.getById(eventId))
                        .orElseThrow(() -> new EntityNotFoundException("Мероприятие с id = " + eventId + " не найдено"))
        );
    }

    @Transactional
    public Event updateEventById(Integer eventId, Event updateEvent) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        EventEntity event = validateBeforeAction(eventId, user);

        if (
                event.getName().equals(updateEvent.getName()) ||
                event.getMaxPlaces().equals(updateEvent.getMaxPlaces()) ||
                event.getDate().equals(updateEvent.getDate()) ||
                event.getCost().equals(updateEvent.getCost()) ||
                event.getDuration().equals(updateEvent.getDuration()) ||
                event.getLocationId().equals(updateEvent.getLocationId())
        ) {
            throw new IllegalArgumentException("Ошибка: необходимо ввести новые данные для обновления");
        }

        event.setName(updateEvent.getName());
        event.setMaxPlaces(updateEvent.getMaxPlaces());
        event.setDate(updateEvent.getDate());
        event.setCost(updateEvent.getCost());
        event.setDuration(updateEvent.getDuration());
        event.setLocationId(updateEvent.getLocationId());

        return converterEvent.toDomain(eventRepository.save(event));
    }

    private EventEntity validateBeforeAction(Integer eventId, User user) {
        EventEntity event = Optional.of(eventRepository.getById(eventId))
                .orElseThrow(() -> new EntityNotFoundException("Мероприятия с id=" + eventId + " не существует"));

        if (!(user.getRole() == UserRole.ADMIN || user.getId().toString().equals(event.getOwnerId()))) {
            throw new IllegalArgumentException("Ошибка: доступ имеет только ADMIN либо создатель мероприятия");
        }

        if (event.getStatus().equals(EventStatus.STARTED)) {
            throw new IllegalArgumentException("Ошибка: мероприятие уже началось");
        }

        return event;
    }
}