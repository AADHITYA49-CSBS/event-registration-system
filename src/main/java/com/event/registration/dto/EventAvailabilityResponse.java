package com.event.registration.dto;

public class EventAvailabilityResponse {

    private Long eventId;
    private String title;
    private int capacity;
    private long registrations;
    private long availableSlots;

    public EventAvailabilityResponse(Long eventId, String title, int capacity, long registrations) {
        this.eventId = eventId;
        this.title = title;
        this.capacity = capacity;
        this.registrations = registrations;
        this.availableSlots = capacity - registrations;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public long getRegistrations() {
        return registrations;
    }

    public void setRegistrations(long registrations) {
        this.registrations = registrations;
    }

    public long getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(long availableSlots) {
        this.availableSlots = availableSlots;
    }
}
