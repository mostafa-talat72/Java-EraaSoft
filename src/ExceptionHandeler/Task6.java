package ExceptionHandeler;

public class Task6 {
    public static void main(String[] args) {
        try {
            String str = null;
            System.out.println(str.length());
            int result = 10 / 0;
        } catch (NullPointerException e) {
            System.out.println("Caught NullPointerException!");
        } catch (ArithmeticException e) {
            System.out.println("Caught ArithmeticException!");
        }catch (Throwable throwable){
            System.out.println(throwable.getMessage());
        }
    }
}
