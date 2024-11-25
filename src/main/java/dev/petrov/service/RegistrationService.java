package dev.petrov.service;

import dev.petrov.converter.ConverterEvent;
import dev.petrov.dto.event.Event;
import dev.petrov.dto.event.EventStatus;
import dev.petrov.dto.usersDto.User;
import dev.petrov.entity.EventEntity;
import dev.petrov.entity.RegistrationEntity;
import dev.petrov.entity.UserEntity;
import dev.petrov.repository.EventRepository;
import dev.petrov.repository.RegistrationRepository;
import dev.petrov.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final ConverterEvent converterEvent;

    public RegistrationService(UserRepository userRepository,
                               EventRepository eventRepository,
                               RegistrationRepository registrationRepository,
                               ConverterEvent converterEvent
    ) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.converterEvent = converterEvent;
    }

    @Transactional
    public void registerUserForEvent(Integer eventId) {
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
    }

    @Transactional
    public void cancelRegistration(Integer eventId) {
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
        eventRepository.save(event);
        registrationRepository.delete(registration);
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
}
