package com.appointmentbookingapi.repo;

import com.appointmentbookingapi.model.DoctorUnavailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DoctorUnavailabilityRepo extends JpaRepository<DoctorUnavailability, Long> {

    // Rule 4: windows overlapping the candidate slot
    // (start < newEnd AND end > newStart).
    List<DoctorUnavailability> findByDoctorIdAndStartDateTimeBeforeAndEndDateTimeAfter(
            Long doctorId, LocalDateTime before, LocalDateTime after);

    // Same overlap condition but stops at the first match -> cheaper when the
    // booking flow only needs ONE conflicting range (e.g. for the 400 message).
    Optional<DoctorUnavailability> findFirstByDoctorIdAndStartDateTimeBeforeAndEndDateTimeAfter(
            Long doctorId, LocalDateTime before, LocalDateTime after);
}
