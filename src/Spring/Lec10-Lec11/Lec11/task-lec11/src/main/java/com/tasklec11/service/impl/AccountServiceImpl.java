package com.tasklec11.service.impl;

import com.tasklec11.dto.AccountDTO;
import com.tasklec11.exception.AccountException;
import com.tasklec11.mapper.AccountMapper;
import com.tasklec11.model.Account;
import com.tasklec11.repo.AccountRepo;
import com.tasklec11.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepo accountRepo;

    private AccountMapper accountMapper;

    @Autowired
    public AccountServiceImpl(AccountRepo accountRepo, AccountMapper accountMapper) {
        this.accountRepo = accountRepo;
        this.accountMapper = accountMapper;
    }


    @Override
    public AccountDTO getByEmail(String email) {
        Optional<Account> account = accountRepo.findByEmail(email);

        if(account.isEmpty()){
            throw new AccountException(
                    "Account",
                    "Account dose not exists"
            );
        }

        return accountMapper.convertFromAccountToAccountDto(
                account.get()
        );
    }
}
