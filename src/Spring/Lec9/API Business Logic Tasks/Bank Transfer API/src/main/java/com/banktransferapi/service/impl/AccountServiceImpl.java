package com.banktransferapi.service.impl;

import com.banktransferapi.dto.AccountDTO;
import com.banktransferapi.dto.AccountRequestDTO;
import com.banktransferapi.dto.AccountUpdateRequestDTO;
import com.banktransferapi.exception.BadRequestException;
import com.banktransferapi.exception.NotFoundException;
import com.banktransferapi.helper.AccountHelper;
import com.banktransferapi.mapper.AccountMapper;
import com.banktransferapi.model.Account;
import com.banktransferapi.model.AccountType;
import com.banktransferapi.repo.AccountRepo;
import com.banktransferapi.service.AccountService;
import com.banktransferapi.service.AccountTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepo accountRepo;
    private final AccountTypeService accountTypeService;
    private final AccountMapper accountMapper;
    private final AccountHelper accountHelper;

    @Transactional(readOnly = true)
    @Override
    public List<AccountDTO> getAllAccounts() {
        // Read-only query: every account in the system.
        List<Account> accounts = accountRepo.findAll();

        // Same convention as the previous task: an empty list is reported as 404,
        // not as an empty 200 response.
        if(accounts.isEmpty()){
            throw new NotFoundException("No accounts found in the system");
        }

        return accountMapper.convertFromAccountListToAccountDtoList(accounts);
    }

    @Transactional(readOnly = true)
    @Override
    public AccountDTO getAccountById(Long id) {
        // Entity -> response DTO; 404 is raised by the shared helper below.
        return accountMapper.convertFromAccountToAccountDto(findAccountOrThrow(id));
    }

    @Transactional(readOnly = true)
    @Override
    public Account getAccountEntityById(Long id) {
        // Raw entity for callers that need the row without a DTO.
        return findAccountOrThrow(id);
    }

    @Override
    public AccountDTO createAccount(AccountRequestDTO requestDTO) {
        // Pre-check the unique business key so a duplicate becomes a clear 400
        // instead of a raw DB unique-constraint 500.
        if(accountRepo.existsByAccountNumber(requestDTO.getAccountNumber())){
            throw new BadRequestException(
                    "Account number '" + requestDTO.getAccountNumber() + "' already exists");
        }

        // Mapper copies the plain fields and turns openingBalance into balance.
        // The mapper ignores accountType on purpose, so the service resolves it here.
        Account account = accountMapper.convertFromAccountRequestDtoToAccount(requestDTO);

        // accountTypeId -> real AccountType entity; 404 when the type does not exist.
        // Required because BT_ACCOUNT.account_type_id is NOT NULL.
        AccountType accountType = accountTypeService.getAccountTypeEntityById(requestDTO.getAccountTypeId());

        account.setAccountType(accountType);

        return accountMapper.convertFromAccountToAccountDto(
                accountRepo.save(account)
        );
    }

    @Override
    public AccountDTO updateAccount(Long id, AccountUpdateRequestDTO requestDTO) {
        // Managed entity: dirty checking flushes the changes at commit, no explicit save needed.
        Account account = findAccountOrThrow(id);

        // Copies holderName + active only; id/accountNumber/balance/accountType are ignored.
        accountMapper.updateAccountFromAccountUpdateRequestDto(requestDTO, account);

        // The account type is not part of the mapper, so it is applied explicitly here.
        AccountType accountType = accountTypeService.getAccountTypeEntityById(requestDTO.getAccountTypeId());

        account.setAccountType(accountType);

        // No save() needed: the entity is managed and dirty checking flushes at commit.
        return accountMapper.convertFromAccountToAccountDto(account);
    }

    @Override
    public void deleteAccount(Long id) {
        // Fetch first, so deleting a missing id is a 404 and not a silent no-op.
        Account account = findAccountOrThrow(id);

        // Refuse the delete when the account already appears in BT_TRANSFER,
        // otherwise the FK constraint fails and the client gets a 500.
        if(accountHelper.hasTransferHistory(id)){
            throw new BadRequestException(
                    "Account with id " + id + " has transfer history and cannot be deleted");
        }

        accountRepo.delete(account);
    }

    // Shared helper for safe entity retrieval with consistent 404 messaging.
    private Account findAccountOrThrow(Long id) {
        return accountHelper.getOrThrow(id);
    }
}
