package com.lasklec10.controller;

import com.lasklec10.dto.TeacherDTO;
import com.lasklec10.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public ResponseEntity<List<TeacherDTO>> getAllTeacher(){
        List<TeacherDTO> teacherDTOList = teacherService.getAllTeacher();

        return ResponseEntity.ok(teacherDTOList);
    }

    @GetMapping("/by-ids")
    public ResponseEntity<List<TeacherDTO>> getTeacherById(@RequestParam List<Long> ids){
        List<TeacherDTO> teacherDTOList = teacherService.getTeachersByIds(ids);
        return ResponseEntity.ok(teacherDTOList);
    }
}
