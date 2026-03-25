package com.event.registration.repository;

import com.event.registration.model.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {

    boolean existsByEventIdAndUserId(Long eventId, Long userId);

    Optional<Waitlist> findFirstByEventIdOrderByCreatedAtAsc(Long eventId);
}
