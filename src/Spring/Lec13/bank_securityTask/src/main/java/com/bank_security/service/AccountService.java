package com.bank_security.service;

import com.bank_security.dto.AccountCreateRequestDTO;
import com.bank_security.dto.AccountDTO;
import com.bank_security.dto.AccountSimpleDTO;
import com.bank_security.helper.CustomUserDetails;
import com.bank_security.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

// Service contract for Account.
// Entity getters stay unannotated: they are internal plumbing used by other
// services during business flows (e.g. transfers) under their own permissions.
public interface AccountService {

    public Account getAccountEntityById(long id);

    public Account getAccountEntityByAccountNumber(String accountNumber);

    @PreAuthorize("hasAuthority('ACCOUNT_READ')")
    public AccountDTO getAccountById(long id, CustomUserDetails customUserDetails);

    @PreAuthorize("hasAuthority('ACCOUNT_CREATE')")
    public AccountDTO createAccount(AccountCreateRequestDTO accountCreateRequestDTO);

    @PreAuthorize("hasAuthority('ACCOUNT_UPDATE')")
    public AccountDTO updateAccount(long id, AccountSimpleDTO accountSimpleDTO);

    @PreAuthorize("hasAuthority('ACCOUNT_DELETE')")
    public void deleteAccount(long id);

    @PreAuthorize("hasAuthority('ACCOUNT_READ')")
    public AccountDTO getMyAccount(CustomUserDetails customUserDetails);

    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    public List<AccountDTO> getAllAccounts();
}
