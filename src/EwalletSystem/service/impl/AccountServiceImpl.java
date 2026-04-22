package EwalletSystem.service.impl;

import EwalletSystem.model.Account;
import EwalletSystem.model.EWalletSystem;
import EwalletSystem.service.AccountService;

public class AccountServiceImpl implements AccountService {

    private EWalletSystem eWalletSystem = new EWalletSystem();

    @Override
    public Boolean createAccount(Account account) {
        boolean isAccountExist =  eWalletSystem.getAccounts()
                .stream()
                .anyMatch(acc -> acc.getUserName().equals(account.getUserName()));
        if(isAccountExist)
            return false;
        eWalletSystem.getAccounts().add(account);
        return true;
    }

    @Override
    public Boolean isAccountExistByUserNameAndPassword(Account account) {
        return eWalletSystem.getAccounts()
                .stream()
                .anyMatch(acc -> acc.getUserName().equals(account.getUserName()) && acc.getPassword().equals(account.getPassword()));
    }


}
