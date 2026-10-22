package com.appointmentbookingapi.service.impl;

import com.appointmentbookingapi.dto.MedicalServiceDTO;
import com.appointmentbookingapi.dto.MedicalServiceRequestDTO;
import com.appointmentbookingapi.exception.BadRequestException;
import com.appointmentbookingapi.exception.NotFoundException;
import com.appointmentbookingapi.mapper.MedicalServiceMapper;
import com.appointmentbookingapi.model.MedicalService;
import com.appointmentbookingapi.repo.MedicalServiceRepo;
import com.appointmentbookingapi.service.MedicalServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicalServiceServiceImpl implements MedicalServiceService {

    private final MedicalServiceRepo medicalServiceRepo;
    private final MedicalServiceMapper medicalServiceMapper;

    @Transactional(readOnly = true)
    @Override
    public List<MedicalServiceDTO> getAllMedicalServices() {
        // Read-only SELECT of every medical service.
        List<MedicalService> medicalServices = medicalServiceRepo.findAll();

        // Empty table -> 404 with a meaningful message, never an empty string.
        if (medicalServices.isEmpty()) {
            throw new NotFoundException("No medical services found in the system");
        }

        return medicalServiceMapper.convertFromMedicalServiceListToMedicalServiceDtoList(medicalServices);
    }

    @Transactional(readOnly = true)
    @Override
    public MedicalService getMedicalServiceEntityById(Long id) {
        // Raw entity without mapping — used by other services (AppointmentServiceImpl
        // resolving the service and its durationMinutes when booking).
        return findMedicalServiceOrThrow(id);
    }

    @Transactional(readOnly = true)
    @Override
    public MedicalServiceDTO getMedicalServiceById(Long id) {
        // 404 is thrown by the helper if the row does not exist.
        return medicalServiceMapper.convertFromMedicalServiceToMedicalServiceDto(
                findMedicalServiceOrThrow(id));
    }

    @Transactional(readOnly = true)
    @Override
    public MedicalService getMedicalServiceEntityByName(String name) {
        // Case-sensitive name lookup — the return type keeps durationMinutes,
        // which the booking flow needs to compute the appointment endTime (rule 3).
        return medicalServiceRepo.findByName(name)
                .orElseThrow(() -> new NotFoundException(
                        "Medical service with name '" + name + "' not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public MedicalServiceDTO getMedicalServiceByName(String name) {
        // Same lookup as the entity variant, returned as a response DTO.
        return medicalServiceMapper.convertFromMedicalServiceToMedicalServiceDto(
                getMedicalServiceEntityByName(name));
    }

    @Override
    public MedicalServiceDTO createMedicalService(MedicalServiceRequestDTO requestDTO) {
        // Field validation (name, @Positive durationMinutes) already ran via @Valid.

        // name is UNIQUE at the DB level -> catch it here and answer 400
        // instead of letting INSERT fail with a raw constraint violation (500).
        if (medicalServiceRepo.existsByName(requestDTO.getName())) {
            throw new BadRequestException(
                    "Medical service with name '" + requestDTO.getName() + "' already exists");
        }

        // Map request -> entity, INSERT, map persisted row -> response DTO.
        MedicalService medicalService =
                medicalServiceMapper.convertFromMedicalServiceRequestDtoToMedicalService(requestDTO);

        return medicalServiceMapper.convertFromMedicalServiceToMedicalServiceDto(
                medicalServiceRepo.save(medicalService));
    }

    @Override
    public void deleteMedicalService(Long id) {
        // Fetch first so a wrong id results in 404 instead of a silent no-op DELETE.
        MedicalService medicalService = findMedicalServiceOrThrow(id);
        medicalServiceRepo.delete(medicalService);
    }

    // Shared lookup used by get/getEntity/delete to keep messages consistent.
    private MedicalService findMedicalServiceOrThrow(Long id) {
        return medicalServiceRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Medical service with id " + id + " not found"));
    }
}
