package com.zd.core.utils.type;

import com.google.common.collect.ImmutableMap;

import java.util.Stack;

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
        if(needle.equals("")) {
            return 0;
        }
        if(haystack.equals("")) {
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
        if(n==1){
            return "1";
        }
        return say(countAndSay(n-1));
    }

    private String say(String s){
        //记录数值出现的次数
        int count=1;
        //当前的数值
        char num = s.charAt(0);
        StringBuilder sb = new StringBuilder();
        for(int i =1;i<s.length();i++){
            char c = s.charAt(i);
            if(c==num){
                count++;
            }else{
                sb.append(count);
                sb.append(num);
                num = c;
                count=1;
            }
        }
        sb.append(count);
        sb.append(num);
        return sb.toString();
    }

}
