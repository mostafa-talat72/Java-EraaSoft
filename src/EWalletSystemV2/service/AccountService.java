package EWalletSystemV2.service;

import EWalletSystemV2.model.Account;

// AccountService interface defines the contract for account operations
public interface AccountService {
    // Create a new account
    Account createAccount(Account account);

    // Check if account exists by username and password
    Account isAccountExistByUserNameAndPassword(Account account);

    // check if deposit confirmed or not
    Boolean deposit(Account account, Double amount);
    Boolean withdraw(Account account, Double amount);
    Boolean transfer(Account senderAccount, String receiverUsername, double amount);
    Boolean changePassword(Account account, String currantPassword, String newPassword);
    Boolean deleteAccount(Account account);
    void addTransactionHistory(Account account, String type, String details);
    void showTransactionHistory(Account account);
}
