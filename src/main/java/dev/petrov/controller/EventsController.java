package dev.petrov.controller;

import dev.petrov.converter.ConverterEvent;
import dev.petrov.dto.MessageResponse;
import dev.petrov.dto.event.request.EventCreateRequestDto;
import dev.petrov.dto.event.EventDto;
import dev.petrov.dto.event.request.EventSearchRequestDto;
import dev.petrov.dto.event.request.EventUpdateRequestDto;
import dev.petrov.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @PutMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEventById(@PathVariable Integer eventId, @Valid @RequestBody EventUpdateRequestDto updateEvent) {
        return ResponseEntity.ok().body(
                converterEvent.toDto(
                        eventService.updateEventById(eventId, converterEvent.toDomain(updateEvent))
                )
        );
    }

    @PostMapping("/search")
    public ResponseEntity<List<EventDto>> searchEventByFilter(@RequestBody EventSearchRequestDto eventSearchDto) {
        return ResponseEntity.ok().body(
                eventService.searchEventByFilter(eventSearchDto)
                        .stream()
                        .map(converterEvent::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> searchEventByOwnerId() {
        return ResponseEntity.ok().body(
                eventService.searchEventByOwnerId()
                        .stream()
                        .map(converterEvent::toDto)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/registrations/{eventId}")
    public ResponseEntity<MessageResponse> registerForEvent(@PathVariable Integer eventId) {
        return ResponseEntity.ok()
                .body(eventService.registerUserForEvent(eventId));
    }
}