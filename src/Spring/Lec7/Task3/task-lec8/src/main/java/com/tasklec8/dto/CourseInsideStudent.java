package com.tasklec8.dto;

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
public class CourseInsideStudent {

    private Long id;

    @NotNull(message = "Course title must not be null")
    @NotBlank(message = "Course title must not be blank")
    private String title;

    private String description;

    private InstructorSimpleResponse instructor ;

    public CourseInsideStudent(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public CourseInsideStudent(String title, String description, InstructorSimpleResponse instructor) {
        this.title = title;
        this.description = description;
        this.instructor = instructor;
    }

}
