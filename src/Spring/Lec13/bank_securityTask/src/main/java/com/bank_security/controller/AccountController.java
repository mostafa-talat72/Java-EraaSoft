package com.bank_security.controller;

import com.bank_security.dto.AccountCreateRequestDTO;
import com.bank_security.dto.AccountDTO;
import com.bank_security.dto.AccountSimpleDTO;
import com.bank_security.helper.CustomUserDetails;
import com.bank_security.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("accounts")
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ACCOUNT_READ')")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable long id
            , @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        AccountDTO accountDTO = accountService.getAccountById(id, customUserDetails);
        return ResponseEntity.ok(accountDTO);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ACCOUNT_CREATE')")
    public ResponseEntity<AccountDTO> createAccount(@Valid @RequestBody AccountCreateRequestDTO accountCreateRequestDTO
            , @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        AccountDTO accountDTO = accountService.createAccount(accountCreateRequestDTO);
        return ResponseEntity.ok(accountDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ACCOUNT_UPDATE')")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable long id, @Valid @RequestBody AccountSimpleDTO accountSimpleDTO
            , @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        AccountDTO accountDTO = accountService.updateAccount(id, accountSimpleDTO);
        return ResponseEntity.ok(accountDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ACCOUNT_DELETE')")
    public ResponseEntity<Void> deleteAccount(@PathVariable long id
            , @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-accounts")
    @PreAuthorize("hasAuthority('ACCOUNT_READ')")
    public ResponseEntity<AccountDTO> getMyAccount(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        AccountDTO accountDTO = accountService.getMyAccount(customUserDetails);
        return ResponseEntity.ok(accountDTO);
    }
}
