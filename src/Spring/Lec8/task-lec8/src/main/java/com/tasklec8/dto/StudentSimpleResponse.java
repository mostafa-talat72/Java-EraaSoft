package com.tasklec8.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentSimpleResponse {
    private Long id;

    @NotNull(message = "Student name must not be null")
    @NotBlank(message = "Student name must not be blank")
    private String name;

    @NotNull(message = "Student email must not be null")
    @NotBlank(message = "Student email must not be blank")
    @Email(message = "Student email must be valid")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Student email must contain a valid domain"
    )
    private String email;

    public StudentSimpleResponse(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
