package com.bank_security.service.impl;

import com.bank_security.dto.AccountCreateRequestDTO;
import com.bank_security.dto.AccountDTO;
import com.bank_security.dto.AccountSimpleDTO;
import com.bank_security.exception.CustomException;
import com.bank_security.helper.CustomUserDetails;
import com.bank_security.mapper.AccountMapper;
import com.bank_security.model.Account;
import com.bank_security.model.AccountStatus;
import com.bank_security.model.Customer;
import com.bank_security.repo.AccountRepo;
import com.bank_security.service.AccountService;
import com.bank_security.service.AccountStatusService;
import com.bank_security.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepo accountRepo;

    private AccountMapper accountMapper;

    private AccountStatusService accountStatusService;

    private CustomerService customerService;

    @Autowired
    public AccountServiceImpl(AccountRepo accountRepo, AccountMapper accountMapper, AccountStatusService accountStatusService, CustomerService customerService) {
        this.accountRepo = accountRepo;
        this.accountMapper = accountMapper;
        this.accountStatusService = accountStatusService;
        this.customerService = customerService;
    }



    @Override
    public Account getAccountEntityById(long id) {
        return accountRepo.findById(id).orElseThrow(
            () ->  new CustomException(
                "Id Not found Exception",
                "Account not found with this id: " + id
            )
        );
    }

    @Override
    public Account getAccountEntityByAccountNumber(String accountNumber) {
        return accountRepo.findByAccountNumber(accountNumber).orElseThrow(
                () ->  new CustomException(
                        "Account Number Not found Exception",
                        "Account not found with this account number: " + accountNumber
                )
        );
    }

    @Override
    public AccountDTO getAccountById(long id, CustomUserDetails customUserDetails) {
        // Task 38 matrix: ADMIN / BANK_MANAGER / AUDITOR read any account;
        // CUSTOMER reads own account only.
        boolean privileged = customUserDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")
                        || a.getAuthority().equals("ROLE_BANK_MANAGER")
                        || a.getAuthority().equals("ROLE_AUDITOR"));

        Customer customer = customerService.getCustomerEntityByEmail(customUserDetails.getUsername());

        if (!privileged) {
            if (Objects.isNull(customer.getAccount())
                    || !customer.getAccount().getId().equals(id)) {
                throw new AccessDeniedException("Access denied: this account does not belong to you");
            }
        }

        return accountMapper.convertFromAccountToAccountDto(
                getAccountEntityById(id)
        );
    }



    @Override
    public AccountDTO createAccount(AccountCreateRequestDTO accountCreateRequestDTO) {

        Customer customer = customerService.getCustomerEntityById(accountCreateRequestDTO.getCustomerId());

        if(Objects.nonNull(customer.getAccount())){
            throw new CustomException(
                    "Account already exist Exception",
                    "There are Account already exists in this customer id: " + accountCreateRequestDTO.getCustomerId()
            );
        }

        Account account = new Account();
        account.setCustomer(customer);
        account.setStatus(accountStatusService.getAccountStatusEntityByName("ACTIVE"));

        return accountMapper.convertFromAccountToAccountDto(
                accountRepo.save(account)
        );
    }

    @Override
    public AccountDTO updateAccount(long id, AccountSimpleDTO accountSimpleDTO) {
        if(Objects.nonNull(accountSimpleDTO.getId()) && accountSimpleDTO.getId() != id){
            throw new CustomException(
                    "Id not equals Exception",
                    "Account id must be equal path variable id"
            );
        }

        Account account = getAccountEntityById(id);

        if(Objects.nonNull(accountSimpleDTO.getCurrency())){
            account.setCurrency(accountSimpleDTO.getCurrency());
        }

        if(Objects.nonNull(accountSimpleDTO.getBalance())){
            account.setBalance(accountSimpleDTO.getBalance());
        }

        if(Objects.nonNull(accountSimpleDTO.getStatus())
                && Objects.nonNull(accountSimpleDTO.getStatus().getName())){
            account.setStatus(
                    accountStatusService.getAccountStatusEntityByName(
                            accountSimpleDTO.getStatus().getName()
                    )
            );
        }

        return accountMapper.convertFromAccountToAccountDto(
                accountRepo.save(account)
        );
    }

    @Override
    public void deleteAccount(long id) {
        Optional<Account> account = accountRepo.findById(id);
        if(account.isEmpty()){
            throw new CustomException(
                    "Id Not found Exception",
                    "Account not found with this id: " + id
            );
        }

        account.get().setStatus(
                accountStatusService.getAccountStatusEntityByName("CLOSED")
        );

        accountRepo.save(account.get());
    }

    @Override
    public AccountDTO getMyAccount(CustomUserDetails customUserDetails) {
        Customer customer = customerService.getCustomerEntityByEmail(customUserDetails.getUsername());

        if(Objects.isNull(customer.getAccount())){
            throw new CustomException(
                    "Account Not found Exception",
                    "Account not found in this customer: " + customUserDetails.getUsername()
            );
        }

        Account account = customer.getAccount();
        return accountMapper.convertFromAccountToAccountDto(account);
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountMapper.convertFromAccountListToAccountDtoList(
                accountRepo.findAll()
        );
    }


}
