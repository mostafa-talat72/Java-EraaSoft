package com.appointmentbookingapi.repo;

import com.appointmentbookingapi.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {

    // Rule 1 candidates: doctor's (BOOKED) appointments inside a window.
    // Final overlap (existingStart < newEnd && existingEnd > newStart) is
    // evaluated in Java because end = start + service.durationMinutes is computed.
    List<Appointment> findByDoctorIdAndStatusNameAndAppointmentDateTimeBetween(
            Long doctorId, String statusName, LocalDateTime from, LocalDateTime to);

    // Rule 2 counter: patient's BOOKED appointments inside a day window.
    long countByPatientIdAndStatusNameAndAppointmentDateTimeBetween(
            Long patientId, String statusName, LocalDateTime from, LocalDateTime to);
}
