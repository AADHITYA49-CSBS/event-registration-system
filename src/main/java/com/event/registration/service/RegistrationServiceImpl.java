package com.event.registration.service;

import com.event.registration.dto.RegisterOutcomeResponse;
import com.event.registration.dto.RegistrationState;
import com.event.registration.exception.EventClosedException;
import com.event.registration.exception.RegistrationConflictException;
import com.event.registration.exception.ResourceNotFoundException;
import com.event.registration.model.Event;
import com.event.registration.model.Registration;
import com.event.registration.model.Waitlist;
import com.event.registration.repository.EventRepository;
import com.event.registration.repository.RegistrationRepository;
import com.event.registration.repository.WaitlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final WaitlistRepository waitlistRepository;
    private final EventStatusService eventStatusService;

    public RegistrationServiceImpl(RegistrationRepository registrationRepository,
                                   EventRepository eventRepository,
                                   WaitlistRepository waitlistRepository,
                                   EventStatusService eventStatusService) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.waitlistRepository = waitlistRepository;
        this.eventStatusService = eventStatusService;
    }

    @Override
    @Transactional
    public RegisterOutcomeResponse register(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

        if (registrationRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new RegistrationConflictException("User is already registered for this event");
        }

        if (waitlistRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new RegistrationConflictException("User is already in waitlist for this event");
        }

        long registrationCount = registrationRepository.countByEventId(eventId);
        event = eventStatusService.syncStatus(event, registrationCount);

        if (event.getDate().isBefore(LocalDateTime.now())) {
            throw new EventClosedException("Event is closed because event date has passed");
        }

        if (registrationCount >= event.getCapacity()) {
            Waitlist waitlist = waitlistRepository.save(new Waitlist(event, userId, LocalDateTime.now()));

            RegisterOutcomeResponse response = new RegisterOutcomeResponse();
            response.setState(RegistrationState.WAITLISTED);
            response.setEventId(event.getId());
            response.setUserId(userId);
            response.setWaitlistId(waitlist.getId());
            response.setMessage("Event is full. User added to waitlist");
            return response;
        }

        Registration registration = registrationRepository.save(new Registration(event, userId));
        eventStatusService.syncStatus(event, registrationCount + 1);

        RegisterOutcomeResponse response = new RegisterOutcomeResponse();
        response.setState(RegistrationState.REGISTERED);
        response.setEventId(event.getId());
        response.setUserId(userId);
        response.setRegistrationId(registration.getId());
        response.setMessage("User registered successfully");
        return response;
    }

    @Override
    @Transactional
    public void cancelRegistration(Long registrationId) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found with id: " + registrationId));

        Event event = registration.getEvent();
        registrationRepository.delete(registration);

        waitlistRepository.findFirstByEventIdOrderByCreatedAtAsc(event.getId()).ifPresent(waitlistedUser -> {
            registrationRepository.save(new Registration(event, waitlistedUser.getUserId()));
            waitlistRepository.delete(waitlistedUser);
        });

        long registrationCount = registrationRepository.countByEventId(event.getId());
        eventStatusService.syncStatus(event, registrationCount);
    }
}
