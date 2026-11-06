package com.libraryapi.controller;

import com.libraryapi.dto.BorrowStatusDTO;
import com.libraryapi.dto.BorrowStatusRequestDTO;
import com.libraryapi.service.BorrowStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/borrow-statuses")
@RequiredArgsConstructor
public class BorrowStatusController {

    private final BorrowStatusService borrowStatusService;

    // GET /borrow-statuses - every stored state.
    @GetMapping
    public ResponseEntity<List<BorrowStatusDTO>> getAllBorrowStatuses() {
        return ResponseEntity.ok(borrowStatusService.getAllBorrowStatuses());
    }

    // GET /borrow-statuses/{id} - one status; 404 through the service when missing.
    @GetMapping("/{id}")
    public ResponseEntity<BorrowStatusDTO> getBorrowStatusById(@PathVariable Long id) {
        return ResponseEntity.ok(borrowStatusService.getBorrowStatusById(id));
    }

    // POST /borrow-statuses - create; 201 with the saved representation.
    @PostMapping
    public ResponseEntity<BorrowStatusDTO> createBorrowStatus(
            @Valid @RequestBody BorrowStatusRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(borrowStatusService.createBorrowStatus(requestDTO));
    }

    // PUT /borrow-statuses/{id} - rename a status.
    @PutMapping("/{id}")
    public ResponseEntity<BorrowStatusDTO> updateBorrowStatus(
            @PathVariable Long id,
            @Valid @RequestBody BorrowStatusRequestDTO requestDTO) {
        return ResponseEntity.ok(borrowStatusService.updateBorrowStatus(id, requestDTO));
    }

    // DELETE /borrow-statuses/{id} - 204 on success; refused while borrows use it.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrowStatus(@PathVariable Long id) {
        borrowStatusService.deleteBorrowStatus(id);
        return ResponseEntity.noContent().build();
    }
}
