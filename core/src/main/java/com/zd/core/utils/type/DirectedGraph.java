package com.zd.core.utils.type;

import java.util.ArrayDeque;
import java.util.Queue;

public class DirectedGraph {


    public int[] findOrder(int numCourses, int[][] prerequisites) {
        if (numCourses == 0) {
            return new int[0];
        }

        //入度表
        int[] in = new int[numCourses];
        // 每有一组先决条件，则对应要学课程的先修课门数 +1 。
        for (int[] prerequisite : prerequisites) {
            in[prerequisite[0]]++;
        }
        // 将所有入度为 0 的课程加入队列，即不需要先修课就可以学习的课程。
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < in.length; i++) {
            if (in[i] == 0) {
                queue.offer(i);
            }
        }
        int count = 0;
        // 定义数组依次存可以学完的课程。
        int[] ans = new int[numCourses];
        while (!queue.isEmpty()) {
            //因为入度为0可以直接加入完成数组
            Integer cur = queue.poll();
            ans[count++] = cur;
            //遍历
            for (int[] prerequisite : prerequisites) {
                if (prerequisite[1] == cur) {
                    in[prerequisite[0]]--;
                    if (in[prerequisite[0]] == 0) queue.offer(prerequisite[0]);
                }
            }
        }
        return count == numCourses ? ans : new int[0];
    }

    public static void main(String[] args) {
        new DirectedGraph().findOrder(2, new int[][]{{1,0}});
    }
}
