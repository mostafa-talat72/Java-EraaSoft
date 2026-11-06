package com.libraryapi.service;

import com.libraryapi.dto.MemberTypeDTO;
import com.libraryapi.dto.MemberTypeRequestDTO;
import com.libraryapi.model.MemberType;

import java.util.List;

public interface MemberTypeService {

    List<MemberTypeDTO> getAllMemberTypes();

    MemberTypeDTO getMemberTypeById(Long id);

    // Raw entity for internal callers (e.g. MemberServiceImpl).
    MemberType getMemberTypeEntityById(Long id);

    MemberTypeDTO createMemberType(MemberTypeRequestDTO requestDTO);

    MemberTypeDTO updateMemberType(Long id, MemberTypeRequestDTO requestDTO);

    void deleteMemberType(Long id);
}
