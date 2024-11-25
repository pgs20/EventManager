package dev.petrov.converter;

import dev.petrov.dto.event.Event;
import dev.petrov.entity.EventEntity;

public class UpdateEventMapper {
    public static void updateEventFromDto(EventEntity event, Event updateEvent) {
        if (updateEvent.getName() != null) {
            event.setName(updateEvent.getName());
        }
        if (updateEvent.getMaxPlaces() != null) {
            event.setMaxPlaces(updateEvent.getMaxPlaces());
        }
        if (updateEvent.getDate() != null) {
            event.setDate(updateEvent.getDate());
        }
        if (updateEvent.getCost() != null) {
            event.setCost(updateEvent.getCost());
        }
        if (updateEvent.getDuration() != null) {
            event.setDuration(updateEvent.getDuration());
        }
        if (updateEvent.getLocationId() != null) {
            event.setLocationId(updateEvent.getLocationId());
        }
    }
}
