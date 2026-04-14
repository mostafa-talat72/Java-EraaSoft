package ExceptionHandeler;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Task2 {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter a number: ");
            String input = sc.next();
            int num = Integer.parseInt(input);
            System.out.println("Converted number: " + num);
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format!");
        }catch (InputMismatchException exception){
            System.out.println("Input must by a number");
        }catch (Throwable throwable){
            System.out.println(throwable.getMessage());
        }
    }
}
