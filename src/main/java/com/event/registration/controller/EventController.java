package com.event.registration.controller;

import com.event.registration.dto.CreateEventRequest;
import com.event.registration.dto.EventAvailabilityResponse;
import com.event.registration.dto.EventResponse;
import com.event.registration.model.Event;
import com.event.registration.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventRequest request) {
        Event event = new Event(request.getTitle(), request.getDate(), request.getCapacity());
        Event createdEvent = eventService.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(EventResponse.from(createdEvent));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        return ResponseEntity.ok(EventResponse.from(event));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents(
            @RequestParam(required = false) String filter) {
        List<Event> events;
        if ("upcoming".equalsIgnoreCase(filter)) {
            events = eventService.getUpcomingEvents();
        } else if ("past".equalsIgnoreCase(filter)) {
            events = eventService.getPastEvents();
        } else {
            events = eventService.getAllEvents();
        }
        List<EventResponse> responses = events.stream()
                .map(EventResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<EventAvailabilityResponse> getEventAvailability(@PathVariable Long id) {
        EventAvailabilityResponse availability = eventService.getEventAvailability(id);
        return ResponseEntity.ok(availability);
    }
}
