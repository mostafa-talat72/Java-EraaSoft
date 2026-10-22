package com.appointmentbookingapi.service.impl;

import com.appointmentbookingapi.dto.DoctorDTO;
import com.appointmentbookingapi.dto.DoctorRequestDTO;
import com.appointmentbookingapi.exception.NotFoundException;
import com.appointmentbookingapi.mapper.DoctorMapper;
import com.appointmentbookingapi.model.Doctor;
import com.appointmentbookingapi.model.Specialty;
import com.appointmentbookingapi.repo.DoctorRepo;
import com.appointmentbookingapi.service.DoctorService;
import com.appointmentbookingapi.service.SpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorServiceImpl implements DoctorService {

    // Fields MUST be final: @RequiredArgsConstructor builds the injection
    // constructor only for final fields -> null deps otherwise (NPE at runtime).
    private final DoctorRepo doctorRepo;
    private final SpecialtyService specialtyService;
    private final DoctorMapper doctorMapper;

    @Transactional(readOnly = true)
    @Override
    public List<DoctorDTO> getAllDoctors() {
        // Read-only SELECT of every doctor, nested specialty mapped via SpecialtyMapper.
        List<Doctor> doctors = doctorRepo.findAll();

        // Empty table -> 404 "no doctors at all", not an empty DTO list.
        if (doctors.isEmpty()) {
            throw new NotFoundException("No doctors found in the system");
        }

        return doctorMapper.convertFromDoctorListToDoctorDtoList(doctors);
    }

    @Transactional(readOnly = true)
    @Override
    public Doctor getDoctorEntityById(Long id) {
        // Raw entity without mapping — used by other services that need the relation
        // (e.g. AppointmentServiceImpl resolving the doctor on booking).
        return findDoctorOrThrow(id);
    }

    @Transactional(readOnly = true)
    @Override
    public DoctorDTO getDoctorById(Long id) {
        // 404 is thrown by the helper if the row does not exist.
        return doctorMapper.convertFromDoctorToDoctorDto(findDoctorOrThrow(id));
    }

    @Override
    public DoctorDTO createDoctor(DoctorRequestDTO requestDTO) {
        // Field validation (name, specialtyId) already ran via @Valid in the controller.

        // The request carries only specialtyId -> resolve the real Specialty entity,
        // 404 if the specialty does not exist.
        Specialty specialty = specialtyService.getSpecialtyEntityById(requestDTO.getSpecialtyId());

        // Mapper fills name only (specialty is intentionally ignored) -> attach it here.
        Doctor doctor = doctorMapper.convertFromDoctorRequestDtoToDoctor(requestDTO);
        doctor.setSpecialty(specialty);

        // INSERT + map the persisted row back to the response DTO.
        return doctorMapper.convertFromDoctorToDoctorDto(doctorRepo.save(doctor));
    }

    @Override
    public void deleteDoctor(Long id) {
        // Fetch first so a wrong id results in 404 instead of a silent no-op DELETE.
        Doctor doctor = findDoctorOrThrow(id);
        doctorRepo.delete(doctor);
    }

    // Shared lookup used by both get and delete to keep messages consistent.
    private Doctor findDoctorOrThrow(Long id) {
        return doctorRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Doctor with id " + id + " not found"));
    }
}
