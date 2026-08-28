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
    public int[] productExceptSelf(int[] nums) {
        int[] answer = new int[nums.length];
        Arrays.fill(answer, 1);
        int product = 1;
        for(int i=nums.length - 1;i>=0;i--){
            answer[i] = product;
            product *= nums[i];
        }
        product = nums[0];
        for(int i=1;i<nums.length;i++){
            answer[i] = answer[i] * product;
            product *= nums[i];
        }
        return answer;
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