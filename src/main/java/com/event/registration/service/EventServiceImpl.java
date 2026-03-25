package com.event.registration.service;

import com.event.registration.dto.EventAvailabilityResponse;
import com.event.registration.dto.EventRequestDTO;
import com.event.registration.dto.EventResponseDTO;
import com.event.registration.exception.EventNotFoundException;
import com.event.registration.model.Event;
import com.event.registration.model.EventStatus;
import com.event.registration.repository.EventRepository;
import com.event.registration.repository.RegistrationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final EventStatusService eventStatusService;

    public EventServiceImpl(EventRepository eventRepository,
                            RegistrationRepository registrationRepository,
                            EventStatusService eventStatusService) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.eventStatusService = eventStatusService;
    }

    @Override
    @Transactional
    public EventResponseDTO createEvent(EventRequestDTO eventRequestDTO) {
        Event event = new Event(eventRequestDTO.getTitle(), eventRequestDTO.getDate(), eventRequestDTO.getCapacity());
        event.setStatus(event.getDate().isBefore(LocalDateTime.now()) ? EventStatus.CLOSED : EventStatus.OPEN);
        return toDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventResponseDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + id));
        long registrationCount = registrationRepository.countByEventId(id);
        return toDto(eventStatusService.syncStatus(event, registrationCount));
    }

    @Override
    @Transactional
    public List<EventResponseDTO> getAllEvents(String filter) {
        List<Event> events;
        if ("upcoming".equalsIgnoreCase(filter)) {
            events = eventRepository.findByDateAfterOrderByDateAsc(LocalDateTime.now());
        } else if ("past".equalsIgnoreCase(filter)) {
            events = eventRepository.findByDateBeforeOrderByDateDesc(LocalDateTime.now());
        } else {
            events = eventRepository.findAll();
        }

        for (Event event : events) {
            long registrationCount = registrationRepository.countByEventId(event.getId());
            eventStatusService.syncStatus(event, registrationCount);
        }
        return events.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventAvailabilityResponse getAvailability(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));
        long currentCount = registrationRepository.countByEventId(eventId);
        event = eventStatusService.syncStatus(event, currentCount);
        long registrationCount = registrationRepository.countByEventId(eventId);
        return new EventAvailabilityResponse(event.getId(), event.getTitle(), event.getCapacity(), registrationCount);
    }

    private EventResponseDTO toDto(Event event) {
        EventResponseDTO responseDTO = new EventResponseDTO();
        responseDTO.setId(event.getId());
        responseDTO.setTitle(event.getTitle());
        responseDTO.setDate(event.getDate());
        responseDTO.setCapacity(event.getCapacity());
        responseDTO.setStatus(event.getStatus());
        return responseDTO;
    }
}
