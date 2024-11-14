package dev.petrov.service;

import dev.petrov.converter.ConverterEvent;
import dev.petrov.dto.event.Event;
import dev.petrov.dto.event.EventDto;
import dev.petrov.dto.event.EventStatus;
import dev.petrov.dto.usersDto.User;
import dev.petrov.entity.EventEntity;
import dev.petrov.repository.EventRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ConverterEvent converterEvent;

    public EventService(EventRepository eventRepository, ConverterEvent converterEvent) {
        this.eventRepository = eventRepository;
        this.converterEvent = converterEvent;
    }

    public Event createEvent(Event event) {
        event.setStatus(EventStatus.WAIT_START);
        event.setOwnerId(((User) SecurityContextHolder.getContext().getAuthentication().getCredentials()).getId().toString());
        event.setOccupiedPlaces(0);

        EventEntity eventEntity = eventRepository.save(converterEvent.toEntity(event));

        return converterEvent.toDomain(eventEntity);
    }
}
