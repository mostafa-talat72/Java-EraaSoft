package ExceptionHandeler;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Task1 {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Please enter first number: ");
            int num1 = scanner.nextInt();
            System.out.print("Please enter second number: ");
            int num2 = scanner.nextInt();
            if(num2 == 0)
                throw new ArithmeticException();
            double div = num1 / (num2 * 1.0);
            System.out.println(num1 + "/" + num2 + " = " + div);

        }catch (ArithmeticException exception){
            System.out.println("There are an error you can not divide by zero " + exception.getMessage());
        }catch (InputMismatchException exception){
            System.out.println("Input must by a number");
        }catch (Throwable throwable){
            System.out.println(throwable.getMessage());
        }

    }
}
