package com.libraryapi.service.impl;

import com.libraryapi.dto.MemberDTO;
import com.libraryapi.dto.MemberRequestDTO;
import com.libraryapi.exception.BadRequestException;
import com.libraryapi.exception.NotFoundException;
import com.libraryapi.mapper.MemberMapper;
import com.libraryapi.model.Member;
import com.libraryapi.repo.BorrowRepo;
import com.libraryapi.repo.MemberRepo;
import com.libraryapi.service.MemberService;
import com.libraryapi.service.MemberTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepo memberRepo;

    // Only used to refuse deleting a member that appears in LB_BORROW.
    private final BorrowRepo borrowRepo;

    private final MemberMapper memberMapper;

    // Resolves requestDTO.typeId to the row that carries loanDays and the cap.
    private final MemberTypeService memberTypeService;

    @Transactional(readOnly = true)
    @Override
    public List<MemberDTO> getAllMembers() {
        List<Member> members = memberRepo.findAllByOrderByIdAsc();

        // Same convention as the previous task: an empty list is reported as 404.
        if (members.isEmpty()) {
            throw new NotFoundException("No members found in the system");
        }

        return memberMapper.convertFromMemberListToMemberDtoList(members);
    }

    @Override
    public Member getMemberEntityById(Long id) {
        return findMemberOrThrow(id);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberDTO getMemberById(Long id) {
        return memberMapper.convertFromMemberToMemberDto(findMemberOrThrow(id));
    }

    @Override
    public MemberDTO createMember(MemberRequestDTO requestDTO) {
        // Pre-check the unique business key so a duplicate becomes a clear 400
        // instead of a raw DB unique-constraint 500.
        if (memberRepo.existsByEmail(requestDTO.getEmail())) {
            throw new BadRequestException("Member with email '" + requestDTO.getEmail() + "' already exists");
        }

        Member member = memberMapper.convertFromMemberRequestDtoToMember(requestDTO);

        // The mapper ignores type; the service resolves it from typeId (404 if missing).
        member.setType(memberTypeService.getMemberTypeEntityById(requestDTO.getTypeId()));

        return memberMapper.convertFromMemberToMemberDto(memberRepo.save(member));
    }

    @Override
    public MemberDTO updateMember(Long id, MemberRequestDTO requestDTO) {
        // Managed entity: dirty checking flushes the new name/email/type at commit.
        Member member = findMemberOrThrow(id);

        if (memberRepo.existsByEmailAndIdNot(requestDTO.getEmail(), id)) {
            throw new BadRequestException("Member with email '" + requestDTO.getEmail() + "' already exists");
        }

        memberMapper.updateMemberFromMemberRequestDto(requestDTO, member);

        // The mapper ignores type; the service resolves it from typeId (404 if missing).
        member.setType(memberTypeService.getMemberTypeEntityById(requestDTO.getTypeId()));

        return memberMapper.convertFromMemberToMemberDto(member);
    }

    @Override
    public void deleteMember(Long id) {
        Member member = findMemberOrThrow(id);

        // Refuse the delete while borrow rows still reference this member,
        // otherwise the FK constraint fails and the client gets a 500.
        if (borrowRepo.existsByMemberId(id)) {
            throw new BadRequestException(
                    "Member with id " + id + " has borrow history and cannot be deleted");
        }

        memberRepo.delete(member);
    }

    // Shared helper for safe entity retrieval with consistent 404 messaging.
    private Member findMemberOrThrow(Long id) {
        return memberRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Member with id " + id + " not found"));
    }
}
