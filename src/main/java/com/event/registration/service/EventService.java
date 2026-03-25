package com.event.registration.service;

import com.event.registration.dto.EventAvailabilityResponse;
import com.event.registration.dto.EventRequestDTO;
import com.event.registration.dto.EventResponseDTO;

import java.util.List;

public interface EventService {

    EventResponseDTO createEvent(EventRequestDTO eventRequestDTO);

    EventResponseDTO getEventById(Long id);

    List<EventResponseDTO> getAllEvents(String filter);

    EventAvailabilityResponse getAvailability(Long eventId);
}
