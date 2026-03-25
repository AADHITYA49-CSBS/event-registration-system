package com.event.registration.dto;

import java.time.Instant;

public class ApiErrorResponse {

    private final String message;
    private final int status;
    private final Instant timestamp;

    public ApiErrorResponse(String message, int status, Instant timestamp) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}

