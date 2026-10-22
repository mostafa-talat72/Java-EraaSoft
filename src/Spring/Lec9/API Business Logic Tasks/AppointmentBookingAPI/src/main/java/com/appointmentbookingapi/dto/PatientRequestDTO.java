package com.appointmentbookingapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequestDTO {

    @NotBlank(message = "Patient name is required")
    @Size(max = 100, message = "Patient name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Patient email is required")
    @Email(message = "Invalid patient email format")
    private String email;
}
