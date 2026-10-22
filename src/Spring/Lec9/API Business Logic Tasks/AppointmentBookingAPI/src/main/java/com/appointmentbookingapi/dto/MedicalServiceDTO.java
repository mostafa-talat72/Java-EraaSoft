package com.appointmentbookingapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Business rule 3: durationMinutes here is the authoritative value
// used to compute every appointment end time.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalServiceDTO {

    private Long id;

    private String name;

    private Integer durationMinutes;
}
