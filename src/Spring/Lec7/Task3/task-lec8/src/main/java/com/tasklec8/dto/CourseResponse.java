package com.tasklec8.dto;

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
public class CourseResponse {

    private Long id;

    @NotNull(message = "Course title must not be null")
    @NotBlank(message = "Course title must not be blank")
    private String title;

    private String description;

    private InstructorSimpleResponse instructor ;

    private List<StudentSimpleResponse> students = new ArrayList<>();

    public CourseResponse(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public CourseResponse(String title, String description, InstructorSimpleResponse instructor) {
        this.title = title;
        this.description = description;
        this.instructor = instructor;
    }

    public CourseResponse(String title, String description, List<StudentSimpleResponse> students) {
        this.title = title;
        this.description = description;
        this.students = students;
    }

    public CourseResponse(String title, String description, InstructorSimpleResponse instructor, List<StudentSimpleResponse> students) {
        this.title = title;
        this.description = description;
        this.instructor = instructor;
        this.students = students;
    }
}
