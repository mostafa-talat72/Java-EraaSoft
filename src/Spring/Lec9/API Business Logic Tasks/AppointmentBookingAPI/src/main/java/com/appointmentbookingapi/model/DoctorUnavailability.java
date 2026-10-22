package com.appointmentbookingapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// Business rule 4: the doctor is unavailable inside these windows.
// Booking service must reject any appointment whose time range
// overlaps any of the doctor's unavailability windows.
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorUnavailability {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull(message = "Doctor is required")
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotNull(message = "Unavailability start time is required")
    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @NotNull(message = "Unavailability end time is required")
    @Column(nullable = false)
    private LocalDateTime endDateTime;

    // Cross-field check: the window must be a valid interval.
    // Validation-only method, not a JPA mapping (field access mode).
    @AssertTrue(message = "Unavailability end time must be after start time")
    boolean isTimeRangeValid() {
        if (startDateTime == null || endDateTime == null) {
            return true; // NotNull handles the missing ones
        }
        return endDateTime.isAfter(startDateTime);
    }

    public boolean is() {
        return false;
    }
}
