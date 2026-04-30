package EWalletSystemV3Bonus.service;

import EWalletSystemV3Bonus.model.Account;

/**
 * Defines all operations related to account management.
 */
public interface AccountService {

    /**
     * Creates a new account
     */
    Account createAccount(Account account);

    /**
     * Validates login credentials
     */
    Account isAccountExistByUserNameAndPassword(Account account);

    /**
     * Deposits money into account
     */
    Boolean deposit(Account account, Double amount);

    /**
     * Withdraws money from account
     */
    Boolean withdraw(Account account, Double amount);

    /**
     * Transfers money between accounts
     */
    Boolean transfer(Account senderAccount, String receiverUsername, double amount);

    /**
     * Changes account password
     */
    Boolean changePassword(Account account, String currentPassword, String newPassword);

    /**
     * Deletes an account
     */
    Boolean deleteAccount(Account account);

    /**
     * Adds a transaction record
     */
    void addTransactionHistory(Account account, String type, String details);

    /**
     * Displays transaction history
     */
    void showTransactionHistory(Account account);

    /**
     * Displays all accounts (Admin only)
     */
    Boolean showAllAccounts(Account account);
}