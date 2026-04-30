package EWalletSystemV3Bonus.service.impl;

import EWalletSystemV3Bonus.service.ValidationService;

import static EWalletSystemV2.util.ConsoleColor.RED;
import static EWalletSystemV2.util.ConsoleColor.RESET;

/**
 * ValidationServiceImpl
 * Responsible for validating all user inputs:
 * - Username
 * - Password
 * - Phone number
 * - Age
 * - Amounts
 * - Transfer rules
 */
public class ValidationServiceImpl implements ValidationService {

    // ===================== USERNAME VALIDATION =====================
    @Override
    public boolean isUserNameValid(String userName) {
        boolean checkValidation = true;

        // Null check
        if(userName == null)
        {
            System.out.println(RED + "❌ Username must not be null" + RESET);
            return false;
        }

        // Length check
        if(userName.isBlank() || userName.length() < 3)
        {
            System.out.println(RED + "❌ Username must contain at least 3 characters" + RESET);
            checkValidation = false;
        }

        // Must not contain digits
        if(userName.chars().anyMatch(Character::isDigit))
        {
            System.out.println(RED + "❌ Username must not contain numbers" + RESET);
            checkValidation = false;
        }
        else if(!Character.isLetter(userName.charAt(0)))
        {
            System.out.println(RED + "❌ Username must start with a letter" + RESET);
            checkValidation = false;
        }
        else if(!Character.isUpperCase(userName.charAt(0)))
        {
            System.out.println(RED + "❌ Username must start with an uppercase letter" + RESET);
            checkValidation = false;
        }

        return checkValidation;
    }

    // ===================== PASSWORD VALIDATION =====================
    @Override
    public boolean isPasswordValid(String password) {
        boolean checkValidation = true;

        if(password == null)
        {
            System.out.println(RED + "❌ Password must not be null" + RESET);
            return false;
        }

        if(password.isBlank() || password.length() < 8)
        {
            System.out.println(RED + "❌ Password must contain at least 8 characters" + RESET);
            checkValidation = false;
        }

        if(!Character.isLetter(password.charAt(0)))
        {
            System.out.println(RED + "❌ Password must start with a letter" + RESET);
            checkValidation = false;
        }

        if(password.chars().noneMatch(Character::isUpperCase))
        {
            System.out.println(RED + "❌ Password must contain at least one uppercase letter" + RESET);
            checkValidation = false;
        }

        if(password.chars().noneMatch(Character::isLowerCase))
        {
            System.out.println(RED + "❌ Password must contain at least one lowercase letter" + RESET);
            checkValidation = false;
        }

        if(password.chars().noneMatch(Character::isDigit))
        {
            System.out.println(RED + "❌ Password must contain at least one digit" + RESET);
            checkValidation = false;
        }

        if(password.chars().allMatch(Character::isLetterOrDigit))
        {
            System.out.println(RED + "❌ Password must contain at least one special character" + RESET);
            checkValidation = false;
        }

        return checkValidation;
    }

    // ===================== PHONE VALIDATION =====================
    @Override
    public boolean isPhoneNumberValid(String phoneNumber) {
        boolean checkValidation = true;

        if(phoneNumber == null)
        {
            System.out.println(RED + "❌ Phone number must not be null" + RESET);
            return false;
        }

        if(phoneNumber.isBlank() || phoneNumber.length() != 11)
        {
            System.out.println(RED + "❌ Phone number must be 11 digits" + RESET);
            checkValidation = false;
        }

        if(!phoneNumber.chars().allMatch(Character::isDigit))
        {
            System.out.println(RED + "❌ Phone number must contain only digits" + RESET);
            checkValidation = false;
        }

        if(!phoneNumber.startsWith("0"))
        {
            System.out.println(RED + "❌ Phone number must start with 0" + RESET);
            checkValidation = false;
        }

        return checkValidation;
    }

    // ===================== AGE VALIDATION =====================
    @Override
    public boolean isAgeValid(float age) {
        boolean checkValidation = true;

        if(age < 18.0)
        {
            System.out.println(RED + "❌ Age must be 18 or above" + RESET);
            checkValidation = false;
        }

        return checkValidation;
    }

    // ===================== AMOUNT VALIDATION =====================
    @Override
    public boolean isAmountValid(double amount) {
        boolean checkValidation = true;

        if(amount < 100)
        {
            System.out.println(RED + "❌ Amount must be ≥ 100" + RESET);
            checkValidation = false;
        }

        if(amount % 100 != 0)
        {
            System.out.println(RED + "❌ Amount must be a multiple of 100" + RESET);
            checkValidation = false;
        }

        return checkValidation;
    }

    // ===================== TRANSFER VALIDATION =====================
    @Override
    public boolean isTransferAmountValid(double amount) {
        boolean checkValidation = true;

        if(amount <= 0)
        {
            System.out.println(RED + "❌ Transfer amount must be greater than 0" + RESET);
            checkValidation = false;
        }

        return checkValidation;
    }
}
