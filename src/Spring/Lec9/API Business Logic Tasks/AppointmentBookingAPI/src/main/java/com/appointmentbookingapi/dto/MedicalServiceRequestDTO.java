package com.appointmentbookingapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalServiceRequestDTO {

    @NotBlank(message = "Service name is required")
    private String name;

    @NotNull(message = "Service duration is required")
    @Positive(message = "Service duration must be a positive number of minutes")
    private Integer durationMinutes;
}
