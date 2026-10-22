package com.appointmentbookingapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Business rule 3 source: appointment duration is derived from
// this entity's durationMinutes, never from client input.
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalService {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    // Service type identity, e.g. CHECKUP, ROOT_CANAL.
    @NotBlank(message = "Service name is required")
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull(message = "Service duration is required")
    @Positive(message = "Service duration must be a positive number of minutes")
    @Column(nullable = false)
    private Integer durationMinutes;
}
