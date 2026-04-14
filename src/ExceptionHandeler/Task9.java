package ExceptionHandeler;

public class Task9 {
    static void method1() throws Exception {
        throw new Exception("Exception from method1");
    }

    static void method2() throws Exception {
        method1();
    }

    public static void main(String[] args) {
        try {
            method2();
        } catch (Exception e) {
            System.out.println("Caught: " + e.getMessage());
        }catch (Throwable throwable){
            System.out.println(throwable.getMessage());
        }
    }
}
