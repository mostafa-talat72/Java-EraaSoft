package EWalletSystemV3Bonus.service.impl;

import EWalletSystemV3Bonus.model.Account;
import EWalletSystemV3Bonus.model.EWalletSystem;
import EWalletSystemV3Bonus.service.AccountService;
import EWalletSystemV3Bonus.service.ValidationService;

import java.util.Objects;
import java.util.Optional;

import static EWalletSystemV2.util.ConsoleColor.*;

/**
 * AccountServiceImpl
 * Handles all core wallet operations:
 * - Account creation
 * - Authentication
 * - Deposit / Withdraw / Transfer
 * - Password management
 * - Admin operations
 * - Transaction history
 */
public class AccountServiceImpl implements AccountService {

    // In-memory system storing all accounts
    private final EWalletSystem eWalletSystem = new EWalletSystem();
    private final ValidationService validationService = new ValidationServiceImpl();

    // ========================== CREATE ACCOUNT ==========================
    @Override
    public Account createAccount(Account account) {

        // Validate account input data before creation
        if(!validationService.isUserNameValid(account.getUserName()) ||
                !validationService.isPasswordValid(account.getPassword()) ||
                !validationService.isPhoneNumberValid(account.getPhoneNumber()) ||
                !validationService.isAgeValid(account.getAge()))
            return null;

        // Check duplicate username or phone number
        Optional<Account> isAccountExist = eWalletSystem.getAccounts()
                .stream()
                .filter(acc -> acc.getUserName().equals(account.getUserName())
                        || acc.getPhoneNumber().equals(account.getPhoneNumber()))
                .findAny();

        if (isAccountExist.isPresent()) {
            System.out.println(RED + "❌ Account already exists (username or phone number)." + RESET);
            return null;
        }

        // Save account
        eWalletSystem.getAccounts().add(account);
        return account;
    }

    // ========================== AUTH CHECK ==========================
    @Override
    public Account isAccountExistByUserNameAndPassword(Account account) {

        // Validate login credentials
        return eWalletSystem.getAccounts()
                .stream()
                .filter(acc -> acc.getUserName().equals(account.getUserName())
                        && acc.getPassword().equals(account.getPassword()))
                .findFirst()
                .orElse(null);
    }

    // ========================== DEPOSIT ==========================
    @Override
    public Boolean deposit(Account account, Double amount) {

        if (!validationService.isAmountValid(amount))
            return false;

        Account isAccountExist = this.isAccountExistByUserNameAndPassword(account);

        if (Objects.isNull(isAccountExist)) {
            System.out.println(RED + "❌ Invalid credentials for deposit." + RESET);
            return false;
        }

        // Update balance
        isAccountExist.setBalance(isAccountExist.getBalance() + amount);

        addTransactionHistory(isAccountExist,"Deposit", "💰 +" + amount);
        return true;
    }

    // ========================== WITHDRAW ==========================
    @Override
    public Boolean withdraw(Account account, Double amount) {

        if (!validationService.isAmountValid(amount))
            return false;

        Account isAccountExist = this.isAccountExistByUserNameAndPassword(account);

        if (Objects.isNull(isAccountExist)) {
            System.out.println(RED + "❌ Invalid credentials for withdrawal." + RESET);
            return false;
        }

        if(account.getBalance() < amount){
            System.out.println(RED + "❌ Insufficient balance." + RESET);
            return false;
        }

        // Deduct balance
        isAccountExist.setBalance(isAccountExist.getBalance() - amount);

        addTransactionHistory(isAccountExist,"Withdraw", "💸 -" + amount);
        return true;
    }

    // ========================== TRANSFER ==========================
    @Override
    public Boolean transfer(Account senderAccount, String receiverUsername, double amount) {

        if(!validationService.isTransferAmountValid(amount))
            return false;

        Account isSenderAccountExist = this.isAccountExistByUserNameAndPassword(senderAccount);

        if (Objects.isNull(isSenderAccountExist)) {
            System.out.println(RED + "❌ Invalid sender credentials." + RESET);
            return false;
        }

        Account isReceiverAccountExist = eWalletSystem.getAccounts()
                .stream().filter(account -> account.getUserName().equals(receiverUsername))
                .findFirst().orElse(null);

        if (Objects.isNull(isReceiverAccountExist)) {
            System.out.println(RED + "❌ Receiver account not found." + RESET);
            return false;
        }

        if(isSenderAccountExist.getBalance() < amount){
            System.out.println(RED + "❌ Insufficient balance." + RESET);
            return false;
        }

        if(isSenderAccountExist.getUserName().equals(receiverUsername)){
            System.out.println(RED + "❌ Cannot transfer to same account." + RESET);
            return false;
        }

        // Execute transfer
        isSenderAccountExist.setBalance(isSenderAccountExist.getBalance() - amount);
        isReceiverAccountExist.setBalance(isReceiverAccountExist.getBalance() + amount);

        addTransactionHistory(isSenderAccountExist,"Transfer", "➡ Send " + amount + " to " + receiverUsername);
        addTransactionHistory(isSenderAccountExist,"Transfer", "⬅ Receive " + amount + " from " + isSenderAccountExist.getUserName());

        return true;
    }

    // ========================== CHANGE PASSWORD ==========================
    @Override
    public Boolean changePassword(Account account, String currantPassword, String newPassword) {

        if(!validationService.isPasswordValid(newPassword))
            return false;

        Account isAccountExist = this.isAccountExistByUserNameAndPassword(account);

        if (Objects.isNull(isAccountExist)) {
            System.out.println(RED + "❌ Invalid account credentials." + RESET);
            return false;
        }

        boolean isCurrantPasswordExist = eWalletSystem.getAccounts().stream()
                .anyMatch(acc -> acc.getUserName().equals(account.getUserName())
                        && acc.getPassword().equals(currantPassword));

        if(!isCurrantPasswordExist){
            System.out.println(RED + "❌ Current password is incorrect." + RESET);
            return false;
        }

        isAccountExist.setPassword(newPassword);
        return true;
    }

    // ========================== DELETE ACCOUNT ==========================
    @Override
    public Boolean deleteAccount(Account account) {

        Account isAccountExist = this.isAccountExistByUserNameAndPassword(account);

        if (Objects.isNull(isAccountExist)) {
            System.out.println(RED + "❌ Invalid account credentials." + RESET);
            return false;
        }

        eWalletSystem.getAccounts().removeIf(acc -> acc.getUserName().equals(account.getUserName()));
        return true;
    }

    // ========================== TRANSACTION HISTORY ==========================
    @Override
    public void addTransactionHistory(Account account, String type, String details) {
        account.addTransaction(type, details);
    }

    @Override
    public void showTransactionHistory(Account account){

        account.getTransactionHistory().forEach(transaction -> {
            System.out.println(BLUE + "📌 " + transaction.getKey() + RESET);
            System.out.println("   " + PURPLE + "➡ " + transaction.getValue().getKey()
                    + " | " + transaction.getValue().getValue() + RESET);
        });
    }

    // ========================== ADMIN: SHOW ALL ACCOUNTS ==========================
    @Override
    public Boolean showAllAccounts(Account account) {

        // Check admin privileges
        boolean isAccountAdminAndExist = eWalletSystem.getAccounts().stream()
                .anyMatch(acc -> acc.getUserName().equals(account.getUserName())
                        && acc.getPassword().equals(account.getPassword())
                        && acc.isAdmin());

        if(!isAccountAdminAndExist)
        {
            System.out.println(RED + "❌ Access denied: Admins only." + RESET);
            return false;
        }

        // Display all accounts
        eWalletSystem.getAccounts().forEach(acc ->{
            System.out.println("👤 Username: " + BLUE + acc.getUserName() + RESET
                    + "\n🔒 Password: " + BLUE + acc.getPassword() + RESET
                    + "\n📱 Phone: " + BLUE + acc.getPhoneNumber() + RESET
                    + "\n🎂 Age: " + BLUE + acc.getAge() + RESET
                    + "\n💰 Balance: " + BLUE + acc.getBalance() + RESET);

            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        });

        return true;
    }
}
