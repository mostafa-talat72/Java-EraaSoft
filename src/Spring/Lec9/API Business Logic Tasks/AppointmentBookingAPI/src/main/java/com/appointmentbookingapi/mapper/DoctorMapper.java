package com.appointmentbookingapi.mapper;

import com.appointmentbookingapi.dto.DoctorDTO;
import com.appointmentbookingapi.dto.DoctorRequestDTO;
import com.appointmentbookingapi.dto.DoctorSimpleDTO;
import com.appointmentbookingapi.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    DoctorDTO convertFromDoctorToDoctorDto(Doctor doctor);

    DoctorSimpleDTO convertFromDoctorToDoctorSimpleDto(Doctor doctor);
    
    Doctor convertFromDoctorRequestDtoToDoctor(DoctorRequestDTO requestDTO);

    // Full round-trip: nested specialty comes as SpecialtySimpleDTO via SpecialtyMapper.
    Doctor convertFromDoctorDtoToDoctor(DoctorDTO doctorDto);

    Doctor convertFromDoctorSimpleDtoToDoctor(DoctorSimpleDTO doctorSimpleDto);

    List<Doctor> convertFromDoctorDtoListToDoctorList(List<DoctorDTO> doctorDtoList);

    List<DoctorDTO> convertFromDoctorListToDoctorDtoList(List<Doctor> doctors);

    List<DoctorSimpleDTO> convertFromDoctorListToDoctorSimpleDtoList(List<Doctor> doctors);
}
