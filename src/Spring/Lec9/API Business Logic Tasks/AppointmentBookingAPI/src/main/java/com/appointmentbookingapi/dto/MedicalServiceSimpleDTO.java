package com.appointmentbookingapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Includes durationMinutes on purpose: appointment responses need the
// full service identity to be self-describing (start + duration -> end).
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalServiceSimpleDTO {

    private Long id;

    private String name;

    private Integer durationMinutes;
}
