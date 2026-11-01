package com.banktransferapi.controller;

import com.banktransferapi.dto.AccountDTO;
import com.banktransferapi.dto.AccountRequestDTO;
import com.banktransferapi.dto.AccountUpdateRequestDTO;
import com.banktransferapi.service.AccountService;
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
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // GET /accounts — list every account with its type and current balance.
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    // GET /accounts/{id} — one account; 404 through the service when missing.
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    // POST /accounts — create; openingBalance becomes the starting balance.
    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(
            @Valid @RequestBody AccountRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.createAccount(requestDTO));
    }

    // PUT /accounts/{id} — only holderName, accountTypeId and active are mutable.
    @PutMapping("/{id}")
    public ResponseEntity<AccountDTO> updateAccount(
            @PathVariable Long id,
            @Valid @RequestBody AccountUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(accountService.updateAccount(id, requestDTO));
    }

    // DELETE /accounts/{id} — 204 on success; refused when transfer history exists.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
