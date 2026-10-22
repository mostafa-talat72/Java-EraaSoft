package com.appointmentbookingapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorUnavailabilityDTO {

    private Long id;

    private DoctorSimpleDTO doctor;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;
}
