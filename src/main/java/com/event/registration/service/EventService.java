package com.event.registration.service;

import com.event.registration.dto.EventAvailabilityResponse;
import com.event.registration.model.Event;

import java.util.List;

public interface EventService {

    Event createEvent(Event event);

    Event getEventById(Long id);

    List<Event> getAllEvents();

    List<Event> getUpcomingEvents();

    List<Event> getPastEvents();

    EventAvailabilityResponse getEventAvailability(Long eventId);
}
