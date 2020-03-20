package com.zd.core.utils.type;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
     * <p>
     * 1.比较连续2个值
     * 2.当小值在大值的左边 减小值
     * 3.小值在大值的右边，则加小值
     */
    public static int romanToInt(String s) {
        int sum = 0;
        int preNum = romanMap.get(s.charAt(0));
        for (int i = 1; i < s.length(); i++) {
            int num = romanMap.get(s.charAt(i));
            if (preNum < num) {
                sum -= preNum;
            } else {
                sum += preNum;
            }
            preNum = num;
        }
        sum += preNum;
        return sum;
    }

    /**
     * coins 数组中求和  amount 最小数量
     */
    public int coinChange(int[] coins, int amount) {
        int max = amount + 1;
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, max);
        //dp: i 为金额 d[i] 表示次数  （默认amount + 1）
        dp[0] = 0;
        for (int i = 0; i <= amount; i++) {
            for (int j = 0; j < coins.length; j++) {
                //如果硬币金额超过 amount跳过
                if (coins[j] <= i) {
                    //dp[i - coins[j]]表示之前差额的次数
                    dp[i] = Math.min(dp[i], dp[i - coins[j]] + 1);
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }

    public int orangesRotting(int[][] grid) {

        int[] dr = new int[]{-1, 0, 1, 0};
        int[] dc = new int[]{0, -1, 0, 1};

        ArrayDeque<Integer> queue = new ArrayDeque<>();
        Map<Integer, Integer> map = new HashMap<>();
        //初始化
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 2) {
                    //找出所有腐坏的
                    int code = i * grid[0].length + j;
                    queue.add(code);
                    map.put(code, 0);
                }
            }
        }

        int ans = 0;
        while (!queue.isEmpty()) {
            int code = queue.remove();
            int r = code / grid[0].length, c = code % grid[0].length;
            for (int i = 0; i < 4; ++i) {
                int nr = r + dr[i];
                int nc = c + dc[i];
                if (0 <= nr && nr < grid.length && 0 <= nc && nc < grid[0].length && grid[nr][nc] == 1) {
                    grid[nr][nc] = 2;
                    int ncode = nr * grid[0].length + nc;
                    queue.add(ncode);
                    map.put(ncode, map.get(code) + 1);
                    ans = map.get(ncode);
                }
            }
        }

        for (int[] row : grid)
            for (int v : row)
                if (v == 1)
                    return -1;

        return ans;
    }

    public int[] distributeCandies(int candies, int num_people) {
        int[] result = new int[num_people];
        int num = 1;
        for (int i = 0; candies > num; i++) {
            result[i % num_people] = num + result[i % num_people];
            candies -= num;
            num++;
        }
        if (candies > 0) {
            result[(num - 1) % num_people] = candies + result[(num - 1) % num_people];
        }
        return result;
    }

    public int climbStairs(int n) {
        if (n == 1) {
            return 1;
        }
        int[] dp = new int[n + 1];
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }

    /**
     * 查找数组种超过半数的数
     */
    public int majorityElement(int[] nums) {
        int count = 0;
        Integer candidate = null;
        for (int num : nums) {
            if (count == 0) {
                candidate = num;
            }
            count += (candidate == num) ? 1 : -1;
        }
        return candidate;
    }

    public int maxProfit(int[] prices) {
        int result = 0;
        Integer start = null;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i - 1] < prices[i]) {
                if (start == null) {
                    start = prices[i - 1];
                }
            }
            if (prices[i - 1] > prices[i]) {
                if (start != null) {
                    result += prices[i - 1] - start;
                    start = null;
                }
            }
            if (i == prices.length - 1 && start != null) {
                result += prices[i] - start;
            }
        }
        return result;
    }

    /**
     * 判断矩阵重叠
     */
    public boolean isRectangleOverlap(int[] rec1, int[] rec2) {
        return rec1[0] < rec2[2] &&
                rec1[2] > rec2[0] &&
                rec1[1] < rec2[3] &&
                rec1[3] > rec2[1];
    }

    /**
     * 单个数,其实的出现2次
     */
    public int singleNumber(int[] nums) {
        int result = 0;
        for (int num : nums) {
            result ^= num;
        }
        return result;
    }

    /**
     * 两数之和
     */
    public int[] twoSum(int[] numbers, int target) {
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers.length; j++) {
                if (i != j && numbers[i] + numbers[j] == target) {
                    return new int[]{i + 1, j + 1};
                }
            }
        }
        return new int[]{};
    }

    /**
     * Excel
     */
    public String convertToTitle(int n) {
        StringBuilder result = new StringBuilder();
        while (n > 0) {
            n -= 1;
            result.append(Character.toChars('A' + (n % 26)));
            n /= 26;
        }
        result.reverse();
        return result.toString();
    }

    /**
     * Excel
     */
    public int titleToNumber(String s) {
        int n = 0;
        for (int i = 0; i < s.length(); i++) {
            n = n * 26 + (s.charAt(i) - 'A' + 1);
        }
        return n;
    }

    /**
     * 返回乘皆种0的数量
     */
    public int trailingZeroes(int n) {
        int count = 0;
        while (n > 0) {
            count += n / 5;
            n = n / 5;
        }
        return count;
    }

    /**
     * 给定一个数组，将数组中的元素向右移动 k 个位置
     */
    public void rotate(int[] nums, int k) {
        for (int i = 0; i < k; i++) {
            int n = nums[nums.length - 1];
            System.arraycopy(nums, 0, nums, 1, nums.length - 1);
            nums[0] = n;
        }
    }

    /**
     * 颠倒二进制
     */
    public int reverseBits(int n) {
        int result = 0;
        for (int i = 31; i >= 0; i--) {
            result = result | (((n >> (31 - i)) & 1) << i);
        }
        return result;
    }

    public int hammingWeight(int n) {
        return Integer.bitCount(n);
    }

    public int rob(int[] nums) {
        //当前最大值
        int curMax = 0;
        //上一个最大值
        int preMax = 0;
        for (int num : nums) {
            int temp = curMax;
            curMax = Math.max(preMax + num, curMax);
            preMax = temp;
        }
        return curMax;
    }

    /**
     * 是否为快乐数
     */
    public boolean isHappy(int n) {
        int p = n, q = getNext(n);
        while (q != 1) {
            p = getNext(p);
            q = getNext(getNext(q));
            if (p == q) return false;
        }
        return true;
    }

    int getNext(int x) {
        int n = 0;
        while (x > 0) {
            n += (x % 10) * (x % 10);
            x /= 10;
        }
        return n;
    }

    /**
     * 获取最小的k位
     */
    public int[] getLeastNumbers(int[] arr, int k) {
        return Arrays.stream(arr)
                .sorted()
                .limit(k)
                .toArray();
    }

    /**
     * 获取小于n的质数
     */
    public int countPrimes(int n) {
        if (n < 2) {
            return 0;
        }
        BitSet bit = new BitSet();
        for (int i = 2; Math.pow(i, 2) < n; i++) {
            if (!bit.get(i)) {
                for (int j = i * i; j < n; j += i) {
                    bit.set(j);
                }
            }
        }
        return n - 2 - bit.cardinality();
    }

    /**
     * 是否是二的幂次
     */
    public boolean isPowerOfTwo(int n) {
        return n >= 1 && Integer.bitCount(n) == 1;
    }

    /**
     * 是否为丑数
     */
    public boolean isUgly(int num) {
        while (num > 0) {
            if (num % 2 == 0) {
                num = num >> 1;
                continue;
            }
            if (num % 3 == 0) {
                num /= 3;
                continue;
            }
            if (num % 5 == 0) {
                num /= 5;
                continue;
            }
            if (num==1){
                return true;
            }
            return false;
        }
        return false;
    }

    public static void main(String[] args) {

        System.out.println(new NumberUtil().isUgly(14));
    }
}
