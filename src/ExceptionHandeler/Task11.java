package ExceptionHandeler;

public class Task11 {
    public static void main(String[] args) {
        try {
            int result = 10 / 0;
        } catch (ArithmeticException e) {
            System.out.println("Caught exception!");
        } catch (Throwable throwable){
            System.out.println(throwable.getMessage());
        }finally {
            System.out.println("Finally block always executes!");
        }
    }
}
