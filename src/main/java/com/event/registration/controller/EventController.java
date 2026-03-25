package com.event.registration.controller;

import com.event.registration.dto.EventAvailabilityResponse;
import com.event.registration.dto.EventRequestDTO;
import com.event.registration.dto.EventResponseDTO;
import com.event.registration.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody EventRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents(@RequestParam(required = false) String filter) {
        return ResponseEntity.ok(eventService.getAllEvents(filter));
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<EventAvailabilityResponse> getEventAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getAvailability(id));
    }
}
