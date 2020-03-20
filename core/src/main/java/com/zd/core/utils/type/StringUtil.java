package com.zd.core.utils.type;

import com.google.common.collect.ImmutableMap;

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

    public class KMP {
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

    public static void main(String[] args) {
        System.out.println(new StringUtil().isIsomorphic("aba", "baa"));
    }
}
