package com.productdiscountapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "Customer name is required")
    @Column(nullable = false)
    private String name;

    // VIP customers receive an additional discount in the calculation flow.
    @NotNull(message = "VIP flag is required")
    @Column(nullable = false)
    private Boolean vip;
}
