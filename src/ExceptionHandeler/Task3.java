package ExceptionHandeler;

import java.util.InputMismatchException;

public class Task3 {
    public static void main(String[] args) {
        try {
            String str = null;
            System.out.println(str.toUpperCase());
        } catch (NullPointerException e) {
            System.out.println("Error: String is null!");
        }catch (Throwable throwable){
            System.out.println(throwable.getMessage());
        }
    }
}
