package com.banktransferapi.service.impl;

import com.banktransferapi.dto.AccountTypeDTO;
import com.banktransferapi.dto.AccountTypeRequestDTO;
import com.banktransferapi.exception.BadRequestException;
import com.banktransferapi.exception.NotFoundException;
import com.banktransferapi.mapper.AccountTypeMapper;
import com.banktransferapi.model.AccountType;
import com.banktransferapi.repo.AccountTypeRepo;
import com.banktransferapi.service.AccountTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountTypeServiceImpl implements AccountTypeService {

    private final AccountTypeRepo accountTypeRepo;
    private final AccountTypeMapper accountTypeMapper;

    @Transactional(readOnly = true)
    @Override
    public List<AccountTypeDTO> getAllAccountTypes() {
        // Read-only query: every account type in the system.
        List<AccountType> accountTypes = accountTypeRepo.findAll();

        // Same convention as the previous task: an empty list is reported as 404.
        if(accountTypes.isEmpty()){
            throw new NotFoundException("No account types found in the system");
        }
        return accountTypeMapper.convertFromAccountTypeListToAccountTypeDtoList(accountTypes);
    }

    @Transactional(readOnly = true)
    @Override
    public AccountTypeDTO getAccountTypeById(Long id) {
        AccountType accountType = findAccountTypeOrThrow(id);
        return accountTypeMapper.convertFromAccountTypeToAccountTypeDto(accountType);
    }

    @Transactional(readOnly = true)
    @Override
    public AccountType getAccountTypeEntityById(Long id) {
        return findAccountTypeOrThrow(id);
    }

    @Override
    public AccountTypeDTO createAccountType(AccountTypeRequestDTO requestDTO) {
        // Pre-check the unique name so a duplicate becomes a clear 400
        // instead of a raw DB unique-constraint 500.
        if(accountTypeRepo.existsByName(requestDTO.getName())){
            throw new BadRequestException(
                    "Account type with name '" + requestDTO.getName() + "' already exists");
        }

        // Mapper copies name + dailyLimit; the id is left for the sequence to generate.
        AccountType accountType = accountTypeMapper.convertFromAccountTypeRequestDtoToAccountType(requestDTO);

        // Persist first, then map: the generated id comes back on the saved entity.
        AccountType saved = accountTypeRepo.save(accountType);

        return accountTypeMapper.convertFromAccountTypeToAccountTypeDto(saved);
    }

    @Override
    public AccountTypeDTO updateAccountType(Long id, AccountTypeRequestDTO requestDTO) {
        // 404 when the account type does not exist.
        AccountType accountType = findAccountTypeOrThrow(id);

        // The new name must not clash with another row (the current row is excluded by id).
        if(accountTypeRepo.existsByNameAndIdNot(requestDTO.getName(), id)){
            throw new BadRequestException(
                    "Account type with name '" + requestDTO.getName() + "' already exists");
        }

        // In-place update; the entity is managed, so dirty checking flushes at commit.
        accountTypeMapper.updateAccountTypeFromAccountTypeRequestDto(requestDTO, accountType);

        return accountTypeMapper.convertFromAccountTypeToAccountTypeDto(
                accountTypeRepo.save(accountType)
        );
    }

    @Override
    public void deleteAccountType(Long id) {
        // 404 when the account type does not exist.
        AccountType accountType = findAccountTypeOrThrow(id);

        accountTypeRepo.delete(accountType);
    }

    // Shared helper for safe entity retrieval with consistent 404 messaging.
    private AccountType findAccountTypeOrThrow(Long id) {
        return accountTypeRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Account type with id " + id + " not found"));
    }
}
