package com.event.registration.service;

import com.event.registration.dto.RegisterOutcomeResponse;

public interface RegistrationService {

    RegisterOutcomeResponse register(Long eventId, Long userId);

    void cancelRegistration(Long registrationId);
}
