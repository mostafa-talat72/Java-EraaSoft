package com.restauranttablereservationapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// No reservations collection here: Reservation owns the relation, so the
// entity stays simple and the DTO mapping cannot recurse back to Customer.
@Entity
@Table(name = "RT_CUSTOMER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "Customer name is required")
    @Size(max = 100, message = "Customer name cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    // The phone is the customer's real identity in a restaurant.
    @NotBlank(message = "Customer phone is required")
    @Size(max = 20, message = "Customer phone cannot exceed 20 characters")
    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Email(message = "Customer email must be valid")
    @Size(max = 100, message = "Customer email cannot exceed 100 characters")
    @Column(length = 100)
    private String email;
}
