package com.zd.core.utils.type;

import java.util.*;
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
     * 判断是否为平衡二叉树
     */
    public boolean isBalanced(TreeNode root) {
        return isBalancedTreeHelper(root).balanced;
    }

    private TreeInfo isBalancedTreeHelper(TreeNode root) {
        if (root == null) {
            return new TreeInfo(-1, true);
        }
        TreeInfo left = isBalancedTreeHelper(root.left);
        if (!left.balanced) {
            return new TreeInfo(-1, false);
        }
        TreeInfo right = isBalancedTreeHelper(root.right);
        if (!right.balanced) {
            return new TreeInfo(-1, false);
        }
        if (Math.abs(left.height - right.height) < 2) {
            return new TreeInfo(Math.max(left.height, right.height) + 1, true);
        }
        return new TreeInfo(-1, false);
    }


    /**
     * 最小深度
     */
    public int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        if (root.left == null && root.right == null) {
            return 1;
        }
        int minDepth = Integer.MAX_VALUE;
        if (root.left != null) {
            minDepth = Math.min(minDepth(root.left), minDepth);
        }
        if (root.right != null) {
            minDepth = Math.min(minDepth(root.right), minDepth);
        }
        return minDepth + 1;
    }


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

    public List<List<Integer>> levelOrderBottom(TreeNode root) {
        if (root == null) {
            return new ArrayList<>();
        }
        List<List<Integer>> result = new LinkedList<>();
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        queue.add(null);
        LinkedList<Integer> tempList = null;
        while (!queue.isEmpty()) {
            TreeNode node = queue.removeFirst();
            if (node != null) {
                if (tempList == null) {
                    tempList = new LinkedList<>();
                }
                tempList.addLast(node.val);
                if (node.left != null) {
                    queue.addLast(node.left);
                }
                if (node.right != null) {
                    queue.addLast(node.right);
                }
            } else {
                if (tempList != null) {
                    ((LinkedList<List<Integer>>) result).addFirst(tempList);
                    tempList = null;
                    queue.addLast(null);
                }
            }
        }
        return result;
    }


    public TreeNode sortedArrayToBST(int[] nums) {
        return helper(0, nums.length - 1, nums);
    }

    public TreeNode helper(int left, int right, int[] nums) {
        if (left > right) {
            return null;
        }
        int p = (left + right) / 2;
        TreeNode root = new TreeNode(nums[p]);
        root.left = helper(left, p - 1, nums);
        root.right = helper(p + 1, right, nums);
        return root;
    }

    public boolean hasPathSum(TreeNode root, int sum) {
        if (root == null) {
            return false;
        }
        sum -= root.val;
        if (root.right == null && root.left == null) {
            return sum == 0;
        }
        return hasPathSum(root.left, sum) || hasPathSum(root.right, sum);
    }

    /**
     * 完美二叉树
     */
    public TreeNode invertTree(TreeNode root) {
        if (root != null) {
            TreeNode left = root.left;
            TreeNode right = root.right;
            root.right = invertTree(left);
            root.left = invertTree(right);
        }
        return root;
    }

    /**
     * 最近共同父节点
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        int pVal = p.val;
        int qVal = q.val;
        while (root != null) {
            if (pVal > root.val && qVal > root.val) {
                root = root.right;
            } else if (pVal < root.val && qVal < root.val) {
                root = root.left;
            } else {
                return root;
            }
        }
        return null;
    }

    /**
     * 二叉树所有路径
     */
    public List<String> binaryTreePaths(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root != null) {
            result.addAll(treePaths(root, new ArrayList<>()));
        }
        return result.stream()
                .map(a -> a.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("->")))
                .collect(Collectors.toList());
    }

    private List<List<Integer>> treePaths(TreeNode node, List<Integer> list) {
        List<List<Integer>> result = new ArrayList<>();
        if (node == null) {
            result.add(list);
            return result;
        }
        list.add(node.val);
        if (node.right == null && node.left == null) {
            result.add(list);
            return result;
        }
        if (node.left != null) {
            result.addAll(treePaths(node.left, new ArrayList<>(list)));
        }
        if (node.right != null) {
            result.addAll(treePaths(node.right, new ArrayList<>(list)));
        }
        return result;
    }

    /**
     * 左叶子之和
     */
    public int sumOfLeftLeaves(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return findLeftLeaves(root, false);
    }

    /**
     * 寻找左节点
     */
    public int findLeftLeaves(TreeNode node, boolean isLeft) {
        int result = 0;
        if (node.left != null) {
            result += findLeftLeaves(node.left, true);
        }
        if (node.right != null) {
            result += findLeftLeaves(node.right, false);
        }
        if (node.right == null && node.left == null && isLeft) {
            return node.val;
        }
        return result;
    }

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    final class TreeInfo {
        public final int height;
        public final boolean balanced;

        public TreeInfo(int height, boolean balanced) {
            this.height = height;
            this.balanced = balanced;
        }
    }

}
