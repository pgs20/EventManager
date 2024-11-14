package dev.petrov.converter;

import dev.petrov.dto.event.Event;
import dev.petrov.dto.event.EventCreateRequestDto;
import dev.petrov.dto.event.EventDto;
import dev.petrov.dto.event.EventStatus;
import dev.petrov.entity.EventEntity;
import org.springframework.stereotype.Component;

@Component
public class ConverterEvent {

    public Event toDomain(EventCreateRequestDto eventCreateRequestDto) {
        return new Event(
                eventCreateRequestDto.getName(),
                eventCreateRequestDto.getMaxPlaces(),
                eventCreateRequestDto.getDate(),
                eventCreateRequestDto.getCost(),
                eventCreateRequestDto.getDuration(),
                eventCreateRequestDto.getLocationId()
        );
    }

    public EventEntity toEntity(Event event) {
        return new EventEntity(
                event.getName(),
                event.getOwnerId(),
                event.getMaxPlaces(),
                event.getDate(),
                event.getCost(),
                event.getDuration(),
                event.getLocationId(),
                event.getStatus().name()
        );
    }

    public EventDto toDto(Event event) {
        return new EventDto(
                event.getId(),
                event.getName(),
                event.getOwnerId(),
                event.getMaxPlaces(),
                event.getDate(),
                event.getCost(),
                event.getDuration(),
                event.getLocationId(),
                event.getStatus()
        );
    }

    public Event toDomain(EventEntity eventEntity) {
        return new Event(
                eventEntity.getId(),
                eventEntity.getName(),
                eventEntity.getOwnerId(),
                eventEntity.getMaxPlaces(),
                eventEntity.getDate(),
                eventEntity.getCost(),
                eventEntity.getDuration(),
                eventEntity.getLocationId(),
                EventStatus.valueOf(eventEntity.getStatus())
        );
    }
}
