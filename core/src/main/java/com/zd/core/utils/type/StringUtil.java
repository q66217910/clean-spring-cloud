package com.zd.core.utils.type;

import com.google.common.collect.ImmutableMap;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StringUtil {

    private static ImmutableMap<Character, Character> mappings
            = ImmutableMap.<Character, Character>builder()
            .put(')', '(')
            .put('}', '{')
            .put(']', '[')
            .build();

    /**
     * 有效括号   （只支持纯符号）
     * <p>
     * 1.创建一个栈
     * 2.非闭合符号直接push到栈里
     * 3.若是闭合符号，从队列里弹出最上面的符号，找其对应的符号
     */
    public static boolean isValid(String s) {
        Stack<Character> stack = new Stack<Character>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (mappings.containsKey(c)) {
                char topElement = stack.empty() ? '#' : stack.pop();
                if (topElement != mappings.get(c)) {
                    return false;
                }
            } else {
                stack.push(c);
            }
        }
        return stack.isEmpty();
    }

    /**
     * 在 haystack 字符串中找出 needle 字符串出现的第一个位置 (从0开始)。如果不存在，则返回  -1。
     */
    public int strStr(String haystack, String needle) {
        if (needle.equals("")) {
            return 0;
        }
        if (haystack.equals("")) {
            return -1;
        }
        KMP kmp = new KMP(needle);
        return kmp.search(haystack);
    }

    public static class KMP {
        private int[][] dp;
        private String pat;

        public KMP(String pat) {
            this.pat = pat;
            int M = pat.length();
            // dp[状态][字符] = 下个状态
            dp = new int[M][256];
            dp[0][pat.charAt(0)] = 1;
            // 影子状态 X 初始为 0
            int X = 0;
            // 构建状态转移图
            for (int j = 1; j < M; j++) {
                for (int c = 0; c < 256; c++) {
                    if (pat.charAt(j) == c)
                        dp[j][c] = j + 1;
                    else
                        dp[j][c] = dp[X][c];
                }
                // 更新影子状态
                X = dp[X][pat.charAt(j)];
            }
        }

        public int search(String txt) {
            // 借助 dp 数组去匹配 txt
            int M = pat.length();
            int N = txt.length();
            int j = 0;
            for (int i = 0; i < N; i++) {
                // 当前是状态 j，遇到字符 txt[i]
                // pat 应该转移到哪个状态？
                j = dp[j][txt.charAt(i)];
                // 如果达到终止态，返回匹配开头的索引
                if (j == M) return i - M + 1;
            }
            return -1;
        }
    }


    public String countAndSay(int n) {
        if (n == 1) {
            return "1";
        }
        return say(countAndSay(n - 1));
    }

    private String say(String s) {
        //记录数值出现的次数
        int count = 1;
        //当前的数值
        char num = s.charAt(0);
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == num) {
                count++;
            } else {
                sb.append(count);
                sb.append(num);
                num = c;
                count = 1;
            }
        }
        sb.append(count);
        sb.append(num);
        return sb.toString();
    }

    /**
     * 自定义字符串压缩
     */
    public String compressString(String s) {
        int num = 1;
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i < s.length() + 1; i++) {
            char c = s.charAt(i - 1);
            if (i < s.length() && s.charAt(i) == s.charAt(i - 1)) {
                num += 1;
                continue;
            }
            sb.append(c);
            sb.append(num);
            num = 1;
        }
        if (sb.length() >= s.length() || sb.length() == 0) {
            return s;
        }
        return sb.toString();
    }

    public int countCharacters(String[] words, String chars) {
        int result = 0;
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < chars.length(); i++) {
            Integer num = map.get(chars.charAt(i));
            if (num == null) {
                num = 0;
            }
            map.put(chars.charAt(i), ++num);
        }
        for (String word : words) {
            Map<Character, Integer> hashMap = new HashMap<>(map);
            boolean flag = true;
            for (int i = 0; i < word.length(); i++) {
                Integer num = hashMap.get(word.charAt(i));
                if (num == null || num == 0) {
                    flag = false;
                    break;
                }
                num--;
                hashMap.put(word.charAt(i), num);
            }
            if (flag) {
                result += word.length();
            }
        }
        return result;
    }

    /**
     * 是否是回文串
     */
    public boolean isPalindrome(String s) {
        String low = s.toLowerCase();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < low.length(); i++) {
            if (Character.isLetterOrDigit(low.charAt(i))) {
                sb.append(low.charAt(i));
            }
        }
        return sb.toString().equals(sb.reverse().toString());
    }

    /**
     * 最长回文串
     */
    public int longestPalindrome(String s) {
        int sum = s.chars()
                .boxed()
                .collect(Collectors.toMap(Function.identity(), v -> 1, Integer::sum))
                .values()
                .stream()
                .mapToInt(i -> i - (i & 1))
                .sum();
        return sum < s.length() ? sum + 1 : sum;
    }

    /**
     * 最长回文子串
     */
    public String longestPalindrome2(String s) {
        String res = "";
        //dp: i代表开始,j代表结束
        boolean[][] dp = new boolean[s.length()][s.length()];
        //这里从后找,也可以从前找,i=0,j从i递减
        for (int i = s.length() - 1; i >= 0; i--) {
            for (int j = i; j < s.length(); j++) {
                //状态转移方程: i和j的值相同并且j-i<2:初始值或者i+1和j-1的值相同
                dp[i][j] = s.charAt(i) == s.charAt(j) && (j - i < 2 || dp[i + 1][j - 1]);
                //设置res最长串
                if (dp[i][j] && j - i + 1 > res.length()) {
                    res = s.substring(i, j + 1);
                }
            }
        }
        return res;
    }


    /**
     * 给定字符串 s 和 t ，判断 s 是否为 t 的子序列。
     */
    public boolean isSubsequence(String s, String t) {
        int sLen = s.length();
        int tLen = t.length();
        if (sLen > tLen) {
            return false;
        }
        if (sLen == 0) {
            return true;
        }
        boolean[][] dp = new boolean[sLen + 1][tLen + 1];
        for (int i = 0; i < tLen; i++) {
            dp[0][i] = true;
        }
        for (int i = 1; i <= sLen; i++) {
            for (int j = 1; j <= tLen; j++) {
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = dp[i][j - 1];
                }
            }
        }
        return dp[sLen][tLen];
    }

    /**
     * 二进制手表
     */
    public List<String> readBinaryWatch(int num) {
        List<String> ans = new ArrayList<>();
        String[][] hstrs = {{"0"},
                {"1", "2", "4", "8"},
                {"3", "5", "6", "9", "10"},
                {"7", "11"}};
        String[][] mstrs = {{"00"},
                {"01", "02", "04", "08", "16", "32"},
                {"03", "05", "06", "09", "10", "12", "17", "18", "20", "24", "33", "34", "36", "40", "48"},
                {"07", "11", "13", "14", "19", "21", "22", "25", "26", "28", "35", "37", "38", "41", "42", "44", "49", "50", "52", "56"},
                {"15", "23", "27", "29", "30", "39", "43", "45", "46", "51", "53", "54", "57", "58"},
                {"31", "47", "55", "59"}};
        for (int i = 0; i <= Math.min(3, num); i++) {
            if (num - i > 5) continue;
            String[] hstr = hstrs[i];
            String[] mstr = mstrs[num - i];
            for (String s : hstr) {
                for (String value : mstr) {
                    ans.add(s + ":" + value);
                }
            }
        }
        return ans;
    }

    /**
     * 是否位同构字符串
     */
    public boolean isIsomorphic(String s, String t) {
        return isomorphic(s, t) && isomorphic(t, s);
    }

    private boolean isomorphic(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        Map<Character, Character> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            Character c = s.charAt(i);
            if (map.containsKey(c)) {
                if (map.get(c) != t.charAt(i)) {
                    return false;
                }
            } else {
                map.put(c, t.charAt(i));
            }
        }
        return true;
    }

    public boolean isAnagram(String s, String t) {
        return s.chars()
                .boxed()
                .sorted()
                .map(Object::toString)
                .collect(Collectors.joining())
                .equals(t.chars()
                        .boxed()
                        .sorted()
                        .map(Object::toString)
                        .collect(Collectors.joining()));
    }

    /**
     * 最长回文串
     */
    public String longestPrefix(String s) {
        StringBuilder a = new StringBuilder();
        int k = 1;
        for (int i = 0; i < s.length() && k < s.length(); i++) {
            for (; k < s.length(); ) {
                k++;
                if ((s.charAt(i) ^ s.charAt(k - 1)) == 0) {
                    a.append(s.charAt(i));
                    if (i + 1 < s.length() && k < s.length() && ((s.charAt(i + 1) ^ s.charAt(k)) != 0)) {
                        a = new StringBuilder();
                        i = -1;
                    }
                    break;
                }
            }
        }
        return a.toString();
    }

    /**
     * 单词规律
     */
    public boolean wordPattern(String pattern, String str) {
        Map<Character, Integer> map = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (int i = 0; i < pattern.length(); i++) {
            if (map.containsKey(pattern.charAt(i))) {
                Integer value = map.get(pattern.charAt(i));
                sb.append(value);
            } else {
                sb.append(index);
                map.put(pattern.charAt(i), index++);
            }
        }

        Map<String, Integer> map2 = new HashMap<>();
        StringBuilder s = new StringBuilder();
        int index1 = 1;
        String[] split = str.split(" ");
        for (String item : split) {
            if (map2.containsKey(item)) {
                Integer value = map2.get(item);
                s.append(value);
            } else {
                s.append(index1);
                map2.put(item, index1++);
            }
        }

        return sb.toString().equals(s.toString());
    }

    public String getHint(String secret, String guess) {
        int bulls = 0;
        int cows = 0;
        int[] a = new int[10];
        int[] b = new int[10];
        for (int i = 0; i < secret.length(); i++) {
            a[secret.charAt(i) - '0']++;
            b[guess.charAt(i) - '0']++;
            if ((secret.charAt(i) ^ guess.charAt(i)) == 0) {
                bulls++;
            }
        }
        for (int i = 0; i < a.length; i++) {
            cows += Math.min(a[i], b[i]);
        }
        return bulls + "A" + (cows - bulls) + "B";
    }

    /**
     * 字符串最大公因子
     */
    public String gcdOfStrings(String str1, String str2) {
        //数学法 （首尾相加相等，说明有公约字符串）
        if ((str1 + str2).equals(str2 + str1)) {
            //求长度的最大公约数就是最大的最大公因子
            return str2.substring(0, new BigInteger(String.valueOf(str1.length())).
                    gcd(new BigInteger(String.valueOf(str2.length()))).intValue());
        }
        return "";
    }

    /**
     * 反转字符串 (双指针)
     */
    public void reverseString(char[] s) {
        int left = 0;
        int right = s.length - 1;
        while (left < right) {
            char temp = s[left];
            s[left] = s[right];
            s[right] = temp;
            left++;
            right--;
        }
    }

    /**
     * 反转字符串中的元音
     */
    public String reverseVowels(String s) {
        char[] chars = s.toCharArray();
        int left = 0;
        int right = s.length() - 1;
        boolean flagL, flagR;
        while (left < right) {
            flagL = isVowels(s.charAt(left));
            flagR = isVowels(s.charAt(right));
            if (!flagL) {
                left++;
            }
            if (!flagR) {
                right--;
            }
            if (flagL && flagR) {
                char temp = chars[left];
                chars[left] = chars[right];
                chars[right] = temp;
                left++;
                right--;
            }
        }
        return new String(chars);
    }

    private boolean isVowels(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u'
                || c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U';
    }

    /**
     * 子符串
     */
    public boolean canConstruct(String ransomNote, String magazine) {
        if (magazine.length() < ransomNote.length()) return false;
        int[] caps = new int[26];
        for (char c : ransomNote.toCharArray()) {
            //从上个字符的位置找字符
            int index = magazine.indexOf(c, caps[c - 'a']);
            if (index == -1)
                return false;
            //记录当前字符的存在的index
            caps[c - 97] = index + 1;
        }
        return true;
    }

    /**
     * 字符串中的第一个唯一字符
     */
    public int firstUniqChar(String s) {
        Map<Integer, Long> map = s.chars()
                .boxed()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        for (int i = 0; i < s.length(); i++) {
            if (map.get((int) s.charAt(i)) == 1) {
                return i;
            }
        }
        return -1;
    }

    public char findTheDifference(String s, String t) {
        int result = 0;
        for (char c : s.toCharArray()) {
            result ^= c;
        }
        for (char c : t.toCharArray()) {
            result ^= c;
        }
        return (char) result;
    }

    public int countSegments(String s) {
        String trimmed = s.trim();
        if (trimmed.equals("")) {
            return 0;
        }
        return trimmed.split("\\s+").length;
    }

    /**
     * 括号的嵌套深度
     */
    public int[] maxDepthAfterSplit(String seq) {
        int[] ans = new int[seq.length()];
        int d = 0;
        for (int i = 0; i < seq.length(); i++) {
            //用栈，遇到(push( , 遇到）弹出一个（，depth为栈的长度 ,嵌套深度为技术的分配 给A，偶数的分配给B
            if (seq.charAt(i) == '(') {
                d += 1;
                ans[i] = d % 2;
            }
            if (seq.charAt(i) == ')') {
                ans[i] = d % 2;
                d -= 1;
            }
        }
        return ans;
    }

    /**
     * 最长有效括号
     */
    public int longestValidParentheses(String s) {
        int maxans = 0;
        int[] dp = new int[s.length()];
        for (int i = 1; i < s.length(); i++) {
            //如果遇到)开始记录
            if (s.charAt(i) == ')') {
                if (s.charAt(i - 1) == '(') {
                    //若前一个是（则,记录上一个长度+2
                    dp[i] = (i >= 2 ? dp[i - 2] : 0) + 2;
                    //若前一个不是（,则看看i-长度前一个
                } else if (i - dp[i - 1] > 0 && s.charAt(i - dp[i - 1] - 1) == '(') {
                    dp[i] = dp[i - 1] + ((i - dp[i - 1]) >= 2 ? dp[i - dp[i - 1] - 2] : 0) + 2;
                }
                maxans = Math.max(maxans, dp[i]);
            }
        }
        return maxans;
    }

    /**
     * 通配符匹配
     */
    public boolean isMatch(String s, String p) {
        int sLen = s.length(), pLen = p.length();
        //字符串相同或者p只为*，直接返回true
        if (p.equals(s) || p.equals("*")) return true;
        //两个字符串都为空，返回false
        if (p.isEmpty() || s.isEmpty()) return false;

        //初始化dp
        boolean[][] d = new boolean[pLen + 1][sLen + 1];
        //dp i:通配符字符串的长度，j：被匹配字符串的长度
        d[0][0] = true;

        for (int i = 1; i < pLen + 1; i++) {
            //p从第一位起,若是*
            if (p.charAt(i - 1) == '*') {
                int sIdx = 1;
                //*匹配到前一个匹配不到的位置
                while ((!d[i - 1][sIdx - 1]) && (sIdx < sLen + 1)) sIdx++;
                //将上一个位置的值设置成当前
                d[i][sIdx - 1] = d[i - 1][sIdx - 1];
                //后面的值设置为true，表示后面的都匹配
                while (sIdx < sLen + 1) d[i][sIdx++] = true;
            } else if (p.charAt(i - 1) == '?') {
                //若是？,则当前，为上一个值后移动一位
                System.arraycopy(d[i - 1], 0, d[i], 1, sLen + 1 - 1);
            } else {
                for (int sIdx = 1; sIdx < sLen + 1; sIdx++) {
                    d[i][sIdx] = d[i - 1][sIdx - 1] &&
                            (p.charAt(i - 1) == s.charAt(sIdx - 1));
                }
            }
        }
        return d[pLen][sLen];
    }

    /**
     * 字符串转数字
     */
    public int myAtoi(String str) {
        //去除首付空格
        char[] c = str.trim().toCharArray();
        if (c.length == 0) return 0;
        long res = 0;
        int i = 1, sign = 1;
        if (c[0] == '-') sign = -1;
        else if (c[0] != '+') i = 0;
        for (int j = i; j < c.length; j++) {
            if (c[j] < '0' || c[j] > '9') break;
            res = res * 10 + (c[j] - '0');
            if (res > Integer.MAX_VALUE) return sign == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        return sign * (int) res;
    }

    public boolean isScramble(String s1, String s2) {
        int m = s2.length();
        int n = s1.length();
        if (n != m) {
            return false;
        }
        boolean[][][] dp = new boolean[m][n][n + 1];
        // 初始化单个字符的情况
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j][1] = s1.charAt(i) == s2.charAt(j);
            }
        }
        // 枚举区间长度 2～n
        for (int len = 2; len <= n; len++) {
            // 枚举 S 中的起点位置
            for (int i = 0; i <= n - len; i++) {
                for (int j = 0; j <= n - len; j++) {
                    for (int k = 1; k <= len - 1; k++) {
                        // 第一种情况：S1 -> T1, S2 -> T2
                        if (dp[i][j][k] && dp[i + k][j + k][len - k]) {
                            dp[i][j][len] = true;
                            break;
                        }
                        // 第二种情况：S1 -> T2, S2 -> T1
                        // S1 起点 i，T2 起点 j + 前面那段长度 len-k ，S2 起点 i + 前面长度k
                        if (dp[i][j + len - k][k] && dp[i + k][j][len - k]) {
                            dp[i][j][len] = true;
                            break;
                        }
                    }
                }
            }
        }
        return dp[0][0][n];
    }

    /**
     * 数字转罗马数字
     */
    public String intToRoman(int num) {
        int[] nums = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] romans = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder sb = new StringBuilder();
        int index = 0;
        while (index < 13) {
            while (num >= nums[index]) {
                sb.append(romans[index]);
                num -= nums[index];
            }
            index++;
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
    }
}
