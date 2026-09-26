package com.lec9.employeeemailtask.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Lightweight employee view (used nested inside EmailResponse; only id is required for linking).
public class EmployeeSimpleResponse {

    private Long id;

    @NotBlank(message = "Employee name is required")
    private String name;

    @NotNull(message = "Employee age is required")
    @Min(value = 16, message = "Employee age must be greater than 15")
    @Max(value = 39, message = "Employee age must be less than 40")
    private Integer age;

    @NotNull(message = "Employee salary is required")
    @DecimalMin(
            value = "5000",
            inclusive = false,
            message = "Employee salary must be greater than 5000"
    )
    @DecimalMax(
            value = "10000",
            inclusive = false,
            message = "Employee salary must be less than 10000"
    )
    private Double salary;
}
