package com.appointmentbookingapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Slim view for nesting inside doctors, appointments.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialtySimpleDTO {

    private Long id;

    private String name;
}
