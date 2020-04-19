package com.zd.core.utils.type;

import com.zd.core.utils.structure.NTree;

import java.util.*;
import java.util.function.Function;
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


    /**
     * 中序遍历
     */
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

    /**
     * 路劲数 ==sum
     */
    public int pathSum(TreeNode root, int sum) {
        // key是前缀和, value是大小为key的前缀和出现的次数
        Map<Integer, Integer> prefixSumCount = new HashMap<>();
        // 前缀和为0的一条路径
        prefixSumCount.put(0, 1);
        return recursionPathSum(root, prefixSumCount, sum, 0);
    }

    private int recursionPathSum(TreeNode node, Map<Integer, Integer> prefixSumCount, int target, int currSum) {
        // 1.递归终止条件
        if (node == null) {
            return 0;
        }
        // 2.本层要做的事情
        int res = 0;
        //当前路径的和
        currSum += node.val;
        //currSum-target相当于找路径的起点
        res += prefixSumCount.getOrDefault(currSum - target, 0);
        //更新路径上当前节点前缀和的个数
        prefixSumCount.put(currSum, prefixSumCount.getOrDefault(currSum, 0) + 1);

        res += recursionPathSum(node.left, prefixSumCount, target, currSum);
        res += recursionPathSum(node.right, prefixSumCount, target, currSum);

        //回到本层，恢复状态，去除当前节点的前缀和数量
        prefixSumCount.computeIfPresent(currSum, (k, v) -> --v);
        return res;
    }

    /**
     * 查找和为k
     */
    public boolean findTarget(TreeNode root, int k) {
        Set<Integer> set = new HashSet<>();
        return findTarget(root, k, set);
    }

    private boolean findTarget(TreeNode root, int k, Set<Integer> set) {
        if (root == null) return false;
        if (set.contains(k - root.val)) return true;
        set.add(root.val);
        return findTarget(root.left, k, set) || findTarget(root.right, k, set);
    }

    /**
     * 堂兄弟节点
     */
    public boolean isCousins(TreeNode root, int x, int y) {
        if (root == null) {
            return false;
        }
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Queue<TreeNode> newQueue = new ArrayDeque<>();
            List<Integer> list = new ArrayList<>();
            for (int i = queue.size(); i > 0; i--) {
                TreeNode cur = queue.poll();
                if (cur != null) {
                    if (cur.left != null) {
                        newQueue.add(cur.left);
                    }
                    if (cur.right != null) {
                        newQueue.add(cur.right);
                    }
                    list.add(cur.val);
                    if (cur.left != null && cur.right != null) {
                        if ((cur.left.val == x && cur.right.val == y) || cur.right.val == x && cur.left.val == y) {
                            return false;
                        }
                    }
                }
            }
            if (list.contains(x) && list.contains(y)) {
                return true;
            }
            queue = newQueue;
        }
        return false;
    }

    /**
     * 二叉搜索树第K大
     */
    public int kthLargest(TreeNode root, int k) {
        List<Integer> list = new ArrayList<>();
        treeAll(root, list);
        return list.stream().sorted(Comparator.reverseOrder()).skip(k - 1).findFirst().get();
    }

    private void treeAll(TreeNode node, List<Integer> list) {
        if (node != null) {
            list.add(node.val);
            treeAll(node.left, list);
            treeAll(node.right, list);
        }
    }

    public static void main(String[] args) {
        new TreeUtil().isCousins(new TreeNode().build(new int[]{1, 2, 3, 0, 4}), 2, 3);
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }

        TreeNode() {

        }

        TreeNode build(int[] arr) {
            return createBinaryTreeByArray(arr, 0);
        }

        private TreeNode createBinaryTreeByArray(int[] array, int index) {
            TreeNode tn = null;
            if (index < array.length) {
                int value = array[index];
                tn = new TreeNode(value);
                tn.left = createBinaryTreeByArray(array, 2 * index + 1);
                tn.right = createBinaryTreeByArray(array, 2 * index + 2);
                return tn;
            }
            return tn;
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
