package EWalletSystemV2.service.impl;

import EWalletSystemV2.model.Account;
import EWalletSystemV2.model.EWalletSystem;
import EWalletSystemV2.service.AccountService;
import EWalletSystemV2.service.ValidationService;

import java.lang.management.ManagementFactory;
import java.util.Objects;
import java.util.Optional;
import static EWalletSystemV2.util.ConsoleColor.*;

public class AccountServiceImpl implements AccountService {

    // EWalletSystem holds the list of accounts
    private final EWalletSystem eWalletSystem = new EWalletSystem();
    private final ValidationService validationService = new ValidationServiceImpl();

    @Override
    public Account createAccount(Account account) {
        if(!validationService.isUserNameValid(account.getUserName()) ||
                !validationService.isPasswordValid(account.getPassword()) ||
                !validationService.isPhoneNumberValid(account.getPhoneNumber()) ||
                !validationService.isAgeValid(account.getAge()))
            return null;
        // Check if username already exists
        Optional<Account> isAccountExist = eWalletSystem.getAccounts()
                .stream()
                .filter(acc -> acc.getUserName().equals(account.getUserName())
                || acc.getPhoneNumber().equals(account.getPhoneNumber()))
                .findAny();

        if (isAccountExist.isPresent()) {
            System.out.println(RED + "❌ Username or phone number is already taken. Please choose another." + RESET);
            return null;
        }
        // Add new account
        eWalletSystem.getAccounts().add(account);

        return account;
    }

    @Override
    public Account isAccountExistByUserNameAndPassword(Account account) {
        // Check if account exists with matching username and password

        return eWalletSystem.getAccounts()
                .stream()
                .filter(acc -> acc.getUserName().equals(account.getUserName()) && acc.getPassword().equals(account.getPassword()))
                .findFirst().orElse(null);
    }

    @Override
    public Boolean deposit(Account account, Double amount) {

        if (!validationService.isAmountValid(amount))
            return false;

        Account isAccountExist = this.isAccountExistByUserNameAndPassword(account);

        if (Objects.isNull(isAccountExist))
        {
            System.out.println(RED + "❌ Username or password dose not exist. Please try again." + RESET);
            return false;
        }

        double newBalance = isAccountExist.getBalance() + amount;
        isAccountExist.setBalance(newBalance);
        addTransactionHistory(isAccountExist,"Deposit", "+" + amount);
        return true;
    }

    @Override
    public Boolean withdraw(Account account, Double amount) {
        if (!validationService.isAmountValid(amount))
            return false;
        Account isAccountExist = this.isAccountExistByUserNameAndPassword(account);

        if (Objects.isNull(isAccountExist))
        {
            System.out.println(RED + "❌ Username or password dose not exist. Please try again." + RESET);
            return false;
        }

        if(account.getBalance() < amount){
            System.out.println(RED + "❌ Your balance is insufficient. Please try again." + RESET);
            return false;
        }

        double newBalance = isAccountExist.getBalance() - amount;
        isAccountExist.setBalance(newBalance);
        addTransactionHistory(isAccountExist,"Withdraw", "-" + amount);

        return true;
    }

    @Override
    public Boolean transfer(Account senderAccount, String receiverUsername, double amount) {
        if(!validationService.isTransferAmountValid(amount))
            return false;

        Account isSenderAccountExist = this.isAccountExistByUserNameAndPassword(senderAccount);

        if (Objects.isNull(isSenderAccountExist))
        {
            System.out.println(RED + "❌ Username or password for sender Account dose not exist. Please try again." + RESET);
            return false;
        }

        Account isReceiverAccountExist = eWalletSystem.getAccounts()
                .stream().filter(account -> account.getUserName().equals(receiverUsername))
                .findFirst().orElse(null);
        if (Objects.isNull(isReceiverAccountExist))
        {
            System.out.println(RED + "❌ Username for receiver Account dose not exist. Please try again." + RESET);
            return false;
        }
        if(isSenderAccountExist.getBalance() < amount){
            System.out.println(RED + "❌ Your balance is insufficient. Please try again." + RESET);
            return false;
        }

        if(isSenderAccountExist.getUserName().equals(receiverUsername)){
            System.out.println(RED + "❌ You can not transfer to your self. Please try again." + RESET);
            return false;
        }
        double newSenderBalance = isSenderAccountExist.getBalance() - amount;
        isSenderAccountExist.setBalance(newSenderBalance);

        double newReceiverBalance = isReceiverAccountExist.getBalance() + amount;
        isReceiverAccountExist.setBalance(newReceiverBalance);
        addTransactionHistory(isSenderAccountExist,"Transfer", "Send " + amount + " to " + receiverUsername);
        addTransactionHistory(isSenderAccountExist,"Transfer", "Receive " + amount + " from " + isSenderAccountExist.getUserName());

        return true;
    }

    @Override
    public Boolean changePassword(Account account, String currantPassword, String newPassword) {
        if(!validationService.isPasswordValid(newPassword))
            return false;

        Account isAccountExist = this.isAccountExistByUserNameAndPassword(account);
        if (Objects.isNull(isAccountExist))
        {
            System.out.println(RED + "❌ Username or password dose not exist. Please try again." + RESET);
            return false;
        }

        boolean isCurrantPasswordExist = eWalletSystem.getAccounts().stream()
                .anyMatch(acc -> acc.getUserName().equals(account.getUserName()) && acc.getPassword().equals(currantPassword));
        if(!isCurrantPasswordExist){
            System.out.println(RED + "❌ Current password dose not equal the user current password. Please try again." + RESET);
            return false;
        }

        isAccountExist.setPassword(newPassword);
        return true;
    }

    @Override
    public Boolean deleteAccount(Account account) {
        Account isAccountExist = this.isAccountExistByUserNameAndPassword(account);
        if (Objects.isNull(isAccountExist))
        {
            System.out.println(RED + "❌ Username or password dose not exist. Please try again." + RESET);
            return false;
        }
        eWalletSystem.getAccounts().removeIf(acc -> acc.getUserName().equals(account.getUserName()));
        return true;
    }

    @Override
    public void addTransactionHistory(Account account, String type, String details) {
        account.addTransaction(type, details);
    }

    @Override
    public void showTransactionHistory(Account account){
        account.getTransactionHistory().forEach(transaction -> {
            System.out.println(BLUE + transaction.getKey() + ": " + RESET);
            System.out.println("\t" + PURPLE + transaction.getValue().getKey() + " ---> " + transaction.getValue().getValue() + RESET);
        });
    }


}
