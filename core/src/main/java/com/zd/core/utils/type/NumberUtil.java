package com.zd.core.utils.type;

import com.google.common.collect.ImmutableMap;

public class NumberUtil {

    /**
     * 整数反转(将整数字符顺序反转)
     * 1. 取余获取最后一位pop，push到新数的第一位
     */
    public static int reverse(int x) {
        int rev = 0;
        while (x != 0) {
            int pop = x % 10;
            x /= 10;
            if (rev > Integer.MAX_VALUE / 10 || rev < Integer.MIN_VALUE / 10) return 0;
            rev = rev * 10 + pop;
        }
        return rev;
    }

    /**
     * 回文数  （左->右==右->左） 负数与个位数不算
     * 1. 通过 x%10 x/10 得到一个y=x%10 * 10+y  取反的新数
     * 2. 当 y>x 时终止，因为数字已经大于原始值
     * 3. 为防止数字是奇数个
     */
    public static boolean isPalindrome(int x) {
        //负数和个位数排除
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }
        int rev = 0;
        while (x > rev) {
            rev = rev * 10 + x % 10;
            x /= 10;
        }
        return x == rev || x == rev / 10;
    }

    private static ImmutableMap<String, Integer> romanMap = ImmutableMap
            .<String, Integer>builder()
            .put("I", 1)
            .put("V", 5)
            .put("X", 10)
            .put("L", 50)
            .put("C", 100)
            .put("D", 500)
            .put("M", 1000)
            .put("IV", 4)
            .put("IX", 9)
            .put("XL", 40)
            .put("XC", 90)
            .put("CD", 400)
            .put("CM", 900)
            .build();

    /**
     * 罗马数字转整数
     * I : 1
     * V : 5
     * X : 10
     * L : 50
     * C : 100
     * D : 500
     * M : 1000
     *
     * 1.比较连续2个值
     * 2.当小值在大值的左边 减小值
     * 3.小值在大值的右边，则加小值
     */
    public static int romanToInt(String s) {
        int sum = 0;
        int preNum = romanMap.get(s.charAt(0));
        for(int i = 1;i < s.length(); i ++) {
            int num = romanMap.get(s.charAt(i));
            if(preNum < num) {
                sum -= preNum;
            } else {
                sum += preNum;
            }
            preNum = num;
        }
        sum += preNum;
        return sum;
    }
}
