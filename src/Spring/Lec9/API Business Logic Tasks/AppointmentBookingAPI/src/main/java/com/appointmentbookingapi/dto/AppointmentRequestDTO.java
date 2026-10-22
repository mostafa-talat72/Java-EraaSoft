package com.appointmentbookingapi.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// Booking request. Flat ids resolved in the service layer, so a missing
// doctor/patient/service becomes a clear 400 instead of a JSON error.
//END is NEVER sent: the caller cannot influence duration (rule 3).
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO {

    @NotNull(message = "Doctor id is required")
    private Long doctorId;

    @NotNull(message = "Patient id is required")
    private Long patientId;

    @NotNull(message = "Service id is required")
    private Long serviceId;

    @NotNull(message = "Appointment time is required")
    @Future(message = "Appointment time must be in the future")
    private LocalDateTime appointmentDateTime;
}
