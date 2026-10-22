package com.appointmentbookingapi.mapper;

import com.appointmentbookingapi.dto.MedicalServiceDTO;
import com.appointmentbookingapi.dto.MedicalServiceRequestDTO;
import com.appointmentbookingapi.dto.MedicalServiceSimpleDTO;
import com.appointmentbookingapi.model.MedicalService;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicalServiceMapper {

    MedicalServiceDTO convertFromMedicalServiceToMedicalServiceDto(MedicalService medicalService);

    MedicalServiceSimpleDTO convertFromMedicalServiceToMedicalServiceSimpleDto(MedicalService medicalService);

    MedicalService convertFromMedicalServiceRequestDtoToMedicalService(MedicalServiceRequestDTO requestDTO);

    MedicalService convertFromMedicalServiceDtoToMedicalService(MedicalServiceDTO medicalServiceDto);

    MedicalService convertFromMedicalServiceSimpleDtoToMedicalService(MedicalServiceSimpleDTO medicalServiceSimpleDto);

    List<MedicalService> convertFromMedicalServiceDtoListToMedicalServiceList(List<MedicalServiceDTO> medicalServiceDtoList);

    List<MedicalServiceDTO> convertFromMedicalServiceListToMedicalServiceDtoList(List<MedicalService> medicalServices);

    List<MedicalServiceSimpleDTO> convertFromMedicalServiceListToMedicalServiceSimpleDtoList(List<MedicalService> medicalServices);
}
