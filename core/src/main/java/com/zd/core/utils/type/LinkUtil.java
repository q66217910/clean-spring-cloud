package com.zd.core.utils.type;

import lombok.val;

import java.util.*;
import java.util.stream.Collectors;

public class LinkUtil {

    static class ListNode {
        int val;
        ListNode next;

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
        node.val = node.next.val;
        node.next = node.next.next;
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
        if (value==0){
            return new ListNode(0);
        }
        while (value > 0) {
            cur = new ListNode((int)value % 10);
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

    public static void main(String[] args) {
        ListNode l1 = new ListNode(3);
        l1.next = new ListNode(9);
        l1.next.next = new ListNode(9);
        l1.next.next.next = new ListNode(9);
        ListNode l2 = new ListNode(7);
        new LinkUtil().addTwoNumbers2(l1, l2);
    }
}
