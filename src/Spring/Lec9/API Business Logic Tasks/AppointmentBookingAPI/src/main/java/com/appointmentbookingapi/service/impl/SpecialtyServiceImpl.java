package com.appointmentbookingapi.service.impl;

import com.appointmentbookingapi.dto.SpecialtyDTO;
import com.appointmentbookingapi.dto.SpecialtyRequestDTO;
import com.appointmentbookingapi.exception.BadRequestException;
import com.appointmentbookingapi.exception.NotFoundException;
import com.appointmentbookingapi.mapper.SpecialtyMapper;
import com.appointmentbookingapi.model.Specialty;
import com.appointmentbookingapi.repo.SpecialtyRepo;
import com.appointmentbookingapi.service.SpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepo specialtyRepo;
    private final SpecialtyMapper specialtyMapper;

    @Transactional(readOnly = true)
    @Override
    public List<SpecialtyDTO> getAllSpecialties() {
        // Read-only SELECT of every specialty.
        List<Specialty> specialties = specialtyRepo.findAll();

        // Empty table -> 404 with a meaningful message, never an empty string.
        if (specialties.isEmpty()) {
            throw new NotFoundException("No specialties found in the system");
        }

        return specialtyMapper.convertFromSpecialtyListToSpecialtyDtoList(specialties);
    }

    @Transactional(readOnly = true)
    @Override
    public Specialty getSpecialtyEntityById(Long id) {
        // Raw entity without mapping — used by other services (DoctorServiceImpl
        // resolving the specialty when creating a doctor).
        return findSpecialtyOrThrow(id);
    }

    @Transactional(readOnly = true)
    @Override
    public SpecialtyDTO getSpecialtyById(Long id) {
        // 404 is thrown by the helper if the row does not exist.
        return specialtyMapper.convertFromSpecialtyToSpecialtyDto(findSpecialtyOrThrow(id));
    }

    @Override
    public SpecialtyDTO createSpecialty(SpecialtyRequestDTO requestDTO) {
        // Field validation (@NotBlank) already ran via @Valid in the controller.

        // name is UNIQUE at the DB level -> catch it here and answer 400
        // instead of letting INSERT fail with a raw constraint violation (500).
        if (specialtyRepo.existsByName(requestDTO.getName())) {
            throw new BadRequestException(
                    "Specialty with name '" + requestDTO.getName() + "' already exists");
        }

        // Map request -> entity, INSERT, map persisted row -> response DTO.
        Specialty specialty = specialtyMapper.convertFromSpecialtyRequestDtoToSpecialty(requestDTO);

        return specialtyMapper.convertFromSpecialtyToSpecialtyDto(
                specialtyRepo.save(specialty)
        );
    }

    @Override
    public void deleteSpecialty(Long id) {
        // Fetch first so a wrong id results in 404 instead of a silent no-op DELETE.
        Specialty specialty = findSpecialtyOrThrow(id);
        specialtyRepo.delete(specialty);
    }

    // Shared lookup used by get/getEntity/delete to keep messages consistent.
    private Specialty findSpecialtyOrThrow(Long id) {
        return specialtyRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Specialty with id " + id + " not found"));
    }
}
