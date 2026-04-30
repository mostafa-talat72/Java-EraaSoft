package EWalletSystemV3Bonus.model;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user account in the E-Wallet system.
 * Contains personal information, balance, and transaction history.
 */
public class Account {

    // ==================== Fields ====================

    private String userName;     // Unique username for login
    private String password;     // Account password (should be encrypted in real systems)
    private Double balance;      // Current wallet balance
    private String phoneNumber;  // User phone number
    private float age;           // User age
    private boolean isAdmin;     // Determines if the user has admin privileges

    /**
     * Transaction history structure:
     * Key   -> Transaction type (Deposit, Withdraw, Transfer, etc.)
     * Value -> (Details, Timestamp)
     */
    private List<AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, LocalDateTime>>> transactionHistory;

    // ==================== Constructors ====================

    /**
     * Default constructor
     */
    public Account() {
    }

    /**
     * Full constructor used when creating a new account
     */
    public Account(String userName, String password, String phoneNumber, float age, boolean isAdmin) {
        this.userName = userName;
        this.password = password;
        this.balance = 0.0; // New accounts start with zero balance
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.isAdmin = isAdmin;
        this.transactionHistory = new ArrayList<>();
    }

    /**
     * Constructor used for login validation
     */
    public Account(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    // ==================== Getters & Setters ====================

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public float getAge() {
        return age;
    }

    public void setAge(float age) {
        this.age = age;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }

    public List<AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, LocalDateTime>>> getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(List<AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, LocalDateTime>>> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    // ==================== Business Logic ====================

    /**
     * Adds a new transaction to the history.
     *
     * @param type    Type of transaction (Deposit, Withdraw, Transfer)
     * @param details Additional details about the transaction
     */
    public void addTransaction(String type, String details) {
        this.transactionHistory.add(
                new AbstractMap.SimpleEntry<>(
                        type,
                        new AbstractMap.SimpleEntry<>(details, LocalDateTime.now())
                )
        );
    }
}