package com.zd.core.utils.type;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
            if (num == 1) {
                return true;
            }
            return false;
        }
        return false;
    }


    public int mySqrt(int x) {
        if (x < 2) {
            return x;
        }
        int left = (int) Math.pow(Math.E, 0.5 * Math.log(x));
        int right = left + 1;
        return (long) right * right > x ? left : right;
    }

    public int findTheDistanceValue(int[] arr1, int[] arr2, int d) {
        int num = 0;
        for (int value : arr1) {
            boolean flag = true;
            for (int i : arr2) {
                if (Math.abs(value - i) <= d) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                num++;
            }
        }
        return num;
    }

    public int getKth(int lo, int hi, int k) {
        return IntStream.range(lo, hi + 1)
                .boxed()
                .sorted(Comparator.<Integer, Integer>comparing(i -> {
                    int count = 0;
                    while (i != 1) {
                        if (i % 2 == 0) {
                            i = i >> 1;
                        } else {
                            i = 3 * i + 1;
                        }
                        count++;
                    }
                    return count;
                }).thenComparing(Comparator.naturalOrder()))
                .skip(k - 1)
                .findFirst()
                .get();
    }

    public int maxSizeSlices(int[] slices) {
        int[] a = new int[slices.length - 1];
        int[] b = new int[slices.length - 1];
        System.arraycopy(slices, 0, a, 0, a.length);
        System.arraycopy(slices, 1, b, 0, b.length);
        return Math.max(slices(a), slices(b));
    }

    public int slices(int[] slices) {
        int[][] dp = new int[slices.length / 3 + 1][slices.length];
        dp[0] = slices;
        for (int i = 1; i < slices.length / 3 + 1; i++) {
            int maxValue = 0;
            for (int j = 0; j < slices.length; ++j) {
                if (j >= 2) maxValue = Math.max(maxValue, dp[i - 1][j - 2]);
                dp[i][j] = maxValue + slices[j];
            }
        }
        return Arrays.stream(dp[slices.length / 3]).max().getAsInt();
    }

    public int maxNumberOfFamilies(int n, int[][] reservedSeats) {
        int num = 0;
        int j = 0;
        int i = 1;
        Arrays.sort(reservedSeats, Comparator.comparing(ints -> ints[0]));
        boolean flag = true;
        boolean[] ret = new boolean[11];
        while (j < reservedSeats.length) {
            if (reservedSeats[j][0] == i) {
                flag = false;
                ret[reservedSeats[j][1] - 1] = true;
                j++;
                if (j != reservedSeats.length) {
                    continue;
                }
            }
            i++;
            if (flag) {
                num += 2;
                continue;
            }
            flag = true;
            boolean b = !ret[1] && !ret[2] && !ret[3] && !ret[4];
            boolean c = !ret[5] && !ret[6] && !ret[7] && !ret[8];
            if (b && c) {
                num += 2;
                ret = new boolean[11];
                continue;
            }
            if ((b)) {
                num++;
                ret = new boolean[11];
                continue;
            }
            if (c) {
                num++;
                ret = new boolean[11];
                continue;
            }
            if ((!ret[3] && !ret[4] && !ret[5] && !ret[6])) {
                num++;
            }
            ret = new boolean[11];
        }
        if (n >= i) {
            num = (n - i + 1) * 2 + num;
        }
        return num;
    }

    public int[] createTargetArray(int[] nums, int[] index) {
        List<Integer> target = new ArrayList<>();
        for (int i = 0; i < index.length; i++) {
            target.add(index[i], nums[i]);
        }
        return target.stream().mapToInt(Integer::intValue).toArray();
    }

    public int sumFourDivisors(int[] nums) {
        int result = 0;
        for (int num : nums) {
            Set<Integer> ints = divisorsNum(num);
            if (ints.size() == 2) {
                result += ints.stream().reduce(Integer::sum).get() + num + 1;
            }
        }
        return result;
    }

    /**
     * 求因数个数
     */
    private Set<Integer> divisorsNum(int n) {
        int num = n;
        Set<Integer> set = new HashSet<>();
        for (int i = 2; i < n / 2; i++) {
            if (n % i == 0) {
                set.add(i);
                set.add(num / i);
            }
            if (set.size() > 2) {
                break;
            }
        }
        return set;
    }

    public boolean isPowerOfThree(int n) {
        if (n == 1) {
            return true;
        }
        while (n > 0) {
            if (n % 4 == 0) {
                n /= 4;
                if (n == 1) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 不连续最大值
     */
    public int massage(int[] nums) {
        if (nums.length == 1) {
            return nums[0];
        }
        int[][] dp = new int[nums.length / 2 + 1][nums.length];

        dp[0] = nums;

        int result = 0;

        for (int i = 1; i < nums.length / 2 + 1; i++) {
            int maxValue = 0;
            for (int j = 0; j < nums.length; j++) {
                if (j >= 2) maxValue = Math.max(maxValue, dp[i - 1][j - 2]);
                dp[i][j] = maxValue + nums[j];
                result = Math.max(dp[i][j], result);
            }
        }
        return result;
    }

    /**
     * 判断是否是完全平方数
     */
    public boolean isPerfectSquare(int num) {
        int sqrt = (int) Math.sqrt(num);
        return sqrt * sqrt == num;
    }

    public int getSum(int a, int b) {
        while (b != 0) {
            int temp = a ^ b;
            b = (a & b) << 1;
            a = temp;
        }
        return a;
    }

    /**
     * 计算表面积
     */
    public int surfaceArea(int[][] grid) {
        int result = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                //算上所有面
                if (grid[i][j] > 0) {
                    result += (grid[i][j] << 2) + 2;
                    result -= i > 0 ? Math.min(grid[i][j], grid[i - 1][j]) << 1 : 0;
                    result -= j > 0 ? Math.min(grid[i][j], grid[i][j - 1]) << 1 : 0;
                }
            }
        }
        return result;
    }

    /**
     * 最长上升子序列
     */
    public int lengthOfLIS(int[] nums) {
        int len = 1, n = nums.length;
        if (n == 0) {
            return 0;
        }
        int[] d = new int[n + 1];
        d[len] = nums[0];
        //dp为最长位的最小值
        for (int i = 1; i < n; ++i) {
            if (nums[i] > d[len]) {
                //若当前数大于dp最大位的值，直接设置当前值位dp下一位的值
                d[++len] = nums[i];
            } else {
                //否则二分法,找出小当前值的最大值
                int l = 1, r = len, pos = 0;
                while (l <= r) {
                    int mid = (l + r) >> 1;
                    if (d[mid] < nums[i]) {
                        pos = mid;
                        l = mid + 1;
                    } else {
                        r = mid - 1;
                    }
                }
                d[pos + 1] = nums[i];
            }
        }
        return len;
    }

    public boolean canMeasureWater(int x, int y, int z) {
        //两个都满还不够
        if (x + y < z) {
            return false;
        }
        //如果有空瓶，除非z是0，或者z刚好等于那个瓶子
        if (x == 0 || y == 0) {
            return z == 0 || x + y == z;
        }
        //z如果能整除 x,y的最大公约数
        return z % new BigInteger(String.valueOf(x))
                .gcd(new BigInteger(String.valueOf(y))).intValue() == 0;
    }

    public int numRookCaptures(char[][] board) {
        int a = 0, b = 0, result = 0;
        int[] dx = new int[]{0, 1, 0, -1};
        int[] dy = new int[]{1, 0, -1, 0};
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if ('R' == board[i][j]) {
                    a = i;
                    b = j;
                    break;
                }
            }
        }
        for (int i = 0; i < 4; ++i) {
            for (int step = 0; ; ++step) {
                int tx = a + step * dx[i];
                int ty = b + step * dy[i];
                if (tx < 0 || tx >= 8 || ty < 0 || ty >= 8 || board[tx][ty] == 'B')
                    break;
                if (board[tx][ty] == 'p') {
                    result++;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 计算岛屿。广度优先算法
     */
    public int maxAreaOfIsland(int[][] grid) {
        int result = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                int num = 0;
                Stack<Integer> stacki = new Stack<>();
                Stack<Integer> stackj = new Stack<>();
                stacki.push(i);
                stackj.push(j);
                while (!stacki.empty()) {
                    int curi = stacki.pop();
                    int curj = stackj.pop();
                    //若当前节点不为1直接跳过
                    if (curi < 0 ||
                            curj < 0 ||
                            curi == grid.length ||
                            curj == grid[0].length ||
                            grid[curi][curj] != 1) {
                        continue;
                    }
                    num++;
                    //当前点设置成0
                    grid[curi][curj] = 0;
                    int[] di = {0, 0, 1, -1};
                    int[] dj = {1, -1, 0, 0};
                    //查看当前点的周围四个点
                    for (int k = 0; k < 4; k++) {
                        stacki.push(curi + di[k]);
                        stackj.push(curj + dj[k]);
                    }
                }
                result = Math.max(result, num);
            }
        }
        return result;
    }

    /**
     * 唯一值最小增量
     */
    public int minIncrementForUnique(int[] A) {
        Lists.<Integer>newArrayList().stream().max(Comparator.comparing(Function.identity())).orElse(-1);
        int result = 0, token = 0;   //重复次数
        //先进行排序
        Arrays.sort(A);
        for (int i = 1; i < A.length; i++) {
            if (A[i - 1] == A[i]) {
                //若重复，token+1
                token++;
                //操作次数减去当前数值
                result -= A[i];
            } else {
                //若不重复,
                int give = Math.min(token, A[i] - A[i - 1] - 1);
                //在A[i-1]-A[i]区间中,重复数要修改的次数
                result += give * (give + 1) / 2 + give * A[i - 1];
                //重复次数减少
                token -= give;
            }
        }
        //再算一次，防止有重复次数不为0
        if (A.length > 0) {
            result += token * (token + 1) / 2 + token * A[A.length - 1];
        }
        return result;
    }

    /**
     * 猜数字
     */
    public int guessNumber(int n, int pick) {
        int left = 0, right = n, result = 0;
        while (left < right) {
            n = left + ((right - left) >> 1);
            result = guess(n, pick);
            if (result == -1) {
                left = n + 1;
            }
            if (result == 1) {
                right = n - 1;
            }
            if (result == 0) {
                return n;
            }
        }
        return n;
    }

    private int guess(int n, int pick) {
        if (n == pick) {
            return 0;
        }
        return pick > n ? -1 : 1;
    }

    /**
     * 10进制转16进制
     */
    public String toHex(int num) {
        if (num == 0) {
            return "0";
        }
        char[] letter = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuilder sb = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            //依次求4位
            int value = (num >>> (4 * i)) & 15;
            if (value == 0) {
                if (sb.length() == 0) {
                    continue;
                }
                sb.append("0");
            } else {
                sb.append(letter[value]);
            }
        }
        return sb.toString();
    }

    public boolean hasGroupsSizeX(int[] deck) {
        Map<Integer, Long> map = Arrays.stream(deck)
                .boxed()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return map.values()
                .stream()
                .reduce((a, b) -> new BigInteger(String.valueOf(a)).gcd(new BigInteger(String.valueOf(b))).longValue())
                .orElse(0L) >= 2;
    }

    public List<String> fizzBuzz(int n) {
        return IntStream.range(1, n + 1).mapToObj(i -> {
            if (i % 3 == 0 && i % 5 == 0) return "FizzBuzz";
            if (i % 3 == 0) return "Fizz";
            if (i % 5 == 0) return "Buzz";
            return String.valueOf(i);
        }).collect(Collectors.toList());
    }

    /**
     * 第K大的数,堆排序
     */
    public int thirdMax(int[] nums) {
        List<Integer> collect = Arrays.stream(nums)
                .distinct()
                .sorted()
                .boxed()
                .collect(Collectors.toList());
        return collect.size() > 2 ? collect.get(2) : collect.get(collect.size() - 1);
    }

    /**
     * 完全二叉树特性
     * 1.若i为奇数且i>1，那么tree的左兄弟为tree[i-1]；
     * 2.若i为偶数且i<n，那么tree的右兄弟为tree[i+1]；
     * 3.若i>1，tree的父亲节点为tree[i div 2]；
     * 4.若2*i<=n，那么tree的左孩子为tree[2*i]；若2*i+1<=n，那么tree的右孩子为tree[2*i+1]；
     * <p>
     * 构建大顶堆
     */
    public void heapSort(int[] nums) {
        int len = nums.length - 1;
        //创建初始堆
        buildMaxHeap(nums, len);
        for (int i = len; i >= 1; --i) {
            //将最大值放入最后一位
            int temp = nums[i];
            nums[i] = nums[0];
            nums[0] = temp;
            len -= 1;
            maxHeapify(nums, 0, len);
        }
    }

    /**
     * 创建大顶堆
     */
    private void buildMaxHeap(int[] nums, int len) {
        for (int i = len / 2; i >= 0; --i) {
            //从最后非叶子节点开始
            maxHeapify(nums, i, len);
        }
    }

    private void maxHeapify(int[] nums, int i, int len) {
        for (; (i << 1) + 1 <= len; ) {
            //左节点
            int lson = (i << 1) + 1;
            //右节点
            int rson = (i << 1) + 2;
            int large;
            //左节点大于父节点
            if (lson <= len && nums[lson] > nums[i]) {
                large = lson;
            } else {
                large = i;
            }
            //右节点大于(父节点和左节点的最大值)
            if (rson <= len && nums[rson] > nums[large]) {
                large = rson;
            }
            //最大不是父节点
            if (large != i) {
                //交换
                int temp = nums[i];
                nums[i] = nums[large];
                nums[large] = temp;
                i = large;
            } else break;
        }
    }

    /**
     * 两个字符串数字相加
     */
    public String addStrings(String num1, String num2) {
        StringBuilder sb = new StringBuilder();
        int i = num1.length() - 1, j = num2.length() - 1, carry = 0;
        while (i >= 0 || j >= 0) {
            int n1 = i >= 0 ? num1.charAt(i) - '0' : 0;
            int n2 = j >= 0 ? num2.charAt(j) - '0' : 0;
            int tmp = n1 + n2 + carry;
            carry = tmp / 10;
            sb.append(tmp % 10);
            i--;
            j--;
        }
        if (carry == 1) sb.append(1);
        return sb.reverse().toString();
    }

    /**
     * 第n个剩下的数(约瑟夫环)
     */
    public int lastRemaining(int n, int m) {
        int f = 0;
        for (int i = 2; i != n + 1; ++i)
            f = (m + f) % i;
        return f;
    }

    /**
     * 两数之和
     */
    public int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];
        Map<Integer,Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i],i);
        }
        for (int i = 0; i < nums.length; i++) {
            Integer j = map.get(target - nums[i]);
            if (j!=null&&i!=j){
                result[0]=i;
                result[1]=j;
                break;
            }
        }
        return result;
    }


    public static void main(String[] args) {
        System.out.println(new NumberUtil().addStrings("0", "9"));
    }
}
