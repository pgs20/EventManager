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

import java.util.Optional;

@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final ConverterEvent converterEvent;
    private final LocationService locationService;

    public EventService(EventRepository eventRepository, ConverterEvent converterEvent, LocationService locationService) {
        this.eventRepository = eventRepository;
        this.converterEvent = converterEvent;
        this.locationService = locationService;
    }

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

    public MessageResponse deleteEventById(Integer eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new IllegalArgumentException("Мероприятия с id=" + eventId + " не существует");
        }

        EventEntity event = eventRepository.getById(eventId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(" user id = {}, event owner id = {}", user.getId(), event.getOwnerId());
        if (!(user.getRole() == UserRole.ADMIN || user.getId().toString().equals(event.getOwnerId()))) {
            throw new IllegalArgumentException("Мероприятия с id=" + eventId + " может удалить только ADMIN либо создатель мероприятия");
        }

        if (event.getStatus().equals(EventStatus.STARTED)) {
            throw new IllegalArgumentException("Мероприятие нельзя удалить, оно уже началось");
        }

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
}
