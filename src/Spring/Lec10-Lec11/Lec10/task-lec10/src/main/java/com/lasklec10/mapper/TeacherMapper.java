package com.lasklec10.mapper;

import com.lasklec10.dto.TeacherDTO;
import com.lasklec10.dto.TeacherSimpleDTO;
import com.lasklec10.model.Teacher;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    public Teacher convertFromTeacherDtoToTeacher(TeacherDTO teacherDTO);

    public TeacherDTO convertFromTeacherToTeacherDto(Teacher teacher);

    public List<Teacher> convertFromTeacherDtoListToTeacherList(List<TeacherDTO> teacherDTOList);

    public List<TeacherDTO> convertFromTeacherListToTeacherDtoList(List<Teacher> teachers);

    public Teacher convertFromTeacherSimpleDtoToTeacher(TeacherSimpleDTO teacherSimpleDTO);

    public TeacherSimpleDTO convertFromTeacherToTeacherSimpleDto(Teacher teacher);

    public List<Teacher> convertFromTeacherSimpleDtoListToTeacherList(List<TeacherSimpleDTO> teacherSimpleDTOList);

    public List<TeacherSimpleDTO> convertFromTeacherListToTeacherSimpleDtoList(List<Teacher> teachers);

}
