package com.event.registration.controller;

import com.event.registration.dto.LoginRequest;
import com.event.registration.dto.LoginResponse;
import com.event.registration.exception.InvalidCredentialsException;
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
        if (!isValidCredential(request.getUsername(), request.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String role = getRoleForUser(request.getUsername());
        String token = tokenProvider.generateToken(request.getUsername(), role);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    private String getRoleForUser(String username) {
        if ("root".equals(username)) {
            return "ROLE_ADMIN";
        }
        return "ROLE_USER";
    }

    private boolean isValidCredential(String username, String password) {
        return ("root".equals(username) && "Djaadhi_09".equals(password))
                || ("user".equals(username) && "1234".equals(password))
                || ("Kabilan".equals(username) && "1234".equals(password));
    }
}
