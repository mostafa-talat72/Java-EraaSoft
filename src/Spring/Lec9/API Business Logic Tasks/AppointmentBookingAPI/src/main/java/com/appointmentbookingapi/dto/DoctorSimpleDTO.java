package com.appointmentbookingapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Slim view for nesting inside appointments and unavailability windows.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSimpleDTO {

    private Long id;

    private String name;
}
