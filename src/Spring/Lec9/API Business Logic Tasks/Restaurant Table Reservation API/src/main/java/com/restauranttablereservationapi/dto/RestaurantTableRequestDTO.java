package com.restauranttablereservationapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Payload for POST/PUT /tables. A missing "active" keeps the default true.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantTableRequestDTO {

    @NotBlank(message = "Table number is required")
    @Size(max = 10, message = "Table number cannot exceed 10 characters")
    private String tableNumber;

    @NotNull(message = "Table capacity is required")
    @Min(value = 1, message = "Table capacity must be at least 1")
    private Integer capacity;

    private Boolean active = true;
}
