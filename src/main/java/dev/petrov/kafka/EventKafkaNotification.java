package dev.petrov.kafka;

import java.util.List;

public record EventKafkaNotification(
        Integer eventId,
        Integer userIdChangesEvent,
        String ownerId,
        List<Integer> subscribersEvent
) {
}
