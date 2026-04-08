import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int tst = sc.nextInt();
        while (tst-->0) {
            int n = sc.nextInt(), m = sc.nextInt();
            PriorityQueue<Integer> set = new PriorityQueue<>();
            while(m-->0){
                set.add(sc.nextInt());
            }

            int[] arr = new int[n + 1];
            for(int a : set){
                for(int i = a; i<= n ;i+=a){
                    if(arr[i] == 1)
                        break;
                    arr[i] = 1;
                }
            }

            for (int i=1;i<=n;i++)
                System.out.print(arr[i] + " ");
            System.out.println();
        }
    }
}
