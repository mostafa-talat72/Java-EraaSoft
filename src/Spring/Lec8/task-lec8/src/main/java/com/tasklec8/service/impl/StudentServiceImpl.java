package com.tasklec8.service.impl;

import com.tasklec8.dto.*;
import com.tasklec8.exception.StudentException;
import com.tasklec8.model.Course;
import com.tasklec8.model.Student;
import com.tasklec8.repo.StudentRepo;
import com.tasklec8.service.CourseService;
import com.tasklec8.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private StudentRepo studentRepo;
    private CourseService courseService;
    @Autowired
    public StudentServiceImpl(StudentRepo studentRepo, CourseService courseService) {
        this.studentRepo = studentRepo;
        this.courseService = courseService;
    }

    @Override
    public StudentSimpleResponse createStudent(StudentSimpleResponse student){
        if(Objects.nonNull(student.getId())){
            throw new StudentException(
                    "id",
                    "Student ID must be null"
            );
        }

        if (studentRepo.existsByEmail(student.getEmail())) {
            throw new StudentException(
                    "email",
                    "Email already exists: " + student.getEmail()
            );
        }
        Student studentSave = new Student(
                student.getName(),
                student.getEmail()
        );
        studentSave = studentRepo.save(studentSave);
        return new StudentSimpleResponse(
                studentSave.getId(),
                studentSave.getName(),
                studentSave.getEmail()
        );
    }

    @Override
    public StudentResponse getStudentById(long id) {
        Optional<Student> studentOptional = studentRepo.findById(id);
        if(studentOptional.isEmpty()){
            throw new StudentException(
                    "id",
                    "Student dose not exists with id: " + id
            );
        }

        Student student = studentOptional.get();

        List<CourseInsideStudent> courseInsideStudentList = student.getCourses().stream()
                .map(course -> new CourseInsideStudent(
                        course.getId(),
                        course.getTitle(),
                        course.getDescription(),
                        new InstructorSimpleResponse(
                                course.getInstructor().getId(),
                                course.getInstructor().getName(),
                                course.getInstructor().getEmail()
                        )
                )).toList();

        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getEmail(),
                courseInsideStudentList
        );
    }

    @Override
    public List<StudentSimpleResponse> getAllStudents() {
        List<Student> students = studentRepo.findAll();

        return students.stream().map(student ->  new StudentSimpleResponse(
                    student.getId(),
                    student.getName(),
                    student.getEmail()
            )
        ).toList();
    }

    @Override
    public StudentResponse registerStudentToCourse(long studentId, long courseId) {
        Optional<Student> studentOptional = studentRepo.findById(studentId);
        if(studentOptional.isEmpty()){
            throw new StudentException(
                    "id",
                    "Student dose not exists with id: " + studentId
            );
        }
        CourseResponse courseResponse = courseService.getCourseById(courseId);
        Student student = studentOptional.get();
        student.getCourses().add(new Course(
                courseResponse.getId(),
                courseResponse.getTitle(),
                courseResponse.getDescription()
        ));
        studentRepo.save(student);
        return getStudentById(studentId);
    }
}
