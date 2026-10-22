package com.appointmentbookingapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// Full read view. endTime is COMPUTED in the mapper
// (start + service.durationMinutes) and never persisted.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {

    private Long id;

    private DoctorSimpleDTO doctor;

    private PatientSimpleDTO patient;

    private MedicalServiceSimpleDTO service;

    private LocalDateTime appointmentDateTime;

    private LocalDateTime endTime;

    private AppointmentStatusDTO status;

    private LocalDateTime createdAt;
}
