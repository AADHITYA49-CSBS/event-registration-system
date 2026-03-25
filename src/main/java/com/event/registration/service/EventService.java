package com.event.registration.service;

import com.event.registration.model.Event;

public interface EventService {

    Event createEvent(Event event);

    Event getEventById(Long id);
}
