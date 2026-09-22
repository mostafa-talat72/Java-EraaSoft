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
public class CourseSimpleResponse {

    private Long id;

    @NotNull(message = "Course title must not be null")
    @NotBlank(message = "Course title must not be blank")
    private String title;

    private String description;

    public CourseSimpleResponse(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
