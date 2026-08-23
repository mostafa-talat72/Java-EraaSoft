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
    public int hIndex(int[] citations) {
        Arrays.sort(citations);
        int l = 0, r = citations.length, mid = 0, out = 0;
        while(l<=r){
            mid=(l+r) / 2;
            System.out.println("mid = " + mid);
            int lowBound = lower_bound(citations, mid);
            if(citations.length -  lowBound >= mid ){
                l = mid+1;
                out = Math.max(out, mid);
            }else
                r=mid-1;
        }
        return out;
    }
    public int lower_bound(int[] citations, int num){
        int l = 0, r = citations.length, mid = 0, out = citations.length;
        while(l<r){
            mid=(l+r) / 2;
            if(citations[mid] >= num){
                r=mid-1;
                out = mid;
            }else
                l = mid+1;
        }
        System.out.println("num = " + out);
        return out;
    }
}
class RandomizedSet {
    private List<Integer> list;
    public RandomizedSet() {
        list = new ArrayList<>();
    }

    public boolean insert(int val) {
        if(list.contains(val))
            return false;
        list.add(val);
       return true;
    }

    public boolean remove(int val) {
        boolean flag = false;
        if(list.contains(val))
        {
            list.remove((Integer) val);
            flag = true;
        }
        return flag;
    }

    public int getRandom() {
        int randIdx = new Random().nextInt(list.size());
        return list.get(randIdx);
    }
}