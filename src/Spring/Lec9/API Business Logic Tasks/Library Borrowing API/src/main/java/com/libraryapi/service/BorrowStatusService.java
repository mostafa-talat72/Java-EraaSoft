package com.libraryapi.service;

import com.libraryapi.dto.BorrowStatusDTO;
import com.libraryapi.dto.BorrowStatusRequestDTO;
import com.libraryapi.model.BorrowStatus;

import java.util.List;

public interface BorrowStatusService {

    List<BorrowStatusDTO> getAllBorrowStatuses();

    BorrowStatusDTO getBorrowStatusById(Long id);

    // Raw entity for internal callers (e.g. BorrowServiceImpl).
    BorrowStatus getBorrowStatusEntityById(Long id);

    // Lookup by the seeded name (BORROWED / OVERDUE / RETURNED).
    BorrowStatus getBorrowStatusEntityByName(String name);

    BorrowStatusDTO createBorrowStatus(BorrowStatusRequestDTO requestDTO);

    BorrowStatusDTO updateBorrowStatus(Long id, BorrowStatusRequestDTO requestDTO);

    void deleteBorrowStatus(Long id);
}
