package dev.petrov.service;

import dev.petrov.converter.ConverterEvent;
import dev.petrov.dto.MessageResponse;
import dev.petrov.dto.event.Event;
import dev.petrov.dto.event.EventStatus;
import dev.petrov.dto.event.request.EventSearchRequestDto;
import dev.petrov.dto.locationDto.Location;
import dev.petrov.dto.usersDto.User;
import dev.petrov.dto.usersDto.UserRole;
import dev.petrov.entity.EventEntity;
import dev.petrov.entity.RegistrationEntity;
import dev.petrov.entity.UserEntity;
import dev.petrov.repository.EventRepository;
import dev.petrov.repository.RegistrationRepository;
import dev.petrov.repository.UserRepository;
import dev.petrov.specification.EventSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final ConverterEvent converterEvent;
    private final LocationService locationService;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository,
                        RegistrationRepository registrationRepository,
                        ConverterEvent converterEvent,
                        LocationService locationService,
                        UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.converterEvent = converterEvent;
        this.locationService = locationService;
        this.userRepository = userRepository;
    }

    @Transactional
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

    public List<Event> searchEventByFilter(EventSearchRequestDto searchRequestDto) {
        return Optional.of(eventRepository.findAll(
                        EventSpecification.filterByDto(searchRequestDto)
                )).orElseThrow(() -> new EntityNotFoundException("Мероприятий по заданному фильтру не найдено"))
                .stream()
                .map(converterEvent::toDomain)
                .collect(Collectors.toList());
    }

    public List<Event> searchEventByOwnerId() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return Optional.of(eventRepository.findAll(EventSpecification.filterByOwnerId(user.getId().toString())))
                .filter(eventEntities -> !eventEntities.isEmpty())
                .map(eventEntities -> eventEntities.stream()
                        .map(converterEvent::toDomain)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new EntityNotFoundException("Данный пользователь мероприятий не создавал"));
    }

    @Transactional
    public MessageResponse registerUserForEvent(Integer eventId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        UserEntity userEntity = userRepository.getById(user.getId());
        EventEntity event = Optional.of(eventRepository.getById(eventId))
                .orElseThrow(() -> new EntityNotFoundException("Мероприятия с id=" + eventId + " не существует"));

        if (event.getStatus().equals(EventStatus.CANCELLED.name()) || event.getStatus().equals(EventStatus.FINISHED.name())) {
            throw new IllegalArgumentException("Ошибка: мероприятие отменено или закончилось");
        }

        Optional<RegistrationEntity> registration = registrationRepository.findByEventAndUser(event, userEntity);
        if (registration.isPresent()) {
            throw new IllegalArgumentException("Ошибка: вы уже зарегистрированы на это мероприятие");
        }

        if (event.getOccupiedPlaces() >= event.getMaxPlaces()) {
            throw new IllegalArgumentException("Ошибка: нет свободных мест на мероприятие");
        }

        event.setOccupiedPlaces(event.getOccupiedPlaces() + 1);
        registrationRepository.save(new RegistrationEntity(event, userEntity));
        eventRepository.save(event);

        return new MessageResponse("Успешная регистрация на мероприятие");
    }

    @Transactional
    public MessageResponse cancelRegistration(Integer eventId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        UserEntity userEntity = userRepository.getById(user.getId());
        EventEntity event = Optional.of(eventRepository.getById(eventId))
                .orElseThrow(() -> new EntityNotFoundException("Мероприятия с id=" + eventId + " не существует"));

        if (event.getStatus().equals(EventStatus.STARTED.name()) || event.getStatus().equals(EventStatus.FINISHED.name())) {
            throw new IllegalArgumentException("Ошибка: мероприятие уже идет или закончилось");
        }

        RegistrationEntity registration = registrationRepository.findByEventAndUser(event, userEntity)
                .orElseThrow(() -> new IllegalArgumentException("Ошибка: вы уже отменили регистрацию на это мероприятие"));

        event.setOccupiedPlaces(event.getOccupiedPlaces() - 1);
        registrationRepository.delete(registration);

        return new MessageResponse("Успешная отмена регистрации на мероприятие");
    }

    public List<Event> getEventsUserIsRegistered() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return Optional.of(registrationRepository.findEventsByUserId(user.getId()))
                .filter(eventEntities -> !eventEntities.isEmpty())
                .map(eventEntities -> eventEntities.stream()
                        .map(converterEvent::toDomain)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не зарегистрирован на мероприятия"));
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