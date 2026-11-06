package com.libraryapi.service.impl;

import com.libraryapi.dto.BorrowStatusDTO;
import com.libraryapi.dto.BorrowStatusRequestDTO;
import com.libraryapi.exception.BadRequestException;
import com.libraryapi.exception.NotFoundException;
import com.libraryapi.mapper.BorrowStatusMapper;
import com.libraryapi.model.BorrowStatus;
import com.libraryapi.repo.BorrowRepo;
import com.libraryapi.repo.BorrowStatusRepo;
import com.libraryapi.service.BorrowStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class BorrowStatusServiceImpl implements BorrowStatusService {

    private final BorrowStatusRepo borrowStatusRepo;

    // Only used to refuse deleting a status that is still assigned to borrows.
    private final BorrowRepo borrowRepo;

    private final BorrowStatusMapper borrowStatusMapper;

    @Transactional(readOnly = true)
    @Override
    public List<BorrowStatusDTO> getAllBorrowStatuses() {
        List<BorrowStatus> borrowStatuses = borrowStatusRepo.findAllByOrderByIdAsc();

        // Same convention as the other list endpoints: empty is reported as 404.
        if (borrowStatuses.isEmpty()) {
            throw new NotFoundException("No borrow statuses found in the system");
        }

        return borrowStatusMapper.convertFromBorrowStatusListToBorrowStatusDtoList(borrowStatuses);
    }

    @Transactional(readOnly = true)
    @Override
    public BorrowStatusDTO getBorrowStatusById(Long id) {
        return borrowStatusMapper.convertFromBorrowStatusToBorrowStatusDto(findBorrowStatusOrThrow(id));
    }

    @Transactional(readOnly = true)
    @Override
    public BorrowStatus getBorrowStatusEntityById(Long id) {
        return findBorrowStatusOrThrow(id);
    }

    @Transactional(readOnly = true)
    @Override
    public BorrowStatus getBorrowStatusEntityByName(String name) {
        return borrowStatusRepo.findByName(name)
                .orElseThrow(() -> new NotFoundException("Borrow status '" + name + "' not found"));
    }

    @Override
    public BorrowStatusDTO createBorrowStatus(BorrowStatusRequestDTO requestDTO) {
        // Clear 400 instead of a raw unique-constraint 500.
        if (borrowStatusRepo.existsByName(requestDTO.getName())) {
            throw new BadRequestException(
                    "Borrow status with name '" + requestDTO.getName() + "' already exists");
        }

        BorrowStatus borrowStatus =
                borrowStatusMapper.convertFromBorrowStatusRequestDtoToBorrowStatus(requestDTO);

        BorrowStatus saved = borrowStatusRepo.save(borrowStatus);

        return borrowStatusMapper.convertFromBorrowStatusToBorrowStatusDto(saved);
    }

    @Override
    public BorrowStatusDTO updateBorrowStatus(Long id, BorrowStatusRequestDTO requestDTO) {
        BorrowStatus borrowStatus = findBorrowStatusOrThrow(id);

        // The new name must not clash with another row (this row excluded by id).
        if (borrowStatusRepo.existsByNameAndIdNot(requestDTO.getName(), id)) {
            throw new BadRequestException(
                    "Borrow status with name '" + requestDTO.getName() + "' already exists");
        }

        // In-place update; the entity is managed, so dirty checking flushes at commit.
        borrowStatusMapper.updateBorrowStatusFromBorrowStatusRequestDto(requestDTO, borrowStatus);

        return borrowStatusMapper.convertFromBorrowStatusToBorrowStatusDto(
                borrowStatusRepo.save(borrowStatus)
        );
    }

    @Override
    public void deleteBorrowStatus(Long id) {
        BorrowStatus borrowStatus = findBorrowStatusOrThrow(id);

        // Refuse while borrow rows still point at this status, otherwise the FK
        // constraint fails and the client gets a 500.
        if (borrowRepo.existsByStatusId(id)) {
            throw new BadRequestException(
                    "Borrow status with id " + id + " is used by existing borrows and cannot be deleted");
        }

        borrowStatusRepo.delete(borrowStatus);
    }

    // Shared helper for safe entity retrieval with consistent 404 messaging.
    private BorrowStatus findBorrowStatusOrThrow(Long id) {
        return borrowStatusRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Borrow status with id " + id + " not found"));
    }
}
