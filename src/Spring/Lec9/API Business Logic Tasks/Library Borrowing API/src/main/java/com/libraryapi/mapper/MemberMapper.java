package com.libraryapi.mapper;

import com.libraryapi.dto.MemberDTO;
import com.libraryapi.dto.MemberRequestDTO;
import com.libraryapi.dto.MemberSimpleDTO;
import com.libraryapi.model.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

// MemberTypeMapper provides the nested MemberTypeDTO in both member views.
@Mapper(componentModel = "spring", uses = MemberTypeMapper.class)
public interface MemberMapper {

    MemberDTO convertFromMemberToMemberDto(Member member);

    MemberSimpleDTO convertFromMemberToMemberSimpleDto(Member member);

    List<MemberDTO> convertFromMemberListToMemberDtoList(List<Member> members);

    // type is resolved by the service from requestDTO.typeId, so the mapper
    // must not try to build a MemberType from it.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", ignore = true)
    Member convertFromMemberRequestDtoToMember(MemberRequestDTO requestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", ignore = true)
    void updateMemberFromMemberRequestDto(MemberRequestDTO requestDTO, @MappingTarget Member member);
}
