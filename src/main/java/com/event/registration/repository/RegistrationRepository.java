package com.event.registration.repository;

import com.event.registration.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    long countByEventId(Long eventId);

    boolean existsByEventIdAndUserId(Long eventId, Long userId);
}
