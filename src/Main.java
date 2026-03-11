
import java.util.*;

import static java.util.Collections.swap;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int n = input.nextInt(),out = 0;
        while(n-- > 0) {
            int a = input.nextInt(), b = input.nextInt();
            out += (b - a >= 2) ? 1 : 0;
        }
        System.out.println(out);
    }
}