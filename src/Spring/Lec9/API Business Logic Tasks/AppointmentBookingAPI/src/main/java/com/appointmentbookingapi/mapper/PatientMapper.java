package com.appointmentbookingapi.mapper;

import com.appointmentbookingapi.dto.PatientDTO;
import com.appointmentbookingapi.dto.PatientRequestDTO;
import com.appointmentbookingapi.dto.PatientSimpleDTO;
import com.appointmentbookingapi.model.Patient;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientDTO convertFromPatientToPatientDto(Patient patient);

    PatientSimpleDTO convertFromPatientToPatientSimpleDto(Patient patient);

    Patient convertFromPatientRequestDtoToPatient(PatientRequestDTO requestDTO);

    Patient convertFromPatientDtoToPatient(PatientDTO patientDto);

    Patient convertFromPatientSimpleDtoToPatient(PatientSimpleDTO patientSimpleDto);

    List<Patient> convertFromPatientDtoListToPatientList(List<PatientDTO> patientDtoList);

    List<PatientDTO> convertFromPatientListToPatientDtoList(List<Patient> patients);

    List<PatientSimpleDTO> convertFromPatientListToPatientSimpleDtoList(List<Patient> patients);
}
