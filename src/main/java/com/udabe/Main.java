package com.udabe;

import jdk.dynalink.linker.LinkerServices;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        int[] digits = {1,6,1,6,2};
        //Test 1:
//        plusOne(digits);

        //Test 2:
//        singleNumber(digits);
        String immutable = "abc";
        immutable = immutable + "def";
        System.out.println(immutable);
    }

    private static void singleNumber(int[] nums) {
        Arrays.sort(nums);
        List<Integer> resultStore = new ArrayList<>();
        for(int i = 0 ; i < nums.length; i++) {
            if(i == nums.length - 1) {
                break;
            }
            if(nums[i + 1] != nums[i] && nums[i - 1] != nums[i]) {
                resultStore.add(nums[i]);
            }
        }
        resultStore.forEach(x -> System.out.println(x));
    }

    public static void plusOne(int[] digits) {
        System.out.println("Old arr:");
        printArr(digits);
        int max = digits.length;
        digits[max-1] = digits[max-1] + 1;
        System.out.println("New arr:");
        printArr(digits);
    }

    public static void printArr(int[] digits) {
        for (int i = 0 ; i < digits.length; i++) {
            System.out.print(digits[i] + " ");
        }
    }

}
