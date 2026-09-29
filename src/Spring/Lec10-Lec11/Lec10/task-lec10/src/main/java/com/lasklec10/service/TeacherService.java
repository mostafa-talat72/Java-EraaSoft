package com.lasklec10.service;

import com.lasklec10.dto.TeacherDTO;

import java.util.List;

public interface TeacherService {

    public List<TeacherDTO> getAllTeacher();

    public List<TeacherDTO> getTeachersByIds( List<Long> ids);
}
