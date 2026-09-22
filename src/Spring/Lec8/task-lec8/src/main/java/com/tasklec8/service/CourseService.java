package com.tasklec8.service;

import com.tasklec8.dto.CourseResponse;
import com.tasklec8.dto.CourseSimpleResponse;
import com.tasklec8.model.Course;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CourseService {
    public CourseSimpleResponse createCourse(@RequestBody CourseSimpleResponse course);

    public CourseResponse getCourseById(long id);

    public List<CourseSimpleResponse> getAllCourses();

    public CourseResponse assignInstructorToCourse(long courseId, long instructorId);
}
