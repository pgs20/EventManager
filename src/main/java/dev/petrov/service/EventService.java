package dev.petrov.service;

import dev.petrov.converter.ConverterEvent;
import dev.petrov.dto.event.Event;
import dev.petrov.dto.event.EventStatus;
import dev.petrov.dto.locationDto.Location;
import dev.petrov.dto.usersDto.User;
import dev.petrov.entity.EventEntity;
import dev.petrov.entity.LocationEntity;
import dev.petrov.repository.EventRepository;
import dev.petrov.repository.LocationRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
}
