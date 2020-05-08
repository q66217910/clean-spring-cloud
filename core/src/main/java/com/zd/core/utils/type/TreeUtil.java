package com.zd.core.utils.type;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 树的遍历
 * 前序：  父->左->右
 * 中序：  左->父->右
 * 后序：   左->右 ->父
 */
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
                        && sameSymmetric(p.right, q.left)
                        && sameSymmetric(p.left, q.right));
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

    /**
     * 自下而上 右->左
     */
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
     * 自下而上 左->右
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
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
                    result.add(tempList);
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
     * (搜索二叉树)
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
     * 最近共同父节点
     * (二叉树)
     */
    public TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
        //记录每一个节点的父节点
        Map<TreeNode, TreeNode> parent = new HashMap<>();
        Queue<TreeNode> queue = new ArrayDeque<>();
        parent.put(root, null);
        queue.add(root);
        //遍历所有节点
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node.right != null) {
                queue.add(node.right);
                parent.put(node.right, node);
            }
            if (node.left != null) {
                queue.add(node.left);
                parent.put(node.left, node);
            }
        }
        //查找q的父节点链
        Set<TreeNode> set = new HashSet<>();
        while (q != null) {
            set.add(q);
            q = parent.get(q);
        }
        //p的父节点链中是否与q的相同
        while (!set.contains(p)) {
            set.add(p);
            p = parent.get(p);
        }
        return p;
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

    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        Queue<TreeNode> stack = new ArrayDeque<>();
        if (root != null) {
            stack.add(root);
        }
        int k = 1;
        while (true) {
            Queue<TreeNode> temp = new ArrayDeque<>();
            while (!stack.isEmpty()) {
                TreeNode node = stack.poll();
                if (list.size() < k) {
                    list.add(node.val);
                }
                if (node.right != null) {
                    temp.add(node.right);
                }
                if (node.left != null) {
                    temp.add(node.left);
                }
            }
            k++;
            if (temp.isEmpty()) {
                break;
            }
            stack = temp;
        }
        return list;
    }

    /**
     * 生成不同的二叉搜索树
     */
    public List<TreeNode> generateTrees(int n) {
        if (n == 0) {
            return new ArrayList<>();
        }
        return generateTrees(1, n);
    }

    private List<TreeNode> generateTrees(int start, int end) {
        List<TreeNode> all = new LinkedList<>();
        if (start > end) {
            all.add(null);
            return all;
        }
        for (int i = start; i <= end; i++) {
            //生成左子树
            List<TreeNode> leftTree = generateTrees(start, i - 1);
            //生成右子树
            List<TreeNode> rightTree = generateTrees(i + 1, end);

            for (TreeNode left : leftTree) {
                for (TreeNode right : rightTree) {
                    TreeNode cur = new TreeNode(i);
                    cur.left = left;
                    cur.right = right;
                    all.add(cur);
                }
            }
        }
        return all;
    }

    /**
     * 前序遍历
     */
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        preorderTraversal(root, result);
        return result;
    }

    public void preorderTraversal(TreeNode node, List<Integer> list) {
        if (node != null) {
            list.add(node.val);
            if (node.left != null) {
                preorderTraversal(node.left, list);
            }
            if (node.right != null) {
                preorderTraversal(node.right, list);
            }
        }
    }

    /**
     * 中序遍历
     */
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        inorderTraversal(root, result);
        return result;
    }

    public void inorderTraversal(TreeNode node, List<Integer> list) {
        if (node != null) {
            if (node.left != null) {
                inorderTraversal(node.left, list);
            }
            list.add(node.val);
            if (node.right != null) {
                inorderTraversal(node.right, list);
            }
        }
    }

    /**
     * 后序遍历
     */
    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        postorderTraversal(root, result);
        return result;
    }

    public void postorderTraversal(TreeNode node, List<Integer> list) {
        if (node != null) {
            if (node.left != null) {
                postorderTraversal(node.left, list);
            }
            if (node.right != null) {
                postorderTraversal(node.right, list);
            }
            list.add(node.val);
        }
    }


    /**
     * 是否是二叉搜索树
     */
    public boolean isValidBST(TreeNode root) {
        return isValidBST(root, null, null);
    }

    private boolean isValidBST(TreeNode node, Integer lower, Integer upper) {
        if (node == null) {
            return true;
        }
        if (lower != null && node.val <= lower) {
            return false;
        }
        if (upper != null && node.val >= upper) {
            return false;
        }
        if (!isValidBST(node.right, node.val, upper)) {
            return false;
        }
        if (!isValidBST(node.left, lower, node.val)) {
            return false;
        }
        return true;
    }

    /**
     * 根据中序和后序遍历生成二叉树
     */
    public TreeNode buildTree(int[] inorder, int[] postorder) {
        AtomicInteger i = new AtomicInteger();
        //中序遍历值与索引
        Map<Integer, Integer> indexMap = Arrays.stream(inorder)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), v -> i.getAndIncrement()));
        return buildTree(postorder, indexMap, 0, postorder.length - 1, i);
    }

    private TreeNode buildTree(int[] postorder, Map<Integer, Integer> indexMap, int left, int right, AtomicInteger ac) {
        if (left > right) {
            return null;
        }
        int rootIndex = ac.decrementAndGet();
        //后序遍历的最后一位为跟节点
        int rootValue = postorder[rootIndex];
        TreeNode root = new TreeNode(rootValue);
        //获取中序遍历的root节点
        int index = indexMap.get(rootValue);
        //创建右节点，中序遍历root节点右侧
        root.right = buildTree(postorder, indexMap, index + 1, right, ac);
        //创建左节点，中序遍历root节点左侧
        root.left = buildTree(postorder, indexMap, left, index - 1, ac);
        return root;
    }

    /**
     * 根据中序和前序遍历生成二叉树
     */
    public TreeNode buildTree2(int[] inorder, int[] preorder) {
        AtomicInteger i = new AtomicInteger();
        //中序遍历值与索引
        Map<Integer, Integer> indexMap = Arrays.stream(inorder)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), v -> i.getAndIncrement()));
        return buildTree2(preorder, indexMap, 0, preorder.length, new AtomicInteger());
    }

    private TreeNode buildTree2(int[] preorder, Map<Integer, Integer> indexMap, int left, int right, AtomicInteger ac) {
        if (left == right) {
            return null;
        }
        int rootIndex = ac.getAndIncrement();
        //后序遍历的最后一位为跟节点
        int rootValue = preorder[rootIndex];
        TreeNode root = new TreeNode(rootValue);
        //获取中序遍历的root节点
        int index = indexMap.get(rootValue);
        //创建左节点，中序遍历root节点左侧
        root.left = buildTree2(preorder, indexMap, left, index, ac);
        //创建右节点，中序遍历root节点右侧
        root.right = buildTree2(preorder, indexMap, index + 1, right, ac);
        return root;
    }

    /**
     * 填充每个节点的下一个右侧节点指针
     * (完美二叉树)
     */
    public TreeNode connect(TreeNode root) {
        if (root == null) {
            return root;
        }
        //广度优先算法
        Queue<TreeNode> stack = new ArrayDeque<>();
        stack.add(root);
        while (!stack.isEmpty()) {
            int size = stack.size();
            TreeNode pre = null;
            for (int i = 0; i < size; i++) {
                TreeNode node = stack.poll();
                //设置当前节点的右侧节点
                node.next = pre;
                //把当前节点设置
                pre = node;
                if (node.right != null) {
                    stack.add(node.right);
                }
                if (node.left != null) {
                    stack.add(node.left);
                }
            }
        }
        return root;
    }

    private static final String spliter = ",";
    private static final String NN = "X"; //当做 null

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        buildString(root, sb);
        return sb.toString();
    }

    private void buildString(TreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append(NN).append(spliter);
        } else {
            sb.append(node.val).append(spliter);
            buildString(node.left, sb);
            buildString(node.right,sb);
        }
    }
    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        Deque<String> nodes = new LinkedList<>();
        nodes.addAll(Arrays.asList(data.split(spliter)));
        return buildTree(nodes);
    }

    private TreeNode buildTree(Deque<String> nodes) {
        String val = nodes.remove();
        if (val.equals(NN)) return null;
        else {
            TreeNode node = new TreeNode(Integer.valueOf(val));
            node.left = buildTree(nodes);
            node.right = buildTree(nodes);
            return node;
        }
    }


    public static void main(String[] args) {
        new TreeUtil().buildTree(new int[]{1, 2}, new int[]{2, 1});
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode next;

        TreeNode(int x) {
            val = x;
        }

        public int getVal() {
            return val;
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

    Map<String, Integer> count;
    List<TreeNode> ans;

    public List<TreeNode> findDuplicateSubtrees(TreeNode root) {
        count = new HashMap();
        ans = new ArrayList();
        collect(root);
        return ans;
    }

    public String collect(TreeNode node) {
        if (node == null) return "#";
        String serial = node.val + "," + collect(node.left) + "," + collect(node.right);
        count.put(serial, count.getOrDefault(serial, 0) + 1);
        if (count.get(serial) == 2)
            ans.add(node);
        return serial;
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
