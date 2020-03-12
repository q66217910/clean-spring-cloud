package com.zd.core.utils.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TreeUtil {

    /**
     * 判断两棵树是否相同
     */
    public boolean isSameTree(TreeNode p, TreeNode q) {
        return same(p, q);
    }

    private boolean same(TreeNode p, TreeNode q) {
        return (p == null && q == null) ||
                (p != null && q != null
                        && p.val == q.val
                        && same(p.left, q.left)
                        && same(p.right, q.right));
    }

    private boolean sameSymmetric(TreeNode p, TreeNode q) {
        return (p == null && q == null) ||
                (p != null && q != null
                        && p.val == q.val
                        && same(p.right, q.left)
                        && same(p.left, q.right));
    }


    /**
     * 判断树是否对称
     */
    public boolean isSymmetric(TreeNode root) {
        return root == null || sameSymmetric(root.left, root.right);
    }

    int result = 0;

    /**
     * 最大深度
     */
    public int maxDepth(TreeNode root) {
        return depth(root);
    }

    private int depth(TreeNode node) {
        return node == null ? 0 : Math.max(result, Math.max(depth(node.left) + 1, depth(node.right) + 1));
    }

    Map<Integer, List<Integer>> map = new HashMap<>();
    


    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

}
