package com.event.registration.service;

import com.event.registration.dto.EventAvailabilityResponse;
import com.event.registration.exception.ResourceNotFoundException;
import com.event.registration.model.Event;
import com.event.registration.model.EventStatus;
import com.event.registration.repository.EventRepository;
import com.event.registration.repository.RegistrationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final EventStatusService eventStatusService;

    public EventServiceImpl(EventRepository eventRepository,
                            RegistrationRepository registrationRepository,
                            EventStatusService eventStatusService) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.eventStatusService = eventStatusService;
    }

    @Override
    public Event createEvent(Event event) {
        event.setStatus(event.getDate().isBefore(LocalDateTime.now()) ? EventStatus.CLOSED : EventStatus.OPEN);
        return eventRepository.save(event);
    }

    @Override
    public Event getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        long registrationCount = registrationRepository.countByEventId(id);
        return eventStatusService.syncStatus(event, registrationCount);
    }

    @Override
    public List<Event> getAllEvents(String filter) {
        List<Event> events;
        if ("upcoming".equalsIgnoreCase(filter)) {
            events = eventRepository.findByDateAfterOrderByDateAsc(LocalDateTime.now());
        } else if ("past".equalsIgnoreCase(filter)) {
            events = eventRepository.findByDateBeforeOrderByDateDesc(LocalDateTime.now());
        } else {
            events = eventRepository.findAll();
        }

        for (Event event : events) {
            long registrationCount = registrationRepository.countByEventId(event.getId());
            eventStatusService.syncStatus(event, registrationCount);
        }
        return events;
    }

    @Override
    public EventAvailabilityResponse getAvailability(Long eventId) {
        Event event = getEventById(eventId);
        long registrationCount = registrationRepository.countByEventId(eventId);
        return new EventAvailabilityResponse(event.getId(), event.getTitle(), event.getCapacity(), registrationCount);
    }
}
