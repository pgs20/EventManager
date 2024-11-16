package dev.petrov.controller;

import dev.petrov.converter.ConverterEvent;
import dev.petrov.dto.MessageResponse;
import dev.petrov.dto.event.EventCreateRequestDto;
import dev.petrov.dto.event.EventDto;
import dev.petrov.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class EventsController {

    private final EventService eventService;
    private final ConverterEvent converterEvent;

    public EventsController(EventService eventService, ConverterEvent converterEvent) {
        this.eventService = eventService;
        this.converterEvent = converterEvent;
    }

    @PostMapping
    public ResponseEntity<EventDto> createEvent(@Valid @RequestBody EventCreateRequestDto createEventDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        converterEvent.toDto(
                                eventService.createEvent(
                                        converterEvent.toDomain(createEventDto)
                                )
                        )
                );
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<MessageResponse> deleteEventById(@PathVariable Integer eventId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventService.deleteEventById(eventId));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Integer eventId) {
        return ResponseEntity.ok().body(
                converterEvent.toDto(
                        eventService.getEventById(eventId)
                )
        );
    }
}
