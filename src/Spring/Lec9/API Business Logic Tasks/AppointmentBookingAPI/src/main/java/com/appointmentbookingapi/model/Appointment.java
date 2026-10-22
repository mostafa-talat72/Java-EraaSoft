package com.appointmentbookingapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// The core entity. Rules 1, 2, 5 and 6 are all evaluated against this.
// Only the START time is stored; the end is always computed on the fly
// as start.plusMinutes(service.durationMinutes) — single source of truth.
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull(message = "Doctor is required")
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotNull(message = "Patient is required")
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull(message = "Service is required")
    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private MedicalService service;

    @NotNull(message = "Appointment time is required")
    @Future(message = "Appointment time must be in the future")
    @Column(nullable = false)
    private LocalDateTime appointmentDateTime;

    // Status defaulted by the service layer on create (BOOKED);
    // no @NotNull on purpose: Bean Validation runs before persistence.
    @ManyToOne
    @JoinColumn(name = "status_id")
    private AppointmentStatus status;

    @PastOrPresent(message = "Creation date cannot be in the future")
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
