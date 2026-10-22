package com.appointmentbookingapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRequestDTO {

    @NotBlank(message = "Doctor name is required")
    @Size(max = 100, message = "Doctor name must be at most 100 characters")
    private String name;

    // Resolved by id in the service layer (must reference an existing specialty).
    @NotNull(message = "Specialty id is required")
    private Long specialtyId;
}
