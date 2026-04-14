package ExceptionHandeler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Task10 {
    static void readFile() throws IOException {
        FileReader fr = new FileReader("test.txt");
        BufferedReader br = new BufferedReader(fr);
        System.out.println(br.readLine());
        br.close();
    }

    public static void main(String[] args) {
        try {
            readFile();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }catch (Throwable throwable){
            System.out.println(throwable.getMessage());
        }
    }
}
