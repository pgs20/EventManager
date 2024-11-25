package dev.petrov.controller;

import dev.petrov.converter.ConverterEvent;
import dev.petrov.dto.event.EventDto;
import dev.petrov.service.EventService;
import dev.petrov.service.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final ConverterEvent converterEvent;

    public RegistrationController(RegistrationService registrationService, ConverterEvent converterEvent) {
        this.registrationService = registrationService;
        this.converterEvent = converterEvent;
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<Void> registerForEvent(@PathVariable Integer eventId) {
        registrationService.registerUserForEvent(eventId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cancel/{eventId}")
    public ResponseEntity<Void> cancelRegistrationForEvent(@PathVariable Integer eventId) {
        registrationService.cancelRegistration(eventId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getEventUserIsReg() {
        return ResponseEntity.ok().body(
                registrationService.getEventsUserIsRegistered()
                        .stream()
                        .map(converterEvent::toDto)
                        .collect(Collectors.toList()));
    }
}
