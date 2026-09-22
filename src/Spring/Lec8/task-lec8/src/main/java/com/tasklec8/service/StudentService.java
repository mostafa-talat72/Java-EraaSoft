package com.tasklec8.service;

import com.tasklec8.dto.StudentResponse;
import com.tasklec8.dto.StudentSimpleResponse;
import com.tasklec8.model.Student;
import jakarta.transaction.SystemException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface StudentService {

    public StudentSimpleResponse createStudent(StudentSimpleResponse student) ;

    public StudentResponse getStudentById(long id) ;

    public List<StudentSimpleResponse> getAllStudents();

    public StudentResponse registerStudentToCourse(long studentId, long courseId) ;

}
