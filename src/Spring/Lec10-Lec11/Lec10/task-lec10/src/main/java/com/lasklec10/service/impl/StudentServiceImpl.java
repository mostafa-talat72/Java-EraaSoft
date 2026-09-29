package com.lasklec10.service.impl;

import com.lasklec10.dto.StudentDTO;
import com.lasklec10.exception.StudentException;
import com.lasklec10.mapper.StudentMapper;
import com.lasklec10.model.Student;
import com.lasklec10.repo.StudentRepo;
import com.lasklec10.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl  implements StudentService {

    private StudentRepo studentRepo;

    private StudentMapper studentMapper;

    @Autowired
    public StudentServiceImpl(StudentRepo studentRepo, StudentMapper studentMapper) {
        this.studentRepo = studentRepo;
        this.studentMapper = studentMapper;
    }

    @Override
    public List<StudentDTO> getAllStudent() {
        List<Student> students = studentRepo.findAll();
        if(students.isEmpty()){
            throw new StudentException(
                    "students",
                    "No students found"
            );
        }

        return studentMapper.convertFromStudentListToStudentDtoList(students);
    }

    @Override
    public List<StudentDTO> getStudentsByIdS(List<Long> ids) {
        Set<Long> distinctIds = new HashSet<>();
        Set<String> errors = ids.stream()
                .filter(id -> !distinctIds.add(id))
                .map(id -> id.toString())
                .collect(Collectors.toSet());
        if (!errors.isEmpty()){
            throw new StudentException("student", "Duplicate id in request: " + String.join(", ", errors));
        }

        errors = distinctIds.stream()
                .filter(id -> !studentRepo.existsById(id))
                .map(id -> id.toString()).collect(Collectors.toSet());
        if (!errors.isEmpty()) {
            throw new StudentException("student", "This ids dose not exists: " + String.join(", ", errors));
        }

        return studentMapper.convertFromStudentListToStudentDtoList(studentRepo.findAllById(distinctIds));
    }
}
