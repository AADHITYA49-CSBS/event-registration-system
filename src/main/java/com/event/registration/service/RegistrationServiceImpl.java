package com.event.registration.service;

import com.event.registration.exception.RegistrationConflictException;
import com.event.registration.exception.ResourceNotFoundException;
import com.event.registration.model.Event;
import com.event.registration.model.Registration;
import com.event.registration.repository.EventRepository;
import com.event.registration.repository.RegistrationRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;

    public RegistrationServiceImpl(RegistrationRepository registrationRepository, EventRepository eventRepository) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Registration register(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

        if (registrationRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new RegistrationConflictException("User is already registered for this event");
        }

        long registrationCount = registrationRepository.countByEventId(eventId);
        if (registrationCount >= event.getCapacity()) {
            throw new RegistrationConflictException("Event is full. Registration capacity reached");
        }

        Registration registration = new Registration(event, userId);
        return registrationRepository.save(registration);
    }

    @Override
    public void cancelRegistration(Long registrationId) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found with id: " + registrationId));
        registrationRepository.delete(registration);
    }
}
