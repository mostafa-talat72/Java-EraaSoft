package com.appointmentbookingapi.service;

import com.appointmentbookingapi.dto.MedicalServiceDTO;
import com.appointmentbookingapi.dto.MedicalServiceRequestDTO;
import com.appointmentbookingapi.model.MedicalService;

import java.util.List;

public interface MedicalServiceService {

    List<MedicalServiceDTO> getAllMedicalServices();

    MedicalService getMedicalServiceEntityById(Long id);

    MedicalServiceDTO getMedicalServiceById(Long id);

    MedicalService getMedicalServiceEntityByName(String name);

    MedicalServiceDTO getMedicalServiceByName(String name);

    MedicalServiceDTO createMedicalService(MedicalServiceRequestDTO requestDTO);

    void deleteMedicalService(Long id);
}
