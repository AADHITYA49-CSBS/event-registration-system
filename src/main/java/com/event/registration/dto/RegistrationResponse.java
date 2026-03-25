package com.event.registration.dto;

import com.event.registration.model.Registration;

public class RegistrationResponse {

    private Long id;
    private Long eventId;
    private Long userId;

    public static RegistrationResponse from(Registration registration) {
        RegistrationResponse response = new RegistrationResponse();
        response.setId(registration.getId());
        response.setEventId(registration.getEvent().getId());
        response.setUserId(registration.getUserId());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
