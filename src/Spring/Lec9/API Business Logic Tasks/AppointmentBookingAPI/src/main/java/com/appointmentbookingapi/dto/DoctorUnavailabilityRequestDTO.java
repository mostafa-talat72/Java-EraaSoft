package com.appointmentbookingapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// Blocks a time window for one doctor (rule 4). The cross-field
// end-after-start check lives in the service layer.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorUnavailabilityRequestDTO {

    @NotNull(message = "Doctor id is required")
    private Long doctorId;

    @NotNull(message = "Unavailability start time is required")
    private LocalDateTime startDateTime;

    @NotNull(message = "Unavailability end time is required")
    private LocalDateTime endDateTime;
}
