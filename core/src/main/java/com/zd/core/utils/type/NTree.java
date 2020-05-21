package com.zd.core.utils.type;

import java.util.ArrayList;
import java.util.List;

public class NTree {

    class Node {
        public int val;
        public List<Node> children;

        public Node() {
        }

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, List<Node> _children) {
            val = _val;
            children = _children;
        }
    }

    /**
     * 后序遍历
     */
    public List<Integer> postorder(Node root) {
        List<Integer> result = new ArrayList<>();
        postorderTraversal(root, result);
        return result;
    }

    private void postorderTraversal(Node node, List<Integer> list) {
        if (node != null) {
            if (node.children != null) {
                for (int i = 0; i < node.children.size(); i++) {
                    postorderTraversal(node.children.get(i), list);
                }
            }
            list.add(node.val);
        }
    }

    public List<Integer> preorder(Node root) {
        List<Integer> result = new ArrayList<>();
        preorderTraversal(root, result);
        return result;
    }

    private void preorderTraversal(Node node, List<Integer> list) {
        if (node != null) {
            list.add(node.val);
            if (node.children != null) {
                for (int i = 0; i < node.children.size(); i++) {
                    preorderTraversal(node.children.get(i), list);
                }
            }
        }
    }
}
