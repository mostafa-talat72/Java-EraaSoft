package EWalletSystemV2.service.impl;

import EWalletSystemV2.service.ValidationService;

import static EWalletSystemV2.util.ConsoleColor.*;
public class ValidationServiceImpl implements ValidationService {


    @Override
    public boolean isUserNameValid(String userName) {
        boolean checkValidation = true;
        if(userName == null)
        {
            System.out.println(RED + "❌ userName must not be null" + RESET);
            return false;
        }
        if( userName.isBlank() || userName.length() < 3)
        {
            System.out.println(RED + "❌ The username must contain at least 3 character" + RESET);
            checkValidation = false;
        }

        if (userName.chars().anyMatch(Character::isDigit))
        {
            System.out.println(RED + "❌ The username must not contain a numbers" + RESET);
            checkValidation = false;
        }
        else if (!Character.isLetter(userName.charAt(0)))
        {
            System.out.println(RED + "❌ Username must start with a letter" + RESET);
            checkValidation = false;
        } else if (!Character.isUpperCase(userName.charAt(0))) {
            System.out.println(RED + "❌ Username must start with a Uppercase letter" + RESET);
            checkValidation = false;
        }

        return checkValidation;
    }

    @Override
    public boolean isPasswordValid(String password) {
        boolean checkValidation = true;
        if(password == null)
        {
            System.out.println(RED + "❌ password must not be null" + RESET);
            return false;
        }
        if( password.isBlank() || password.length() < 8)
        {
            System.out.println(RED + "❌ The password must contain at least 8 character" + RESET);
            checkValidation = false;
        }

        if (!Character.isLetter(password.charAt(0)))
        {
            System.out.println(RED + "❌ password must start with a letter" + RESET);
            checkValidation = false;
        }
        if (password.chars().noneMatch(Character::isUpperCase)) {
            System.out.println(RED + "❌ password must contain at least one Uppercase letter" + RESET);
            checkValidation = false;
        }

        if (password.chars().noneMatch(Character::isLowerCase)) {
            System.out.println(RED + "❌ password must contain at least one Lowercase letter" + RESET);
            checkValidation = false;
        }


        if (password.chars().noneMatch(Character::isDigit)) {
            System.out.println(RED + "❌ password must contain at least one digit" + RESET);
            checkValidation = false;
        }


        if (password.chars().allMatch(Character::isLetterOrDigit)) {
            System.out.println(RED + "❌ password must contain at least one special character" + RESET);
            checkValidation = false;
        }
        return checkValidation;
    }

    @Override
    public boolean isPhoneNumberValid(String phoneNumber) {
        boolean checkValidation = true;
        if(phoneNumber == null)
        {
            System.out.println(RED + "❌ phone number must not be null" + RESET);
            return false;
        }
        if( phoneNumber.isBlank() || phoneNumber.length() != 11)
        {
            System.out.println(RED + "❌ The phone number must 11 digits" + RESET);
            checkValidation = false;
        }

        if (!phoneNumber.chars().allMatch(Character::isDigit)) {
            System.out.println(RED + "❌ phone number must contain only numbers" + RESET);
            checkValidation = false;
        }

        if (!Character.isDigit(phoneNumber.charAt(0)) && phoneNumber.charAt(0) != '0')
        {
            System.out.println(RED + "❌ phone number must start with 0" + RESET);
            checkValidation = false;
        }


        return checkValidation;
    }

    @Override
    public boolean isAgeValid(float age) {
        boolean checkValidation = true;

        if( age < 18.0)
        {
            System.out.println(RED + "❌ The age must be more than or equal 18" + RESET);
            checkValidation = false;
        }

        return checkValidation;
    }

    @Override
    public boolean isAmountValid(double amount) {
        boolean checkValidation = true;
        if( amount < 100)
        {
            System.out.println(RED + "❌ The amount must be more than or equals 100" + RESET);
            checkValidation = false;
        }

        if(amount % 100 != 0){
            System.out.println(RED + "❌ The value must be 100 or a multiple of 100" + RESET);
            checkValidation = false;
        }

        return checkValidation;
    }

    @Override
    public boolean isTransferAmountValid(double amount) {
        boolean checkValidation = true;
        if( amount <= 0)
        {
            System.out.println(RED + "❌ The amount must be more than 0" + RESET);
            checkValidation = false;
        }

        return checkValidation;
    }
}
