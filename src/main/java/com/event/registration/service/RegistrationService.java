package com.event.registration.service;

import com.event.registration.model.Registration;

public interface RegistrationService {

    Registration register(Long eventId, Long userId);

    void cancelRegistration(Long registrationId);
}
