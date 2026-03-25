package com.event.registration.service;

import com.event.registration.dto.RegistrationRequestDTO;
import com.event.registration.dto.RegistrationResponseDTO;

public interface RegistrationService {

    RegistrationResponseDTO register(RegistrationRequestDTO request);

    void cancelRegistration(Long registrationId);
}
