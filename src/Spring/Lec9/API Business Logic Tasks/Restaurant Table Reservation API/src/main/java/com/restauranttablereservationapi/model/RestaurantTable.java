package com.restauranttablereservationapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// The class is named RestaurantTable, not Table, because @Table is a JPA
// annotation and a class with that name would shadow it. RT_RESTAURANT_TABLE
// avoids the reserved word TABLE in Oracle as well.
@Entity
@Table(name = "RT_RESTAURANT_TABLE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "Table number is required")
    @Size(max = 10, message = "Table number cannot exceed 10 characters")
    @Column(name = "table_number", nullable = false, unique = true, length = 10)
    private String tableNumber;

    @NotNull(message = "Table capacity is required")
    @Min(value = 1, message = "Table capacity must be at least 1")
    @Column(nullable = false)
    private Integer capacity;

    // false = the table is out of service and cannot be reserved.
    @Column(nullable = false)
    private boolean active = true;
}
