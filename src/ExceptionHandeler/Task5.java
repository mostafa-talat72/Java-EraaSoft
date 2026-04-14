package ExceptionHandeler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Task5 {
    public static void main(String[] args) {
        try {
            File file = new File("test.txt");
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                System.out.println(sc.nextLine());
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found!");
        }catch (Throwable throwable){
            System.out.println(throwable.getMessage());
        }
    }
}
