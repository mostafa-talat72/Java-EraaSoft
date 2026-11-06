package com.libraryapi.controller;

import com.libraryapi.dto.BorrowDTO;
import com.libraryapi.dto.BorrowRequestDTO;
import com.libraryapi.service.BorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/borrows")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;

    // POST /borrows - borrow a copy; 201 with the dates the server chose.
    // All business rules (copies, cap, overdue, loan period) live in the service.
    @PostMapping
    public ResponseEntity<BorrowDTO> borrow(@Valid @RequestBody BorrowRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(borrowService.borrow(requestDTO));
    }

    // PUT /borrows/{id}/return - returns the copy and reports the fine, if any.
    @PutMapping("/{id}/return")
    public ResponseEntity<BorrowDTO> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(borrowService.returnBook(id));
    }

    // GET /borrows - every borrow, newest first.
    @GetMapping
    public ResponseEntity<List<BorrowDTO>> getAllBorrows() {
        return ResponseEntity.ok(borrowService.getAllBorrows());
    }

    // GET /borrows/overdue - still out and already past the due date.
    // Declared before /{id} for readability; Spring matches the literal path first.
    @GetMapping("/overdue")
    public ResponseEntity<List<BorrowDTO>> getOverdueBorrows() {
        return ResponseEntity.ok(borrowService.getOverdueBorrows());
    }

    // GET /borrows/{id} - one borrow; 404 through the service when missing.
    @GetMapping("/{id}")
    public ResponseEntity<BorrowDTO> getBorrowById(@PathVariable Long id) {
        return ResponseEntity.ok(borrowService.getBorrowById(id));
    }

    // GET /borrows/member/{memberId} - a member's history, newest first.
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<BorrowDTO>> getBorrowsByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(borrowService.getBorrowsByMember(memberId));
    }

    // GET /borrows/book/{bookId} - a book's history, newest first.
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BorrowDTO>> getBorrowsByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(borrowService.getBorrowsByBook(bookId));
    }
}
