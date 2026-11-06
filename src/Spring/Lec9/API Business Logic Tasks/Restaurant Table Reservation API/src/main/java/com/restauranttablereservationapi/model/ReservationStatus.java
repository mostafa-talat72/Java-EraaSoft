package com.restauranttablereservationapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Lookup table for reservation states (seeded with PENDING, CONFIRMED,
// CANCELLED, EXPIRED). Keeping the state in a row lets the service resolve it
// by name and keeps the stored reservations consistent with the seed.
@Entity
@Table(name = "RT_RESERVATION_STATUS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "Reservation status name is required")
    @Size(max = 20, message = "Reservation status name cannot exceed 20 characters")
    @Column(nullable = false, unique = true, length = 20)
    private String name;
}
