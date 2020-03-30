package com.zd.core.utils.structure;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * N叉数
 */
public class NTree {

    /**
     * N叉树最大深度
     */
    public int maxDepth(Node root) {
        if (root == null) {
            return 0;
        }
        int result = 0;
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            result++;
            for (int size = queue.size(); size > 0; size--) {
                Node cur = queue.poll();
                if (cur != null && cur.children != null) {
                    queue.addAll(cur.children);
                }
            }
        }
        return result;
    }

    class Node {
        public int val;
        public List<Node> children;

        public Node() {
        }

        public Node(int val) {
            this.val = val;
        }

        public Node(int val, List<Node> children) {
            this.val = val;
            this.children = children;
        }
    }

}
