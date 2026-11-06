package com.libraryapi.model;

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

// Lookup table for borrow states (seeded with BORROWED, OVERDUE, RETURNED).
// RETURNED  - it has a returnDate (on time or late; see the fine field).
// OVERDUE   - still out and its dueDate is before today.
// BORROWED  - still out and not late yet.
//
// The service keeps it up to date: BORROWED on borrow, RETURNED on return,
// and open rows are flipped to OVERDUE whenever they are read after the due date.
@Entity
@Table(name = "LB_BORROW_STATUS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "Borrow status name is required")
    @Size(max = 20, message = "Borrow status name cannot exceed 20 characters")
    @Column(nullable = false, unique = true, length = 20)
    private String name;
}
