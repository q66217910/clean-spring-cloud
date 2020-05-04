package com.zd.core.utils.type;

import lombok.val;

import java.util.*;
import java.util.stream.Collectors;

public class LinkUtil {

    class ListNode {
        public int val;
        public ListNode prev;
        public ListNode next;
        public ListNode child;

        ListNode() {
            next = null;
        }

        ListNode(int x) {
            val = x;
            next = null;
        }
    }

    /**
     * 链表是否闭环
     */
    public boolean hasCycle(ListNode head) {
        Set<ListNode> set = new HashSet<>();
        while (head != null) {
            if (set.contains(head)) {
                return true;
            }
            set.add(head);
            head = head.next;
        }
        return false;
    }

    /**
     * 链表是否相交
     */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        Set<ListNode> set = new HashSet<>();
        while (headA != null) {
            set.add(headA);
            headA = headA.next;
        }
        while (headB != null) {
            if (set.contains(headB)) {
                return headB;
            }
            headB = headB.next;
        }
        return null;
    }

    public ListNode removeElements(ListNode head, int val) {
        ListNode cur = head;
        ListNode pre = null;
        while (cur != null) {
            if (cur.val == val) {
                if (pre != null) {
                    pre.next = cur.next;
                } else {
                    head = cur.next;
                }
            } else {
                pre = cur;
            }
            cur = cur.next;
        }
        return head;
    }

    /**
     * 回文链表
     */
    public boolean isPalindrome(ListNode head) {
        List<Integer> list = new ArrayList<>();

        while (head != null) {
            list.add(head.val);
            head = head.next;
        }
        for (int i = 0; i < list.size() / 2; i++) {
            if (!list.get(i).equals(list.get(list.size() - 1 - i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取链表中间节点
     */
    public ListNode middleNode(ListNode head) {
        List<ListNode> list = new ArrayList<>();
        while (head != null) {
            list.add(head);
            head = head.next;
        }
        return list.stream().skip(list.size() / 2).findFirst().get();
    }

    public void deleteNode(ListNode node) {
        if (node.next != null) {
            node.val = node.next.val;
            node.next = node.next.next;
        }
    }

    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode first = dummy;
        ListNode second = dummy;
        // Advances first pointer so that the gap between first and second is n nodes apart
        for (int i = 1; i <= n + 1; i++) {
            first = first.next;
        }
        // Move first to the end, maintaining the gap
        while (first != null) {
            first = first.next;
            second = second.next;
        }
        second.next = second.next.next;
        return dummy.next;
    }

    /**
     * 链表两数相加
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode(0);
        ListNode p = l1, q = l2, curr = dummyHead;
        int carry = 0;
        while (p != null || q != null) {
            int x = (p != null) ? p.val : 0;
            int y = (q != null) ? q.val : 0;
            int sum = carry + x + y;
            carry = sum / 10;
            curr.next = new ListNode(sum % 10);
            curr = curr.next;
            if (p != null) p = p.next;
            if (q != null) q = q.next;
        }
        if (carry > 0) {
            curr.next = new ListNode(carry);
        }
        return dummyHead.next;
    }

    public ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
        ListNode p = l1, q = l2, last = null, cur = null;
        long v1 = 0, v2 = 0, value = 0;
        while (p != null) {
            v1 = v1 * 10 + p.val;
            p = p.next;
        }
        while (q != null) {
            v2 = v2 * 10 + q.val;
            q = q.next;
        }
        value = v1 + v2;
        if (value == 0) {
            return new ListNode(0);
        }
        while (value > 0) {
            cur = new ListNode((int) value % 10);
            value /= 10;
            if (last == null) {
                last = cur;
            } else {
                cur.next = last;
                last = cur;
            }
        }
        return cur;
    }

    public ListNode removeDuplicateNodes(ListNode head) {
        //思路：冒泡排序的思想
        if (head == null) return null;
        ListNode first = head;
        while (first != null) {
            ListNode second = first;
            while (second.next != null) {
                if (second.next.val == first.val) {
                    second.next = second.next.next;
                } else {
                    second = second.next;
                }
            }
            first = first.next;
        }
        return head;
    }

    /**
     * 合并K个排序链表
     */
    public ListNode mergeKLists(ListNode[] lists) {
        List<Integer> list = new ArrayList<>();
        for (ListNode listNode : lists) {
            ListNode head = listNode;
            while (head != null) {
                list.add(head.val);
                head = head.next;
            }
        }
        list = list.stream().sorted().collect(Collectors.toList());
        ListNode head = null;
        ListNode pre = null;
        for (Integer value : list) {
            ListNode node = new ListNode(value);
            if (pre != null) {
                pre.next = node;
            } else {
                head = node;
            }
            pre = node;
        }
        return head;
    }

    /**
     * 二进制链表转数字
     */
    public int getDecimalValue(ListNode head) {
        int result = 0;
        while (head != null) {
            int value = head.val;
            if (value == 1) {
                result = (result << 1) + 1;
            } else {
                result = result << 1;
            }
            head = head.next;
        }
        return result;
    }

    /**
     * 倒序排k
     */
    public int kthToLast(ListNode head, int k) {
        List<Integer> list = new ArrayList<>();
        while (head != null) {
            list.add(head.val);
            head = head.next;
        }
        return list.get(list.size() - k);
    }

    public ListNode getKthFromEnd(ListNode head, int k) {
        List<ListNode> list = new ArrayList<>();
        while (head != null) {
            list.add(head);
            head = head.next;
        }
        return list.get(list.size() - k);
    }

    public int[] reversePrint(ListNode head) {
        List<Integer> list = new ArrayList<>();
        while (head != null) {
            list.add(head.val);
            head = head.next;
        }
        Collections.reverse(list);
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * 链表节点两两交换
     */
    public ListNode swapPairs(ListNode head) {
        //当前节点和下一个节点不为null
        if ((head == null) || (head.next == null)) {
            return head;
        }
        //两个节点记录
        ListNode firstNode = head;
        ListNode secondNode = head.next;
        //交换
        firstNode.next = swapPairs(secondNode.next);
        secondNode.next = firstNode;
        return secondNode;
    }

    /**
     * 反转链表
     */
    public ListNode reverseList(ListNode head) {
        ListNode pre = null;
        ListNode cur = head;
        while (cur != null) {
            ListNode temp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = temp;
        }
        return pre;
    }

    /**
     * 合并两个有序数组
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

    public ListNode detectCycle(ListNode head) {
        Set<ListNode> visited = new HashSet<ListNode>();

        ListNode node = head;
        while (node != null) {
            if (visited.contains(node)) {
                return node;
            }
            visited.add(node);
            node = node.next;
        }

        return null;
    }

    public ListNode oddEvenList(ListNode head) {
        if (head == null) return null;
        ListNode odd = head, even = head.next, evenHead = even;
        while (even != null && even.next != null) {
            odd.next = even.next;
            odd = odd.next;
            even.next = odd.next;
            even = even.next;
        }
        odd.next = evenHead;
        return head;
    }

    public ListNode flatten(ListNode head) {
        if (head == null) return head;

        ListNode pseudoHead = new ListNode(0);
        ListNode curr, prev = pseudoHead;

        Deque<ListNode> stack = new ArrayDeque<>();
        stack.push(head);

        while (!stack.isEmpty()) {
            curr = stack.pop();
            prev.next = curr;
            curr.prev = prev;

            if (curr.next != null) stack.push(curr.next);
            if (curr.child != null) {
                stack.push(curr.child);
                // don't forget to remove all child pointers.
                curr.child = null;
            }
            prev = curr;
        }
        // detach the pseudo node from the result
        pseudoHead.next.prev = null;
        return pseudoHead.next;
    }

    /**
     * 旋转链表
     */
    public ListNode rotateRight(ListNode head, int k) {
        if (head == null) {
            return null;
        }
        if (head.next == null) {
            return head;
        }
        //将链表变成环
        ListNode oldTail = head;
        //n为链表长度
        int n;
        for (n = 1; oldTail.next != null; n++) {
            oldTail = oldTail.next;
        }
        oldTail.next = head;


        //k%n 是要移动多少
        ListNode newTail = head;
        for (int i = 0; i < n - (k % n) - 1; i++) {
            newTail = newTail.next;
        }
        ListNode newHead = newTail.next;
        //中断环
        newTail.next = null;
        return newHead;
    }
}
