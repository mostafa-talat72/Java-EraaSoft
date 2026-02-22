
import java.util.Scanner;

import static java.util.Collections.swap;

public class Main {
    public static void main(String[] args) {
        Scanner input= new Scanner(System.in);

    }




    public int removeElement(int[] nums, int val) {
        int k=0,i=0;
        while(i<nums.length){
            if(nums[i]!=val)
                nums[k++]=nums[i];
            i++;
        }
        return k;
    }
}