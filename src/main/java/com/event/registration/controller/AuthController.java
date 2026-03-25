package com.event.registration.controller;

import com.event.registration.dto.LoginRequest;
import com.event.registration.dto.LoginResponse;
import com.event.registration.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenProvider tokenProvider;

    public AuthController(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = tokenProvider.generateToken(request.getUsername());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
