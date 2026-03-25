package com.event.registration.service;

import com.event.registration.dto.EventAvailabilityResponse;
import com.event.registration.exception.ResourceNotFoundException;
import com.event.registration.model.Event;
import com.event.registration.repository.EventRepository;
import com.event.registration.repository.RegistrationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public EventServiceImpl(EventRepository eventRepository, RegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    @Override
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> getUpcomingEvents() {
        return eventRepository.findByDateAfterOrderByDateAsc(LocalDateTime.now());
    }

    @Override
    public List<Event> getPastEvents() {
        return eventRepository.findByDateBeforeOrderByDateDesc(LocalDateTime.now());
    }

    @Override
    public EventAvailabilityResponse getEventAvailability(Long eventId) {
        Event event = getEventById(eventId);
        long registrationCount = registrationRepository.countByEventId(eventId);
        return new EventAvailabilityResponse(event.getId(), event.getTitle(), event.getCapacity(), registrationCount);
    }
}
