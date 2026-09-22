package com.tasklec8.controller;

import com.tasklec8.dto.CourseResponse;
import com.tasklec8.dto.CourseSimpleResponse;
import com.tasklec8.model.Course;
import com.tasklec8.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public CourseSimpleResponse createCourse(@Valid @RequestBody CourseSimpleResponse course){
        return courseService.createCourse(course);
    }

    @GetMapping("/{id}")
    public CourseResponse getCourseById(@PathVariable long id){
        return courseService.getCourseById(id);
    }

    @GetMapping
    public List<CourseSimpleResponse> getAllCourses(){
        return courseService.getAllCourses();
    }

    @PostMapping("/{courseId}/instructors/{instructorId}")
    public CourseResponse assignInstructorToCourse(@PathVariable long courseId,@PathVariable long instructorId){
        return courseService.assignInstructorToCourse(courseId, instructorId);
    }
}
