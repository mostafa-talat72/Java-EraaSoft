package com.libraryapi.service;

import com.libraryapi.dto.BorrowDTO;
import com.libraryapi.dto.BorrowRequestDTO;

import java.util.List;

public interface BorrowService {

    // POST /borrows - rules 1..4 and the due-date calculation.
    BorrowDTO borrow(BorrowRequestDTO requestDTO);

    // PUT /borrows/{id}/return - rules 5 and 6.
    BorrowDTO returnBook(Long id);

    // GET /borrows - newest first; empty is reported as 404.
    List<BorrowDTO> getAllBorrows();

    // GET /borrows/overdue - still out and already past the due date.
    List<BorrowDTO> getOverdueBorrows();

    // GET /borrows/{id}
    BorrowDTO getBorrowById(Long id);

    // GET /borrows/member/{memberId} - history, newest first.
    List<BorrowDTO> getBorrowsByMember(Long memberId);

    // GET /borrows/book/{bookId} - history, newest first.
    List<BorrowDTO> getBorrowsByBook(Long bookId);
}
