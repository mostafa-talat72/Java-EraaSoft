package ExceptionHandeler;

public class Task12 {
    public static void main(String[] args) {
        try {
            try {
                int result = 10 / 0;
            } catch (ArithmeticException e) {
                System.out.println("Inner catch: Division by zero!");
                throw e;
            }catch (Throwable throwable){
                System.out.println(throwable.getMessage());
                throw  throwable;
            }
        } catch (ArithmeticException e) {
            System.out.println("Outer catch: Exception handled!");
        }catch (Throwable throwable){
            System.out.println(throwable.getMessage());
        }
    }
}
