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
public class SpecialtyRequestDTO {

    @NotBlank(message = "Specialty name is required")
    @Size(max = 100, message = "Specialty name must be at most 100 characters")
    private String name;
}
