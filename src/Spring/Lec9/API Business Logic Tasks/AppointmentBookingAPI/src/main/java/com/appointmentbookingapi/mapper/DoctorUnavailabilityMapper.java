package com.appointmentbookingapi.mapper;

import com.appointmentbookingapi.dto.DoctorUnavailabilityDTO;
import com.appointmentbookingapi.dto.DoctorUnavailabilityRequestDTO;
import com.appointmentbookingapi.model.DoctorUnavailability;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = DoctorMapper.class)
public interface DoctorUnavailabilityMapper {

    DoctorUnavailabilityDTO convertFromDoctorUnavailabilityToDoctorUnavailabilityDto(DoctorUnavailability doctorUnavailability);

    // doctorId has no matching entity field on purpose — the Doctor is resolved
    // by id in the service layer, so the relation stays null after mapping.
    @Mapping(target = "doctor", ignore = true)
    DoctorUnavailability convertFromDoctorUnavailabilityRequestDtoToDoctorUnavailability(DoctorUnavailabilityRequestDTO requestDTO);

    List<DoctorUnavailabilityDTO> convertFromDoctorUnavailabilityListToDoctorUnavailabilityDtoList(List<DoctorUnavailability> doctorUnavailabilities);
}
