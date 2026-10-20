package com.bank_security.service.impl;

import com.bank_security.exception.CustomException;
import com.bank_security.mapper.AccountStatusMapper;
import com.bank_security.model.AccountStatus;
import com.bank_security.repo.AccountStatusRepo;
import com.bank_security.service.AccountStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountStatusServiceImpl implements AccountStatusService {

    private AccountStatusRepo accountStatusRepo;

    private AccountStatusMapper accountStatusMapper;

    @Autowired
    public AccountStatusServiceImpl(AccountStatusRepo accountStatusRepo, AccountStatusMapper accountStatusMapper) {
        this.accountStatusRepo = accountStatusRepo;
        this.accountStatusMapper = accountStatusMapper;
    }


    @Override
    public AccountStatus getAccountStatusEntityByName(String name) {
        return accountStatusRepo.findByName(name).orElseThrow(()->
                    new CustomException(
                            "Status Name not found Exception",
                            "Account Status not found with this name: " + name
                    )
                );
    }
}
