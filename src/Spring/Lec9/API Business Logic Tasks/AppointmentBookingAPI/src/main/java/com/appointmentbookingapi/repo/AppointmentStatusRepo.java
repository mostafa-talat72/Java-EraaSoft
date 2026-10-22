package com.appointmentbookingapi.repo;

import com.appointmentbookingapi.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppointmentStatusRepo extends JpaRepository<AppointmentStatus, Long> {

    Optional<AppointmentStatus> findByName(String name);
}
