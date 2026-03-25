package com.event.registration.dto;

import com.event.registration.model.Event;

import java.time.LocalDateTime;

public class EventResponse {

    private Long id;
    private String title;
    private LocalDateTime date;
    private int capacity;

    public static EventResponse from(Event event) {
        EventResponse response = new EventResponse();
        response.setId(event.getId());
        response.setTitle(event.getTitle());
        response.setDate(event.getDate());
        response.setCapacity(event.getCapacity());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
