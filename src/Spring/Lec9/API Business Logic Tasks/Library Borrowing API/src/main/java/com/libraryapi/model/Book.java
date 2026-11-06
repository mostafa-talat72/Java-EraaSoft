package com.libraryapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "LB_BOOK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "Book title is required")
    @Size(max = 150, message = "Book title cannot exceed 150 characters")
    @Column(nullable = false, length = 150)
    private String title;

    @NotBlank(message = "Book author is required")
    @Size(max = 100, message = "Book author cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String author;

    @NotBlank(message = "Book isbn is required")
    @Size(max = 20, message = "Book isbn cannot exceed 20 characters")
    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @NotNull(message = "Total copies is required")
    @Min(value = 1, message = "A book must have at least one copy")
    @Column(name = "total_copies", nullable = false)
    private Integer totalCopies;

    // Copies currently on the shelf: -1 on borrow, +1 on return (rule 1).
    // No @NotNull here: Bean Validation may run before @PrePersist fills the value.
    @Column(name = "available_copies", nullable = false)
    private Integer availableCopies;

    @PrePersist
    void onCreate() {
        if (Objects.isNull(availableCopies)) {
            availableCopies = totalCopies;
        }
    }
}
