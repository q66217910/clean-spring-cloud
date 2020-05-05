package com.zd.core.utils.type;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.*;

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
        int i = 0, count = 0;
        for (int j = 0; j < nums.length; j++) {
            if (nums[j] != nums[i]) {
                count = 1;
            } else {
                count++;
            }
            if (count <= 2) {
                i++;
                nums[i] = nums[j];
            }
        }
        return i + 1;
    }

    public int removeDuplicates2(int[] nums) {
        if (nums.length == 0) return 0;
        int i = 1, count = 1;
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[j - 1]) {
                count = 1;
            } else {
                count++;
            }
            if (count <= 2) {
                nums[i++] = nums[j];
            }
        }
        return i;
    }

    public void sortColors(int[] nums) {
        Arrays.sort(nums);
    }

    public int findKthLargest(int[] nums, int k) {
        return Arrays.stream(nums)
                .boxed()
                .sorted(Comparator.<Integer>naturalOrder().reversed())
                .skip(k-1)
                .findFirst()
                .orElse(0);

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

    public List<Integer> getRow(int rowIndex) {
        return generate(rowIndex).get(rowIndex - 1);
    }

    /**
     * 生成杨辉三角
     */
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> result = new ArrayList<>();
        if (numRows == 0) {
            return result;
        }
        List<Integer> last = new ArrayList<>();
        last.add(1);
        result.add(last);
        if (numRows == 1) {
            return result;
        }
        for (int i = 1; i < numRows; i++) {
            List<Integer> list = new ArrayList<>();
            list.add(1);
            list.add(1);
            for (int j = 1; j < last.size(); j++) {
                list.add(j, last.get(j - 1) + last.get(j));
            }
            last = list;
            result.add(list);
        }
        return result;
    }

    /**
     * 重复数
     */
    public boolean containsDuplicate(int[] nums) {
        return Arrays.stream(nums).distinct().count() < nums.length;
    }

    public boolean containsNearbyDuplicate(int[] nums, int k) {
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            if (set.contains(nums[i])) {
                return true;
            }
            set.add(nums[i]);
            if (set.size() > k) {
                set.remove(nums[i - k]);
            }
        }
        return false;
    }

    /**
     * 连续数组缺失数字
     */
    public int missingNumber(int[] nums) {
        int[] result = new int[2 * nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            result[i + nums.length] = i;
            result[i] = nums[i];
        }
        result[2 * nums.length] = nums.length;
        int s = 0;
        for (int i = 0; i < result.length; i++) {
            s ^= result[i];
        }
        return s;
    }

    public void moveZeroes(int[] nums) {
        for (int lastNonZeroFoundAt = 0, cur = 0; cur < nums.length; cur++) {
            if (nums[cur] != 0) {
                int i = lastNonZeroFoundAt++;
                int temp = nums[i];
                nums[i] = nums[cur];
                nums[cur] = temp;
            }
        }
    }


    /**
     * 数组+1
     */
    public int[] plusOne(int[] digits) {
        for (int i = digits.length - 1; i >= 0; i--) {
            digits[i]++;
            digits[i] = digits[i] % 10;
            if (digits[i] != 0) return digits;
        }
        digits = new int[digits.length + 1];
        digits[0] = 1;
        return digits;
    }

    /**
     * 给定两个二进制字符串，返回他们的和（用二进制表示）。
     */
    public String addBinary(String a, String b) {
        BigInteger x = new BigInteger(a, 2);
        BigInteger y = new BigInteger(b, 2);
        BigInteger zero = new BigInteger("0", 2);
        BigInteger carry, answer;
        while (y.compareTo(zero) != 0) {
            answer = x.xor(y);
            carry = x.and(y).shiftLeft(1);
            x = answer;
            y = carry;
        }
        return x.toString(2);
    }

    /**
     * 将数组分成和相等的三个部分
     */
    public boolean canThreePartsEqualSum(int[] A) {
        //取和
        int sum = Arrays.stream(A).reduce(Integer::sum).orElse(0);
        //若和不是3的倍数则不可能分
        if (sum % 3 == 0) {
            int num = sum / 3;
            int value = 0;
            int count = 0;
            for (int item : A) {
                //寻找索引i,j
                value += item;
                if (value == num) {
                    value = 0;
                    count++;
                }
            }
            if (count >= 3) {
                return true;
            }
        }
        return false;
    }

    /**
     * 数组交集(一个数出现一次)
     */
    public int[] intersection(int[] nums1, int[] nums2) {
        HashSet<Integer> set = new HashSet<>();
        for (int num : nums1) set.add(num);
        ArrayList<Integer> arr = new ArrayList<>();
        for (int num : nums2) {
            if (set.contains(num)) {
                arr.add(num);
                set.remove(num);
            }
        }
        int[] res = new int[arr.size()];
        for (int i = 0; i < arr.size(); i++) res[i] = arr.get(i);
        return res;
    }


    /**
     * 矩阵90度旋转
     */
    public void rotate(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < (n + 1) / 2; i++) {
            for (int j = 0; j < n / 2; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[n - j - 1][i];
                matrix[n - j - 1][i] = matrix[n - i - 1][n - j - 1];
                matrix[n - i - 1][n - j - 1] = matrix[j][n - i - 1];
                matrix[j][n - i - 1] = temp;
            }
        }
    }

    public static void main(String[] args) {
        new CollectionUtil().canThreePartsEqualSum(new int[]{10, -10, 10, -10, 10, -10, 10, -10});
    }

}
