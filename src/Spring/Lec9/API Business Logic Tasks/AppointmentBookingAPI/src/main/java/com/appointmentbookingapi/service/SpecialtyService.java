package com.appointmentbookingapi.service;

import com.appointmentbookingapi.dto.SpecialtyDTO;
import com.appointmentbookingapi.dto.SpecialtyRequestDTO;
import com.appointmentbookingapi.model.Specialty;

import java.util.List;

public interface SpecialtyService {

    List<SpecialtyDTO> getAllSpecialties();

    Specialty getSpecialtyEntityById(Long id);

    SpecialtyDTO getSpecialtyById(Long id);

    SpecialtyDTO createSpecialty(SpecialtyRequestDTO requestDTO);

    void deleteSpecialty(Long id);
}
