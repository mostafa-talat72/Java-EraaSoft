package com.lasklec10.mapper;

import com.lasklec10.dto.StudentDTO;
import com.lasklec10.dto.StudentSimpleDTO;
import com.lasklec10.model.Student;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    public Student convertFromStudentDtoToStudent(StudentDTO studentDTO);

    public StudentDTO convertFromStudentToStudentDto(Student student);

    public List<Student> convertFromStudentDtoListToStudentList(List<StudentDTO> studentDTOList);

    public List<StudentDTO> convertFromStudentListToStudentDtoList(List<Student> students);

    public Student convertFromStudentSimpleDtoToStudent(StudentSimpleDTO studentSimpleDTO);

    public StudentSimpleDTO convertFromStudentToStudentSimpleDto(Student student);

    public List<Student> convertFromStudentSimpleDtoListToStudentList(List<StudentSimpleDTO> studentSimpleDTOList);

    public List<StudentSimpleDTO> convertFromStudentListToStudentSimpleDtoList(List<Student> students);

}
