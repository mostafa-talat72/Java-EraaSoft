package EWalletSystemV2.service;

public interface ValidationService {

    public boolean isUserNameValid(String userName);
    public boolean isPasswordValid(String password);
    public boolean isPhoneNumberValid(String phoneNumber);
    public boolean isAgeValid(float age);
    public boolean isAmountValid(double amount);
    public boolean isTransferAmountValid(double amount);
}
