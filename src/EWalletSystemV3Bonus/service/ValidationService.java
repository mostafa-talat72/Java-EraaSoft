package EWalletSystemV3Bonus.service;

/**
 * Provides validation methods for user inputs.
 */
public interface ValidationService {

    boolean isUserNameValid(String userName);

    boolean isPasswordValid(String password);

    boolean isPhoneNumberValid(String phoneNumber);

    boolean isAgeValid(float age);

    boolean isAmountValid(double amount);

    boolean isTransferAmountValid(double amount);
}