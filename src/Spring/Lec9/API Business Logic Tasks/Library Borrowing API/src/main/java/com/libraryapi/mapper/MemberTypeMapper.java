package com.libraryapi.mapper;

import com.libraryapi.dto.MemberTypeDTO;
import com.libraryapi.dto.MemberTypeRequestDTO;
import com.libraryapi.model.MemberType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberTypeMapper {

    MemberTypeDTO convertFromMemberTypeToMemberTypeDto(MemberType memberType);

    List<MemberTypeDTO> convertFromMemberTypeListToMemberTypeDtoList(List<MemberType> memberTypes);

    // id comes from the sequence, never from the request.
    @Mapping(target = "id", ignore = true)
    MemberType convertFromMemberTypeRequestDtoToMemberType(MemberTypeRequestDTO requestDTO);

    // In-place update; the entity is managed, so dirty checking flushes it.
    @Mapping(target = "id", ignore = true)
    void updateMemberTypeFromMemberTypeRequestDto(MemberTypeRequestDTO requestDTO,
                                                  @MappingTarget MemberType memberType);
}
