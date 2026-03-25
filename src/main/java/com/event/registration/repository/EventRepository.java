package com.event.registration.repository;

import com.event.registration.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByDateAfterOrderByDateAsc(LocalDateTime date);

    List<Event> findByDateBeforeOrderByDateDesc(LocalDateTime date);
}
