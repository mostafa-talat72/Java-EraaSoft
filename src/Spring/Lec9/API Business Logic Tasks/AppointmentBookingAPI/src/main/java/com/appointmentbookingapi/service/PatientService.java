package com.appointmentbookingapi.service;

import com.appointmentbookingapi.dto.PatientDTO;
import com.appointmentbookingapi.dto.PatientRequestDTO;
import com.appointmentbookingapi.model.Patient;

import java.util.List;

public interface PatientService {

    List<PatientDTO> getAllPatients();

    Patient getPatientEntityById(Long id);

    PatientDTO getPatientById(Long id);

    PatientDTO createPatient(PatientRequestDTO requestDTO);

    void deletePatient(Long id);
}
