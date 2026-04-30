package EWalletSystemV2.model;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

// Account class represents a user account in the E-Wallet system
public class Account {
    // Fields (attributes of the account)
    private String userName;     // Username of the account holder
    private String password;     // Password for authentication
    private Double balance;      // Current balance in the wallet
    private String phoneNumber;  // Phone number of the account holder
    private float age;           // Age of the account holder
    private List<AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, LocalDateTime>>> transactionHistory;
    // Default constructor
    public Account() {
    }

    // Constructor for creating a new account with full details
    public Account(String userName, String password, String phoneNumber, float age) {
        this.setUserName(userName);
        this.setPassword(password);
        this.setBalance(0.0); // New accounts start with balance = 0
        this.setPhoneNumber(phoneNumber);
        this.setAge(age);
        this.transactionHistory = new ArrayList<AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, LocalDateTime>>>();
    }

    // Constructor for login (only username and password needed)
    public Account(String userName, String password) {
        this.setUserName(userName);
        this.setPassword(password);
    }

    // Getter and Setter for userName
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Getter and Setter for password
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and Setter for balance
    public Double getBalance() {
        return balance;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    // Getter and Setter for phoneNumber
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Getter and Setter for age
    public float getAge() {
        return age;
    }
    public void setAge(float age) {
        this.age = age;
    }

    public List<AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, LocalDateTime>>> getTransactionHistory() {
        return transactionHistory;
    }

    public void addTransaction(String type, String details){
        this.transactionHistory.add(new AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, LocalDateTime>>(type, new AbstractMap.SimpleEntry<String, LocalDateTime>(details, LocalDateTime.now())));
    }
}
