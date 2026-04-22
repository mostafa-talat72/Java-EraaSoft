package EWalletSystemV1.service.impl;

import EWalletSystemV1.model.Account;
import EWalletSystemV1.model.EWalletSystem;
import EWalletSystemV1.service.AccountService;

public class AccountServiceImpl implements AccountService {

    // EWalletSystem holds the list of accounts
    private EWalletSystem eWalletSystem = new EWalletSystem();

    @Override
    public Boolean createAccount(Account account) {
        // Check if username already exists
        boolean isAccountExist = eWalletSystem.getAccounts()
                .stream()
                .anyMatch(acc -> acc.getUserName().equals(account.getUserName()));

        if (isAccountExist) {
            System.out.println("⚠ Username '" + account.getUserName() + "' is already taken. Please choose another.");
            return false;
        }

        // Add new account
        eWalletSystem.getAccounts().add(account);
        return true;
    }

    @Override
    public Boolean isAccountExistByUserNameAndPassword(Account account) {
        // Check if account exists with matching username and password

        return eWalletSystem.getAccounts()
                .stream()
                .anyMatch(acc -> acc.getUserName().equals(account.getUserName())
                        && acc.getPassword().equals(account.getPassword()));
    }
}
