package com.lasklec10.service;

import com.lasklec10.dto.StudentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface StudentService {

    public List<StudentDTO> getAllStudent();

    public List<StudentDTO> getStudentsByIdS(List<Long> ids);
}
