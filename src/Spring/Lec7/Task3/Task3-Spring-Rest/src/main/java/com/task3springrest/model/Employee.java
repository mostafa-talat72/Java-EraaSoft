package com.task3springrest.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name must be not empty")
    @Column(nullable = false)
    private String name;

    @Min(value = 18, message = "Age must be >= 18")
    @Max(value = 65, message = "Age must be <= 65")
    @NotNull(message = "Age is required")
    @Column(nullable = false)
    private Integer age;

    @NotNull(message = "Phone number is required")
    @NotBlank(message = "Phone number must be only numbers")
    @Pattern(regexp = "^01[0-9]{9}$", message = "Phone must be 11 digits starting with 01 (e.g. 01012345678)")
    @Column(nullable = false, length = 11, unique = true)
    @Check(constraints = "REGEXP_LIKE(phone_number, '^01[0-9]{9}$')")
    private String phoneNumber;

    public Employee(String name, Integer age, String phoneNumber) {
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }
}
