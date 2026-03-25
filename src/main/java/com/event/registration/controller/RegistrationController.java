package com.event.registration.controller;

import com.event.registration.dto.RegisterRequest;
import com.event.registration.dto.RegistrationResponse;
import com.event.registration.model.Registration;
import com.event.registration.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegisterRequest request) {
        Registration registration = registrationService.register(request.getEventId(), request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(RegistrationResponse.from(registration));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelRegistration(@PathVariable Long id) {
        registrationService.cancelRegistration(id);
        return ResponseEntity.noContent().build();
    }
}
