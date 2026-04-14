package ExceptionHandeler;

public class Task7 {
    public static void main(String[] args) {
        try {
            int age = 15;
            if (age < 18) {
                throw new InvalidAgeException("Age must be 18 or above!");
            }
            System.out.println("Valid age: " + age);
        } catch (InvalidAgeException e) {
            System.out.println("Error: " + e.getMessage());
        }catch (Throwable throwable){
            System.out.println(throwable.getMessage());
        }
    }
}
class InvalidAgeException extends Exception {
    public InvalidAgeException(String message) {
        super(message);
    }
}