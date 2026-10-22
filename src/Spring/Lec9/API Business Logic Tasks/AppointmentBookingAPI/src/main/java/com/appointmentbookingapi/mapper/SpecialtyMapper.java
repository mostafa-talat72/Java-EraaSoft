package com.appointmentbookingapi.mapper;

import com.appointmentbookingapi.dto.SpecialtyDTO;
import com.appointmentbookingapi.dto.SpecialtyRequestDTO;
import com.appointmentbookingapi.dto.SpecialtySimpleDTO;
import com.appointmentbookingapi.model.Specialty;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpecialtyMapper {

    SpecialtyDTO convertFromSpecialtyToSpecialtyDto(Specialty specialty);

    SpecialtySimpleDTO convertFromSpecialtyToSpecialtySimpleDto(Specialty specialty);

    Specialty convertFromSpecialtyRequestDtoToSpecialty(SpecialtyRequestDTO requestDTO);

    Specialty convertFromSpecialtyDtoToSpecialty(SpecialtyDTO specialtyDto);

    Specialty convertFromSpecialtySimpleDtoToSpecialty(SpecialtySimpleDTO specialtySimpleDto);

    List<Specialty> convertFromSpecialtyDtoListToSpecialtyList(List<SpecialtyDTO> specialtyDtoList);

    List<SpecialtyDTO> convertFromSpecialtyListToSpecialtyDtoList(List<Specialty> specialties);

    List<SpecialtySimpleDTO> convertFromSpecialtyListToSpecialtySimpleDtoList(List<Specialty> specialties);
}
