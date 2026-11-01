package com.banktransferapi.controller;

import com.banktransferapi.dto.TransferDTO;
import com.banktransferapi.dto.TransferRequestDTO;
import com.banktransferapi.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    // POST /transfers — execute a transfer; 201 with the recorded transfer.
    // All business rules (balance, limit, verification) live in the service.
    @PostMapping
    public ResponseEntity<TransferDTO> transfer(
            @Valid @RequestBody TransferRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transferService.transfer(requestDTO));
    }

    // GET /transfers — every recorded transfer.
    @GetMapping
    public ResponseEntity<List<TransferDTO>> getAllTransfers() {
        return ResponseEntity.ok(transferService.getAllTransfers());
    }

    // GET /transfers/{id} — one transfer; 404 through the service when missing.
    @GetMapping("/{id}")
    public ResponseEntity<TransferDTO> getTransferById(@PathVariable Long id) {
        return ResponseEntity.ok(transferService.getTransferById(id));
    }

    // GET /transfers/sender/{accountId} — outgoing history, newest first.
    @GetMapping("/sender/{accountId}")
    public ResponseEntity<List<TransferDTO>> getTransfersBySender(@PathVariable Long accountId) {
        return ResponseEntity.ok(transferService.getTransfersBySender(accountId));
    }

    // GET /transfers/receiver/{accountId} — incoming history, newest first.
    @GetMapping("/receiver/{accountId}")
    public ResponseEntity<List<TransferDTO>> getTransfersByReceiver(@PathVariable Long accountId) {
        return ResponseEntity.ok(transferService.getTransfersByReceiver(accountId));
    }
}
