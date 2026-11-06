package com.libraryapi.service.impl;

import com.libraryapi.dto.MemberTypeDTO;
import com.libraryapi.dto.MemberTypeRequestDTO;
import com.libraryapi.exception.BadRequestException;
import com.libraryapi.exception.NotFoundException;
import com.libraryapi.mapper.MemberTypeMapper;
import com.libraryapi.model.MemberType;
import com.libraryapi.repo.MemberRepo;
import com.libraryapi.repo.MemberTypeRepo;
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
public class MemberTypeServiceImpl implements MemberTypeService {

    private final MemberTypeRepo memberTypeRepo;

    // Only used to refuse deleting a type that is still assigned to members.
    private final MemberRepo memberRepo;

    private final MemberTypeMapper memberTypeMapper;

    @Transactional(readOnly = true)
    @Override
    public List<MemberTypeDTO> getAllMemberTypes() {
        List<MemberType> memberTypes = memberTypeRepo.findAllByOrderByIdAsc();

        // Same convention as the other list endpoints: empty is reported as 404.
        if (memberTypes.isEmpty()) {
            throw new NotFoundException("No member types found in the system");
        }

        return memberTypeMapper.convertFromMemberTypeListToMemberTypeDtoList(memberTypes);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberTypeDTO getMemberTypeById(Long id) {
        return memberTypeMapper.convertFromMemberTypeToMemberTypeDto(findMemberTypeOrThrow(id));
    }

    @Transactional(readOnly = true)
    @Override
    public MemberType getMemberTypeEntityById(Long id) {
        return findMemberTypeOrThrow(id);
    }

    @Override
    public MemberTypeDTO createMemberType(MemberTypeRequestDTO requestDTO) {
        // Clear 400 instead of a raw unique-constraint 500.
        if (memberTypeRepo.existsByName(requestDTO.getName())) {
            throw new BadRequestException(
                    "Member type with name '" + requestDTO.getName() + "' already exists");
        }

        MemberType memberType =
                memberTypeMapper.convertFromMemberTypeRequestDtoToMemberType(requestDTO);

        MemberType saved = memberTypeRepo.save(memberType);

        return memberTypeMapper.convertFromMemberTypeToMemberTypeDto(saved);
    }

    @Override
    public MemberTypeDTO updateMemberType(Long id, MemberTypeRequestDTO requestDTO) {
        MemberType memberType = findMemberTypeOrThrow(id);

        // The new name must not clash with another row (this row excluded by id).
        if (memberTypeRepo.existsByNameAndIdNot(requestDTO.getName(), id)) {
            throw new BadRequestException(
                    "Member type with name '" + requestDTO.getName() + "' already exists");
        }

        // In-place update; the entity is managed, so dirty checking flushes at commit.
        memberTypeMapper.updateMemberTypeFromMemberTypeRequestDto(requestDTO, memberType);

        return memberTypeMapper.convertFromMemberTypeToMemberTypeDto(
                memberTypeRepo.save(memberType)
        );
    }

    @Override
    public void deleteMemberType(Long id) {
        MemberType memberType = findMemberTypeOrThrow(id);

        // Refuse while members still point at this type, otherwise the FK
        // constraint fails and the client gets a 500.
        if (memberRepo.existsByTypeId(id)) {
            throw new BadRequestException(
                    "Member type with id " + id + " is used by existing members and cannot be deleted");
        }

        memberTypeRepo.delete(memberType);
    }

    // Shared helper for safe entity retrieval with consistent 404 messaging.
    private MemberType findMemberTypeOrThrow(Long id) {
        return memberTypeRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Member type with id " + id + " not found"));
    }
}
