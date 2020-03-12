package com.zd.core.utils.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CollectionUtil {

    class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    /**
     * 合并两个有序链表
     */
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        //创建一个新的链表
        ListNode prehead = new ListNode(-1);
        ListNode prev = prehead;
        //迭代
        while (l1 != null && l2 != null) {
            //把前置节点的next指向小的节点，并把当前链表向后移动
            if (l1.val <= l2.val) {
                prev.next = l1;
                l1 = l1.next;
            } else {
                prev.next = l2;
                l2 = l2.next;
            }
            //新链表下个节点
            prev = prev.next;
        }
        //加入最后一个节点
        prev.next = l1 == null ? l2 : l1;
        //返回时去掉跟节点
        return prehead.next;
    }


    /**
     * 排序数组，删除重复元素并返回新数组的长度
     * <p>
     * 原地算法：原地算法（in-place algorithm）是一种使用小的，固定数量的额外之空间来转换资料的算法。当算法执行时，输入的资料通常会被要输出的部份覆盖掉。
     * <p>
     * 双指针法：
     */
    public static int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        int i = 0;
        for (int j = 0; j < nums.length; j++) {
            if (nums[j] != nums[i]) {
                i++;
                nums[i] = nums[j];
            }
        }
        return i + 1;
    }

    /**
     * 移除元素
     * <p>
     * 双指针法：
     */
    public static int removeElement(int[] nums, int val) {
        if (nums.length == 0) return 0;
        int i = 0;
        for (int j = 0; j < nums.length; j++) {
            if (nums[j] != val) {
                nums[i] = nums[j];
                i++;
            }
        }
        return i;
    }

    /**
     * 二分查找法
     */
    public static int searchInsert(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = (right + left) >> 1;
            if (nums[mid] == target) {
                // 相关逻辑
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        // 相关返回值
        return left;
    }

    /**
     * 最大和连续子集
     */
    public int maxSubArray(int[] nums) {
        int n = nums.length, maxSum = nums[0];
        for (int i = 1; i < n; ++i) {
            if (nums[i - 1] > 0) {
                nums[i] += nums[i - 1];
            }
            maxSum = Math.max(nums[i], maxSum);
        }
        return maxSum;
    }

    /**
     * 数组顺序最大差值
     */
    public int maxProfit(int[] prices) {
        int last = 0, profit = 0;
        for (int i = 0; i < prices.length - 1; i++) {
            last = Math.max(0, prices[i + 1] + last - prices[i]);
            profit = Math.max(profit, last);
        }
        return profit;
    }

    /**
     * 合并两个数组
     */
    public void merge(int[] A, int m, int[] B, int n) {
        int a = 0, b = 0;
        if (B.length == 0) {
            return;
        }
        for (int i = 0; i < m + n; i++) {
            if (b > n - 1) {
                break;
            }
            if (A[a + b] > B[b] || a == m) {
                System.arraycopy(A, i, A, i + 1, A.length - i - 1);
                A[i] = B[b];
                b++;
            } else {
                a++;
            }
        }
    }

    /**
     * 输出和target的连续正整数
     */
    public int[][] findContinuousSequence(int target) {
        int i = 1;
        List<int[]> list = new ArrayList<>();
        while (target > 0) {
            target -= i++;
            if (target > 0 && target % i == 0) {
                int[] array = new int[i];
                for (int k = target / i, j = 0; k < target / i + i; k++, j++) {
                    array[j] = k;
                }
                list.add(array);
            }
        }
        Collections.reverse(list);
        return list.toArray(new int[0][]);
    }

    public static void main(String[] args) {
        new CollectionUtil().findContinuousSequence(17);
    }
}
