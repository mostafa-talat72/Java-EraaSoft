package com.bank_security.controller;

import com.bank_security.dto.TransferDTO;
import com.bank_security.dto.TransferSimpleDTO;
import com.bank_security.helper.CustomUserDetails;
import com.bank_security.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TRANSFER_CREATE')")
    public ResponseEntity<TransferDTO> createTransfer(@Valid @RequestBody TransferSimpleDTO transferSimpleDTO
            , @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        TransferDTO transferDTO = transferService.createTransfer(transferSimpleDTO, customUserDetails);

        return ResponseEntity.created(URI.create("")).body(transferDTO);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('TRANSFER_READ')")
    public ResponseEntity<TransferDTO> getTransferById(@PathVariable long id
            , @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        TransferDTO transferDTO = transferService.getTransferById(id, customUserDetails);
        return ResponseEntity.ok(transferDTO);
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('TRANSFER_APPROVE')")
    public ResponseEntity<TransferDTO> approveTransferById(@PathVariable long id
            , @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        TransferDTO transferDTO = transferService.approveTransferById(id, customUserDetails);
        return ResponseEntity.ok(transferDTO);
    }
}
