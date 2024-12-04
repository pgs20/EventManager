package dev.petrov.kafka;

import java.util.List;

public record EventKafkaNotification(
        Integer eventId,
        Integer userIdChangesEvent,
        String ownerId,
        List<Integer> subscribersEvent,
        FieldChange<String> name,
        FieldChange<Integer> maxPlaces,
        FieldChange<String> date,
        FieldChange<Integer> cost,
        FieldChange<Integer> duration,
        FieldChange<Long> locationId
) {
}
