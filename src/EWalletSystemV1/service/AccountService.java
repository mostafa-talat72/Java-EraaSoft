package EWalletSystemV1.service;

import EWalletSystemV1.model.Account;

// AccountService interface defines the contract for account operations
public interface AccountService {
    // Create a new account
    Boolean createAccount(Account account);

    // Check if account exists by username and password
    Boolean isAccountExistByUserNameAndPassword(Account account);
}
