package com.lec9.employeeemailtask.dto;

import com.lec9.employeeemailtask.validation.ValidEmailType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ValidEmailType
public class EmailSimpleResponse {
    private Long id;

    @NotBlank(message = "Email type is required")
    @Pattern(
            regexp = "^[A-Za-z][A-Za-z0-9]*$",
            message = "Email type must start with a letter and contain only letters and numbers"
    )
    private String name;

    @NotBlank(message = "Email content is required")
    @Email(message = "Invalid email format")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email must contain a valid domain"
    )
    private String content;
}
