package com.restauranttablereservationapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// Payload for POST /reservations. The client chooses table, customer, start
// time, duration and party size; status and timestamps are server-side only.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDTO {

    @NotNull(message = "Table id is required")
    private Long tableId;

    @NotNull(message = "Customer id is required")
    private Long customerId;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    // endTime is computed from this value (rule 1 needs a concrete window).
    @NotNull(message = "Duration is required")
    @Min(value = 30, message = "Duration must be at least 30 minutes")
    @Max(value = 240, message = "Duration cannot exceed 240 minutes")
    private Integer durationMinutes;

    @NotNull(message = "Guests is required")
    @Min(value = 1, message = "At least one guest is required")
    private Integer guests;
}
