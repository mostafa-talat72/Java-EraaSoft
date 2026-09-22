package com.tasklec8.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstructorResponse {
    private Long id;

    @NotNull(message = "Instructor name must not be null")
    @NotBlank(message = "Instructor name must not be blank")
    private String name;

    @NotNull(message = "Instructor email must not be null")
    @NotBlank(message = "Instructor email must not be blank")
    @Email(message = "Instructor email must be valid")
    private String email;

    private List<CourseInsideInstructor> courses = new ArrayList<>();

    public InstructorResponse(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public InstructorResponse(String name, String email,  List<CourseInsideInstructor> courses) {
        this.name = name;
        this.email = email;
        this.courses = courses;
    }

}
