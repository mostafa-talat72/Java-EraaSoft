package com.lasklec10.service.impl;

import com.lasklec10.dto.TeacherDTO;
import com.lasklec10.exception.TeacherException;
import com.lasklec10.mapper.TeacherMapper;
import com.lasklec10.model.Teacher;
import com.lasklec10.repo.TeacherRepo;
import com.lasklec10.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

    private TeacherRepo teacherRepo;

    private TeacherMapper teacherMapper;

    @Autowired
    public TeacherServiceImpl(TeacherRepo teacherRepo, TeacherMapper teacherMapper) {
        this.teacherRepo = teacherRepo;
        this.teacherMapper = teacherMapper;
    }


    @Override
    public List<TeacherDTO> getAllTeacher() {
        List<Teacher> teachers = teacherRepo.findAll();
        if(teachers.isEmpty()){
            throw new TeacherException(
                    "Teacher",
                    "No teacher found"
            );
        }
        System.out.println(teachers);
        return teacherMapper.convertFromTeacherListToTeacherDtoList(teachers);
    }

    @Override
    public List<TeacherDTO> getTeachersByIds(List<Long> ids) {
        Set<Long> distinctIds = new HashSet<>();
        Set<String> errors = ids.stream()
              .filter(id -> !distinctIds.add(id))
              .map(id -> id.toString())
              .collect(Collectors.toSet());
        if (!errors.isEmpty()){
            throw new TeacherException("teacher", "Duplicate id in request: " + String.join(", ", errors));
        }

        errors = distinctIds.stream()
              .filter(id -> !teacherRepo.existsById(id))
              .map(id -> id.toString()).collect(Collectors.toSet());
        if (!errors.isEmpty()) {
            throw new TeacherException("teacher", "This ids dose not exists: " + String.join(", ", errors));
        }

        return teacherMapper.convertFromTeacherListToTeacherDtoList(teacherRepo.findAllById(distinctIds));
    }
}
