package com.banktransferapi.controller;

import com.banktransferapi.dto.AccountTypeDTO;
import com.banktransferapi.dto.AccountTypeRequestDTO;
import com.banktransferapi.service.AccountTypeService;
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
@RequestMapping("/account-types")
@RequiredArgsConstructor
public class AccountTypeController {

    private final AccountTypeService accountTypeService;

    // GET /account-types — list every account type with its daily transfer limit.
    @GetMapping
    public ResponseEntity<List<AccountTypeDTO>> getAllAccountTypes() {
        return ResponseEntity.ok(accountTypeService.getAllAccountTypes());
    }

    // GET /account-types/{id} — one account type; 404 through the service when missing.
    @GetMapping("/{id}")
    public ResponseEntity<AccountTypeDTO> getAccountTypeById(@PathVariable Long id) {
        return ResponseEntity.ok(accountTypeService.getAccountTypeById(id));
    }

    // POST /account-types — create; 201 with the saved representation.
    @PostMapping
    public ResponseEntity<AccountTypeDTO> createAccountType(
            @Valid @RequestBody AccountTypeRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountTypeService.createAccountType(requestDTO));
    }

    // PUT /account-types/{id} — update the name and/or the daily limit.
    @PutMapping("/{id}")
    public ResponseEntity<AccountTypeDTO> updateAccountType(
            @PathVariable Long id,
            @Valid @RequestBody AccountTypeRequestDTO requestDTO) {
        return ResponseEntity.ok(accountTypeService.updateAccountType(id, requestDTO));
    }

    // DELETE /account-types/{id} — 204 on success; business errors come from the service.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountType(@PathVariable Long id) {
        accountTypeService.deleteAccountType(id);
        return ResponseEntity.noContent().build();
    }
}
