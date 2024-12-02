package dev.petrov.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventSender {

    private final KafkaTemplate<Integer, EventKafkaNotification> kafkaTemplate;

    public EventSender(KafkaTemplate<Integer, EventKafkaNotification> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(EventKafkaNotification eventKafkaNotification) {
        kafkaTemplate.send(
                "event-topic",
                eventKafkaNotification.eventId(),
                eventKafkaNotification
        );
    }
}
