package com.tasklec8.service.impl;

import com.tasklec8.dto.*;
import com.tasklec8.exception.CourseException;
import com.tasklec8.model.Course;
import com.tasklec8.model.Instructor;
import com.tasklec8.repo.CourseRepo;
import com.tasklec8.service.CourseService;
import com.tasklec8.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private CourseRepo courseRepo;
    private InstructorService instructorService;
    @Autowired
    public CourseServiceImpl(CourseRepo courseRepo, InstructorService instructorService) {
        this.courseRepo = courseRepo;
        this.instructorService = instructorService;
    }

    @Override
    public CourseSimpleResponse createCourse(CourseSimpleResponse courseSimpleResponse) {
        if(Objects.nonNull(courseSimpleResponse.getId())){
            throw new CourseException(
                    "id",
                    "Course ID must be null"
            );
        }

        if(courseRepo.existsByTitle(courseSimpleResponse.getTitle()))
        {
            throw new CourseException(
                    "title",
                    "Title already exists: " + courseSimpleResponse.getTitle()
            );
        }
        Course course = new Course(
                courseSimpleResponse.getTitle(),
                courseSimpleResponse.getDescription()
        );
        course = courseRepo.save(course);

        return new CourseSimpleResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription()
        );
    }

    @Override
    public CourseResponse getCourseById(long id) {
        Optional<Course> courseOptional = courseRepo.findById(id);
        if(courseOptional.isEmpty()){
            throw new CourseException(
                    "id",
                    "Course dose not exists with id: " + id
            );
        }
        Course course = courseOptional.get();
        List<StudentSimpleResponse> studentSimpleResponses = course.getStudents().stream()
                .map(student -> new StudentSimpleResponse(
                        student.getId(),
                        student.getName(),
                        student.getEmail()
                )).toList();

        InstructorSimpleResponse instructorSimpleResponse = new InstructorSimpleResponse(
                course.getInstructor().getId(),
                course.getInstructor().getName(),
                course.getInstructor().getEmail()
        );

        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                instructorSimpleResponse,
                studentSimpleResponses
        );
    }

    @Override
    public List<CourseSimpleResponse> getAllCourses() {
        List<Course> courses = courseRepo.findAll();
        if (courses.isEmpty()){
            throw new CourseException(
                    "courses",
                    "No Courses found"
            );
        }
        return courses.stream().map(course -> new CourseSimpleResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription()
        )).toList();
    }

    @Override
    public CourseResponse assignInstructorToCourse(long courseId, long instructorId) {
        Optional<Course> courseOptional = courseRepo.findById(courseId);
        if(courseOptional.isEmpty()){
            throw new CourseException(
                    "id",
                    "Course dose not exists with id: " + courseId
            );
        }
        Course course = courseOptional.get();
        InstructorResponse instructorResponse = instructorService.getInstructorById(instructorId);


        course.setInstructor(new Instructor(
                instructorResponse.getId(),
                instructorResponse.getName(),
                instructorResponse.getEmail()
        ));
        courseRepo.save(course);
        return getCourseById(courseId);
    }
}
