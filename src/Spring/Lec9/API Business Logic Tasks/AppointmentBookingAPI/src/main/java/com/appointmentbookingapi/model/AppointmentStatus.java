package com.appointmentbookingapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    // Only two states per task: BOOKED -> CANCELED (one-way transition).
    @NotBlank(message = "Status name is required")
    @Pattern(regexp = "^(BOOKED|CANCELED)$", message = "Status must be one of: BOOKED, CANCELED")
    @Column(nullable = false, unique = true)
    private String name;
}
