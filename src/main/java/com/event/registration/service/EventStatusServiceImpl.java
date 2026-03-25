package com.event.registration.service;

import com.event.registration.model.Event;
import com.event.registration.model.EventStatus;
import com.event.registration.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EventStatusServiceImpl implements EventStatusService {

    private final EventRepository eventRepository;

    public EventStatusServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public Event syncStatus(Event event, long registrationCount) {
        EventStatus computedStatus = isClosed(event, registrationCount) ? EventStatus.CLOSED : EventStatus.OPEN;
        if (event.getStatus() != computedStatus) {
            event.setStatus(computedStatus);
            return eventRepository.save(event);
        }
        return event;
    }

    private boolean isClosed(Event event, long registrationCount) {
        return event.getDate().isBefore(LocalDateTime.now()) || registrationCount >= event.getCapacity();
    }
}
