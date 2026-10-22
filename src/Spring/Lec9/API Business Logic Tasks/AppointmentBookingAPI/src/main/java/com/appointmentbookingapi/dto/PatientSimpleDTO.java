package com.appointmentbookingapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Slim view for nesting inside appointments (no back-reference, no cycles).
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientSimpleDTO {

    private Long id;

    private String name;

    private String email;
}
