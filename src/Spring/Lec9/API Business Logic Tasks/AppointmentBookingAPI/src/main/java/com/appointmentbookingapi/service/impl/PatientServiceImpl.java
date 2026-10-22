package com.appointmentbookingapi.service.impl;

import com.appointmentbookingapi.dto.PatientDTO;
import com.appointmentbookingapi.dto.PatientRequestDTO;
import com.appointmentbookingapi.exception.BadRequestException;
import com.appointmentbookingapi.exception.NotFoundException;
import com.appointmentbookingapi.mapper.PatientMapper;
import com.appointmentbookingapi.model.Patient;
import com.appointmentbookingapi.repo.PatientRepo;
import com.appointmentbookingapi.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepo patientRepo;
    private final PatientMapper patientMapper;

    @Transactional(readOnly = true)
    @Override
    public List<PatientDTO> getAllPatients() {
        // Read-only SELECT of every patient.
        List<Patient> patients = patientRepo.findAll();

        // Empty table -> 404 with a meaningful message, never an empty string.
        if (patients.isEmpty()) {
            throw new NotFoundException("No patients found in the system");
        }

        return patientMapper.convertFromPatientListToPatientDtoList(patients);
    }

    @Transactional(readOnly = true)
    @Override
    public Patient getPatientEntityById(Long id) {
        // Raw entity without mapping — used by other services (AppointmentServiceImpl
        // resolving the patient when booking).
        return findPatientOrThrow(id);
    }

    @Transactional(readOnly = true)
    @Override
    public PatientDTO getPatientById(Long id) {
        // 404 is thrown by the helper if the row does not exist.
        return patientMapper.convertFromPatientToPatientDto(findPatientOrThrow(id));
    }

    @Override
    public PatientDTO createPatient(PatientRequestDTO requestDTO) {
        // Field validation (name, @Email) already ran via @Valid in the controller.

        // email is UNIQUE at the DB level -> catch it here and answer 400
        // instead of letting INSERT fail with a raw constraint violation (500).
        if (patientRepo.existsByEmail(requestDTO.getEmail())) {
            throw new BadRequestException(
                    "Patient with email '" + requestDTO.getEmail() + "' already exists");
        }

        // Map request -> entity, INSERT, map persisted row -> response DTO.
        Patient patient = patientMapper.convertFromPatientRequestDtoToPatient(requestDTO);

        return patientMapper.convertFromPatientToPatientDto(patientRepo.save(patient));
    }

    @Override
    public void deletePatient(Long id) {
        // Fetch first so a wrong id results in 404 instead of a silent no-op DELETE.
        Patient patient = findPatientOrThrow(id);
        patientRepo.delete(patient);
    }

    // Shared lookup used by get/getEntity/delete to keep messages consistent.
    private Patient findPatientOrThrow(Long id) {
        return patientRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient with id " + id + " not found"));
    }
}
