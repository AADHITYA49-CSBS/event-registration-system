package com.event.registration.service;

import com.event.registration.model.Event;

public interface EventStatusService {

    Event syncStatus(Event event, long registrationCount);
}
