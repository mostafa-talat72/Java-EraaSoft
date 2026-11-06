package com.restauranttablereservationapi.dto;

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
public class ReservationStatusRequestDTO {

    @NotBlank(message = "Reservation status name is required")
    @Size(max = 20, message = "Reservation status name cannot exceed 20 characters")
    private String name;
}
