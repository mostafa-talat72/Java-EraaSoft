package com.tasklec8.service.impl;

import com.tasklec8.dto.*;
import com.tasklec8.exception.InstructorException;
import com.tasklec8.model.Course;
import com.tasklec8.model.Instructor;
import com.tasklec8.repo.InstructorRepo;
import com.tasklec8.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class InstructorServiceImpl implements InstructorService {

    private InstructorRepo instructorRepo;

    @Autowired
    public InstructorServiceImpl(InstructorRepo instructorRepo) {
        this.instructorRepo = instructorRepo;
    }


    @Override
    public InstructorSimpleResponse createInstructor(InstructorSimpleResponse instructorSimpleResponse) {
        if(Objects.nonNull(instructorSimpleResponse.getId())){
            throw new InstructorException(
                    "id",
                    "Instructor ID must be null"
            );
        }
        if(instructorRepo.existsByEmail(instructorSimpleResponse.getEmail())){
            throw new InstructorException(
                    "email",
                    "Email already exists: " + instructorSimpleResponse.getEmail()
            );
        }
        Instructor instructor = new Instructor(
                instructorSimpleResponse.getName(),
                instructorSimpleResponse.getEmail()
        );
        instructor = instructorRepo.save(instructor);
        return new InstructorSimpleResponse(
                instructor.getId(),
                instructor.getName(),
                instructor.getEmail()
        );
    }

    @Override
    public InstructorResponse getInstructorById(long id) {
        Optional<Instructor> instructorOptional = instructorRepo.findById(id);

        if(instructorOptional.isEmpty()){
            throw new InstructorException(
                    "id",
                    "Instructor dose not exists with id: " + id
            );
        }
        Instructor instructor = instructorOptional.get();
        return new InstructorResponse(
                instructor.getId(),
                instructor.getName(),
                instructor.getEmail(),
                instructor.getCourses().stream()
                        .map(course -> new CourseInsideInstructor(
                                course.getId(),
                                course.getTitle(),
                                course.getDescription(),
                                course.getStudents().stream()
                                        .map(student -> new StudentSimpleResponse(
                                                student.getId(),
                                                student.getName(),
                                                student.getEmail()
                                        ))
                                        .toList()
                        ))
                        .toList()
        );
    }

    @Override
    public List<InstructorSimpleResponse> getAllInstructors() {
        List<Instructor> instructors = instructorRepo.findAll();

        if(instructors.isEmpty()){
            throw new InstructorException(
                    "Instructors",
                    "No Instructors found"
            );
        }

        return instructorRepo.findAll().stream()
                .map(instructor -> new InstructorSimpleResponse(
                        instructor.getId(),
                        instructor.getName(),
                        instructor.getEmail()
                )).toList();
    }

    @Override
    public List<CourseSimpleResponse> getCoursesTaughtByAnInstructor(long id) {
        Optional<Instructor> instructorOptional = instructorRepo.findById(id);

        if(instructorOptional.isEmpty()){
            throw new InstructorException(
                    "id",
                    "Instructor dose not exists with id: " + id
            );
        }
        return instructorOptional.get().getCourses().stream()
                .map(course -> new CourseSimpleResponse(
                        course.getId(),
                        course.getTitle(),
                        course.getDescription()
                )).toList();
    }

}
