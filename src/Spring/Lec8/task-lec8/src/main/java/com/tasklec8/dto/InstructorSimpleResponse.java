package com.tasklec8.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstructorSimpleResponse {

    private Long id;

    @NotNull(message = "Instructor name must not be null")
    @NotBlank(message = "Instructor name must not be blank")
    private String name;

    @NotNull(message = "Instructor email must not be null")
    @NotBlank(message = "Instructor email must not be blank")
    @Email(message = "Instructor email must be valid")
    private String email;

    public InstructorSimpleResponse(String name, String email) {
        this.name = name;
        this.email = email;
    }

}
