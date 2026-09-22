package com.tasklec8.service;

import com.tasklec8.dto.CourseSimpleResponse;
import com.tasklec8.dto.InstructorResponse;
import com.tasklec8.dto.InstructorSimpleResponse;
import com.tasklec8.model.Course;
import com.tasklec8.model.Instructor;

import java.util.List;

public interface InstructorService {

    public InstructorSimpleResponse createInstructor(InstructorSimpleResponse instructor);

    public InstructorResponse getInstructorById(long id);

    public List<InstructorSimpleResponse> getAllInstructors();

    public List<CourseSimpleResponse> getCoursesTaughtByAnInstructor(long id);
}
