package com.banktransferapi.service;

import com.banktransferapi.dto.AccountDTO;
import com.banktransferapi.dto.AccountRequestDTO;
import com.banktransferapi.dto.AccountUpdateRequestDTO;
import com.banktransferapi.model.Account;

import java.util.List;

public interface AccountService {

    List<AccountDTO> getAllAccounts();

    AccountDTO getAccountById(Long id);

    // Raw entity lookup — used internally by TransferServiceImpl.
    Account getAccountEntityById(Long id);

    AccountDTO createAccount(AccountRequestDTO requestDTO);

    AccountDTO updateAccount(Long id, AccountUpdateRequestDTO requestDTO);

    void deleteAccount(Long id);
}
