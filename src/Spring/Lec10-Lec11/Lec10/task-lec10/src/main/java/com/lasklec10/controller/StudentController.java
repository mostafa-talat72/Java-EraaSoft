package com.lasklec10.controller;

import com.lasklec10.dto.StudentDTO;
import com.lasklec10.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudent(){
        List<StudentDTO> studentDTOList = studentService.getAllStudent();
        return ResponseEntity.ok(studentDTOList);
    }

    @GetMapping("/by-ids")
    public ResponseEntity<List<StudentDTO>> getStudentById(@RequestParam List<Long> ids){
        List<StudentDTO> studentDTOList = studentService.getStudentsByIdS(ids);
        return ResponseEntity.ok(studentDTOList);
    }


}
