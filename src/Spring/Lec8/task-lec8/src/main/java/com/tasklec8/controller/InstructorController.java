package com.tasklec8.controller;

import com.tasklec8.dto.CourseResponse;
import com.tasklec8.dto.CourseSimpleResponse;
import com.tasklec8.dto.InstructorResponse;
import com.tasklec8.dto.InstructorSimpleResponse;
import com.tasklec8.model.Course;
import com.tasklec8.model.Instructor;
import com.tasklec8.service.InstructorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instructors")
public class InstructorController {

    private InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @PostMapping
    public InstructorSimpleResponse createInstructor(@Valid  @RequestBody InstructorSimpleResponse instructor){
        return instructorService.createInstructor(instructor);
    }

    @GetMapping("/{id}")
    public InstructorResponse getInstructorById(@PathVariable long id){
        return instructorService.getInstructorById(id);
    }

    @GetMapping
    public List<InstructorSimpleResponse> getAllInstructors(){
        return instructorService.getAllInstructors();
    }

    @GetMapping("{id}/courses")
    public List<CourseSimpleResponse> getCoursesTaughtByAnInstructor(@PathVariable long id) {
        return instructorService.getCoursesTaughtByAnInstructor(id);
    }
}
