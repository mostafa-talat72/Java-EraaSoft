package com.banktransferapi.service;

import com.banktransferapi.dto.AccountTypeDTO;
import com.banktransferapi.dto.AccountTypeRequestDTO;
import com.banktransferapi.model.AccountType;

import java.util.List;

public interface AccountTypeService {

    List<AccountTypeDTO> getAllAccountTypes();

    AccountTypeDTO getAccountTypeById(Long id);

    // Raw entity lookup — used internally by AccountServiceImpl.
    AccountType getAccountTypeEntityById(Long id);

    AccountTypeDTO createAccountType(AccountTypeRequestDTO requestDTO);

    AccountTypeDTO updateAccountType(Long id, AccountTypeRequestDTO requestDTO);

    void deleteAccountType(Long id);
}
