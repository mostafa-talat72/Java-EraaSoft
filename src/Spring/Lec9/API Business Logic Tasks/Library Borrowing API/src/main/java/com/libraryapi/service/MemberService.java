package com.libraryapi.service;

import com.libraryapi.dto.MemberDTO;
import com.libraryapi.dto.MemberRequestDTO;
import com.libraryapi.model.Member;
import jakarta.validation.Valid;

import java.util.List;

public interface MemberService {

    MemberDTO createMember(@Valid MemberRequestDTO requestDTO);

    MemberDTO updateMember(Long id, @Valid MemberRequestDTO requestDTO);

    List<MemberDTO> getAllMembers();

    Member getMemberEntityById(Long id);

    MemberDTO getMemberById(Long id);

    void deleteMember(Long id);
}
