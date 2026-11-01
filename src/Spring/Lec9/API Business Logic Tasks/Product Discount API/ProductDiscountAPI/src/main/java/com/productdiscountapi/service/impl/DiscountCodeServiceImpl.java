package com.productdiscountapi.service.impl;

import com.productdiscountapi.dto.DiscountCodeDTO;
import com.productdiscountapi.dto.DiscountCodeRequestDTO;
import com.productdiscountapi.exception.BadRequestException;
import com.productdiscountapi.exception.NotFoundException;
import com.productdiscountapi.mapper.DiscountCodeMapper;
import com.productdiscountapi.model.DiscountCode;
import com.productdiscountapi.repo.DiscountCodeRepo;
import com.productdiscountapi.service.DiscountCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DiscountCodeServiceImpl implements DiscountCodeService {

    private final DiscountCodeRepo discountCodeRepo;
    private final DiscountCodeMapper discountCodeMapper;

    @Transactional(readOnly = true)
    @Override
    public List<DiscountCodeDTO> getAllDiscountCodes() {
        // Read-only query to fetch all available discount codes.
        List<DiscountCode> discountCodes = discountCodeRepo.findAll();

        if (discountCodes.isEmpty()) {
            throw new NotFoundException("No discount codes found in the system");
        }

        return discountCodeMapper.convertFromDiscountCodeListToDiscountCodeDtoList(discountCodes);
    }

    @Transactional(readOnly = true)
    @Override
    public DiscountCode getDiscountCodeEntityById(Long id) {
        // Raw entity lookup for internal service validation (e.g. calculation engine).
        return findDiscountCodeOrThrow(id);
    }

    @Transactional(readOnly = true)
    @Override
    public DiscountCode getDiscountCodeEntityByCode(String code) {
        // Retrieve entity by its unique string code, throwing 404 if not found.
        return discountCodeRepo.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Discount code with code '" + code + "' not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public DiscountCodeDTO getDiscountCodeById(Long id) {
        // Fetch entity by ID and map to response DTO.
        return discountCodeMapper.convertFromDiscountCodeToDiscountCodeDto(findDiscountCodeOrThrow(id));
    }

    @Transactional(readOnly = true)
    @Override
    public DiscountCodeDTO getDiscountCodeByCode(String code) {
        // Fetch entity by code string and map to response DTO.
        return discountCodeMapper.convertFromDiscountCodeToDiscountCodeDto(getDiscountCodeEntityByCode(code));
    }

    @Override
    public DiscountCodeDTO createDiscountCode(DiscountCodeRequestDTO requestDTO) {
        // Ensure discount code string is unique to prevent duplicate entries.
        if (discountCodeRepo.existsByCode(requestDTO.getCode())) {
            throw new BadRequestException("Discount Code with code '" + requestDTO.getCode() + "' already exists");
        }

        DiscountCode discountCode = discountCodeMapper.convertFromDiscountCodeRequestDtoToDiscountCode(requestDTO);

        // Resolve and validate incompatible codes if specified (Rule 6).
        if (requestDTO.getIncompatibleCodeIds() != null && !requestDTO.getIncompatibleCodeIds().isEmpty()) {
            discountCode.setIncompatibleCodes(resolveIncompatibleCodes(requestDTO.getIncompatibleCodeIds()));
        }

        return discountCodeMapper.convertFromDiscountCodeToDiscountCodeDto(discountCodeRepo.save(discountCode));
    }

    @Override
    public DiscountCodeDTO updateDiscountCode(Long id, DiscountCodeRequestDTO requestDTO) {
        // Fetch existing discount code or throw 404.
        DiscountCode discountCode = findDiscountCodeOrThrow(id);

        // Ensure updated code string does not conflict with another existing code.
        if (discountCodeRepo.existsByCodeAndIdNot(requestDTO.getCode(), discountCode.getId())) {
            throw new BadRequestException("Discount Code with code '" + requestDTO.getCode() + "' already exists");
        }

        // In-place update using MapStruct target mapping.
        discountCodeMapper.updateDiscountCodeFromDiscountCodeRequestDto(requestDTO, discountCode);

        // Update incompatible codes relations if provided.
        if (requestDTO.getIncompatibleCodeIds() != null && !requestDTO.getIncompatibleCodeIds().isEmpty()) {
            discountCode.setIncompatibleCodes(resolveIncompatibleCodes(requestDTO.getIncompatibleCodeIds()));
        }

        return discountCodeMapper.convertFromDiscountCodeToDiscountCodeDto(discountCodeRepo.save(discountCode));
    }

    @Override
    public void deleteDiscountCode(Long id) {
        // Ensure discount code exists before issuing a delete statement.
        DiscountCode discountCode = findDiscountCodeOrThrow(id);
        discountCodeRepo.delete(discountCode);
    }

    // Shared helper for safe entity retrieval with consistent 404 messaging.
    private DiscountCode findDiscountCodeOrThrow(Long id) {
        return discountCodeRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Discount code with id " + id + " not found"));
    }

    /**
     * Resolves, deduplicates, and validates a list of incompatible discount code IDs.
     * Throws NotFoundException listing any IDs that do not exist in the database.
     */
    private List<DiscountCode> resolveIncompatibleCodes(List<Long> incompatibleCodeIds) {
        // 1. Remove any duplicate IDs sent in the request payload using a Set.
        Set<Long> requestedIds = new HashSet<>(incompatibleCodeIds);

        // 2. Perform a single bulk query to fetch all matching codes.
        List<DiscountCode> foundCodes = discountCodeRepo.findAllById(requestedIds);

        // 3. Fast Path: If sizes match, all requested IDs successfully exist in the database.
        if (foundCodes.size() == requestedIds.size()) {
            return foundCodes;
        }

        // 4. Slow Path: Calculate exactly which IDs are missing to report them in the exception message.
        Set<Long> foundIds = foundCodes.stream()
                .map(DiscountCode::getId)
                .collect(Collectors.toSet());

        List<Long> missingIds = requestedIds.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        // 5. Fail fast with explicit 404 containing the missing IDs.
        throw new NotFoundException("Discount codes with ids " + missingIds + " not found");
    }
}
