package VehicleRegistrationSystem.service;

import java.util.InputMismatchException;
import java.util.Scanner;

public class VehicleRegistrationSystem {
    private final Scanner  scanner = new Scanner(System.in);
    private RegistrationService registrationService = new RegistrationService();
    public void start(){
        System.out.println("========================================");
        System.out.println("\tVEHICLE REGISTRATION SYSTEM v1.0");

        boolean exit = false;
        int failedChoiceCounter = 0;
        while (true) {
            try {
                System.out.println("========================================");
                System.out.println("1. Register New Vehicle");
                System.out.println("2. Search Vehicle by Plate");
                System.out.println("3. Update Owner Name");
                System.out.println("4. Delete Vehicle");
                System.out.println("5. List All Vehicles");
                System.out.println("6. Filter by Vehicle Type");
                System.out.println("7. Show Owner History");
                System.out.println("8. Show Expired Registrations");
                System.out.println("9. Statistics Report");
                System.out.println("0. Exit");
                System.out.println("========================================");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1 -> {

                    }
                    case 2 -> {

                    }
                    case 3 -> {

                    }
                    case 4 -> {

                    }
                    case 5 -> {

                    }
                    case 6 -> {

                    }
                    case 7 -> {

                    }
                    case 8 -> {

                    }
                    case 9 -> {

                    }
                    case 0 -> {
                        exit = true;
                        break;
                    }
                    default -> {
                        throw new InputMismatchException();
                    }
                }
            }catch (InputMismatchException e) {
                failedChoiceCounter++;
                System.out.println("Invalid input. Please enter a number from 0 to 9.");
                scanner.nextLine();
            }
            catch (Throwable e) {
                scanner.nextLine();
                System.out.println("Error happen");
            }

            if(exit){
                System.out.println("Good bye");
                break;
            }
            if(failedChoiceCounter > 4)
            {
                System.out.println("Many filed attempte ");
                break;
            }
        }

    }

    private void register(){
        
    }
}