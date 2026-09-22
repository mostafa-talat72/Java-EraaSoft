package com.tasklec8.controller;

import com.tasklec8.dto.StudentResponse;
import com.tasklec8.dto.StudentSimpleResponse;
import com.tasklec8.model.Student;
import com.tasklec8.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public StudentSimpleResponse createStudent(@Valid @RequestBody StudentSimpleResponse student) {

        return studentService.createStudent(student);
    }

    @GetMapping("/{id}")
    public StudentResponse getStudentById(@PathVariable long id) {
        return studentService.getStudentById(id);
    }

    @GetMapping
    public List<StudentSimpleResponse> getAllStudents(){
        return studentService.getAllStudents();
    }

    @PostMapping("/{studentId}/courses/{courseId}")
    public StudentResponse registerStudentToCourse(
            @PathVariable long studentId,
            @PathVariable long courseId) {
        return studentService.registerStudentToCourse(studentId, courseId);
    }

}
