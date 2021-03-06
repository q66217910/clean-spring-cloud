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
     * 是否是子树
     */
    public boolean isSubtree(TreeNode s, TreeNode t) {
        boolean a = false, b = false;
        if (isSameTree(s, t)) {
            return true;
        } else {
            if (s.left != null) {
                a = isSubtree(s.left, t);
            }
            if (s.right != null) {
                b = isSubtree(s.right, t);
            }
        }
        return a || b;
    }

    public TreeNode searchBST(TreeNode root, int val) {
        if (root == null) {
            return null;
        }
        if (root.val == val) {
            return root;
        }
        if (root.val > val) {
            return searchBST(root.left, val);
        }
        return searchBST(root.right, val);
    }

    public TreeNode insertIntoBST(TreeNode root, int val) {
        if (root == null) {
            root = new TreeNode(val);
        }
        if (root.val > val) {
            if (root.left != null) {
                insertIntoBST(root.left, val);
            } else {
                root.left = new TreeNode(val);
            }
        }
        if (root.val < val) {
            if (root.right != null) {
                insertIntoBST(root.right, val);
            } else {
                root.right = new TreeNode(val);
            }
        }
        return root;
    }

    private TreeNode pre;

    public TreeNode deleteNode(TreeNode root, int key) {
        if (root == null) return null;

        if (key > root.val) root.right = deleteNode(root.right, key);
        else if (key < root.val) root.left = deleteNode(root.left, key);
        else {
            if (root.left == null && root.right == null) root = null;
            else if (root.right != null) {
                root.val = successor(root);
                root.right = deleteNode(root.right, root.val);
            } else {
                root.val = predecessor(root);
                root.left = deleteNode(root.left, root.val);
            }
        }
        return root;
    }

    public int successor(TreeNode root) {
        root = root.right;
        while (root.left != null) root = root.left;
        return root.val;
    }


    public int predecessor(TreeNode root) {
        root = root.left;
        while (root.right != null) root = root.right;
        return root.val;
    }


    /**
     * 叶子节点相似
     */
    public boolean leafSimilar(TreeNode root1, TreeNode root2) {
        List<Integer> leaf1 = new ArrayList<>();
        List<Integer> leaf2 = new ArrayList<>();
        findLeaf(root1, leaf1);
        findLeaf(root2, leaf2);
        return leaf1.equals(leaf2);
    }

    /**
     * 寻找叶子节点
     */
    public void findLeaf(TreeNode node, List<Integer> list) {
        if (node != null) {
            if (node.right == null && node.left == null) {
                //子节点
                list.add(node.val);
            }
            findLeaf(node.left, list);
            findLeaf(node.right, list);
        }
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
            buildString(node.right, sb);
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

    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> list = new ArrayList<>();
        if (root == null) {
            return list;
        }
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        boolean flag = false;
        while (!queue.isEmpty()) {
            List<Integer> temp = new ArrayList<>();
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (flag) {
                    temp.add(0, node.val);
                } else {
                    temp.add(node.val);
                }
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
            list.add(temp);
            flag = !flag;
        }
        return list;
    }

    public int goodNodes(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return goodNodes(root.left, root.val) + goodNodes(root.right, root.val) + 1;
    }

    public int goodNodes(TreeNode node, int max) {
        if (node == null) {
            return 0;
        }
        int count = 0;
        if (node.val >= max) {
            count++;
        }
        return goodNodes(node.left, Math.max(max, node.val)) + goodNodes(node.right, Math.max(max, node.val)) + count;
    }

    public TreeNode increasingBST(TreeNode root) {
        List<Integer> vals = new ArrayList();
        inorderTraversal(root, vals);
        TreeNode ans = new TreeNode(0), cur = ans;
        for (int v : vals) {
            cur.right = new TreeNode(v);
            cur = cur.right;
        }
        return ans.right;
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

    private int maxSum = Integer.MIN_VALUE;

    public int maxGain(TreeNode node) {
        if (node == null) return 0;

        // max sum on the left and right sub-trees of node
        int left_gain = Math.max(maxGain(node.left), 0);
        int right_gain = Math.max(maxGain(node.right), 0);

        // the price to start a new path where `node` is a highest node
        int price_newpath = node.val + left_gain + right_gain;

        // update max_sum if it's better to start a new path
        maxSum = Math.max(maxSum, price_newpath);

        // for recursion :
        // return the max gain if continue the same path
        return node.val + Math.max(left_gain, right_gain);
    }

    public int maxPathSum(TreeNode root) {
        maxGain(root);
        return maxSum;
    }

    /**
     * 合并二叉树
     */
    public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
        if (t1 == null)
            return t2;
        if (t2 == null)
            return t1;
        t1.val += t2.val;
        t1.left = mergeTrees(t1.left, t2.left);
        t1.right = mergeTrees(t1.right, t2.right);
        return t1;
    }

    public int rangeSumBST(TreeNode root, int L, int R) {
        if (root == null) {
            return 0;
        }
        int sum = 0;
        if (root.val >= L && root.val <= R) {
            sum += root.val;
        }
        sum += rangeSumBST(root.right, L, R);
        sum += rangeSumBST(root.left, L, R);
        return sum;
    }

    public int sumRootToLeaf(TreeNode root) {
        return sumRootToLeaf(root, 0);
    }

    private int sumRootToLeaf(TreeNode root, int value) {
        if (root == null) {
            return 0;
        }
        int res = 0;
        value = (value << 1) + root.val;
        if (root.left == null && root.right == null) {
            //叶子节点
            return value;
        }
        res += sumRootToLeaf(root.left, value);
        res %= Math.pow(10, 9) + 7;
        res += sumRootToLeaf(root.right, value);
        res %= Math.pow(10, 9) + 7;
        return res;
    }

    private int sum = 0;

    public TreeNode convertBST(TreeNode root) {
        if (root != null) {
            convertBST(root.right);
            sum += root.val;
            root.val = sum;
            convertBST(root.left);
        }
        return root;
    }


    public int pseudoPalindromicPaths(TreeNode root) {
        return pseudoPalindromicPaths(root, new ArrayList<>());
    }

    public int pseudoPalindromicPaths(TreeNode root, List<Integer> list) {
        int count = 0;
        list.add(root.val);
        if (root.left == null && root.right == null) {
            //叶子节点,判断是否回文
            return isPalindrome(list) ? 1 : 0;
        }
        if (root.left != null) {
            count += pseudoPalindromicPaths(root.left, new ArrayList<>(list));
        }
        if (root.right != null) {
            count += pseudoPalindromicPaths(root.right, new ArrayList<>(list));
        }
        return count;
    }

    /**
     * 是否是回文串
     */
    public boolean isPalindrome(List<Integer> list) {
        return list.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .values().stream().filter(a -> a % 2 == 1).count() <= 1;
    }

    final class TreeInfo {
        public final int height;
        public final boolean balanced;

        public TreeInfo(int height, boolean balanced) {
            this.height = height;
            this.balanced = balanced;
        }
    }

    public boolean isUnivalTree(TreeNode root) {
        return isUnivalTree(root, root.val);
    }

    public boolean isUnivalTree(TreeNode node, int value) {
        if (node == null) {
            return true;
        }
        if (node.val != value) {
            return false;
        }
        return isUnivalTree(node.left, value) && isUnivalTree(node.right, value);
    }

    public TreeNode trimBST(TreeNode root, int L, int R) {
        if (root == null) return root;
        if (root.val > R) return trimBST(root.left, L, R);
        if (root.val < L) return trimBST(root.right, L, R);

        root.left = trimBST(root.left, L, R);
        root.right = trimBST(root.right, L, R);
        return root;
    }

    public List<Double> averageOfLevels(TreeNode root) {
        List<Double> res = new ArrayList<>();
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                list.add(node.val);
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
            res.add(list.stream().mapToDouble(Integer::doubleValue).average().orElse(0d));
        }
        return res;
    }

    int d = 0;

    public int findTilt(TreeNode root) {
        findTilt2(root);
        return d;
    }

    public int findTilt2(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = findTilt2(root.left);
        int right = findTilt2(root.right);
        d += Math.abs(right - left);
        return left + right + root.val;
    }

    public int minDiffInBST(TreeNode root) {
        List<Integer> list = inorderTraversal(root);
        List<Integer> collect = list.stream().sorted().collect(Collectors.toList());
        int min = Integer.MAX_VALUE;
        for (int i = 1; i < collect.size(); i++) {
            min = Math.min(min, Math.abs(collect.get(i) - collect.get(i - 1)));
        }
        return min;
    }

    public int findSecondMinimumValue(TreeNode root) {
        List<Integer> list = inorderTraversal(root);
        List<Integer> collect = list.stream().distinct().sorted().collect(Collectors.toList());
        return collect.stream().skip(1).findFirst().orElse(-1);
    }

    public int[] findMode(TreeNode root) {
        List<Integer> list = inorderTraversal(root);
        Map<Integer, Long> map = list.stream().collect(Collectors.groupingBy(Function.identity(),
                Collectors.counting()));
        Map<Long, List<Integer>> collect = map.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
        return collect.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseGet(ArrayList::new)
                .stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    public TreeNode recoverFromPreorder(String S) {
        Map<Integer, TreeNode> map = new HashMap<>();
        int num = 0;
        //处理root节点
        int start = 0;
        StringBuilder sb = new StringBuilder();
        for (start = 0; start < S.length(); start++) {
            if (S.charAt(start) == '-') {
                map.put(0, new TreeNode(Integer.parseInt(sb.toString())));
                sb = new StringBuilder();
                break;
            }
            sb.append(S.charAt(start));
        }

        if (start == S.length()) {
            return new TreeNode(Integer.parseInt(sb.toString()));
        }

        for (int i = start; i < S.length(); i++) {
            if (S.charAt(i) == '-') {
                num++;
            }
            if (Character.isDigit(S.charAt(i))) {
                sb.append(S.charAt(i));
            }
            if (i == S.length() - 1 || (S.charAt(i + 1) == '-' && Character.isDigit(S.charAt(i)))) {
                TreeNode node = new TreeNode(Integer.parseInt(sb.toString()));
                TreeNode lastNode = map.get(num - 1);
                if (lastNode != null) {
                    if (lastNode.left == null) {
                        lastNode.left = node;
                    } else {
                        lastNode.right = node;
                    }
                }
                map.put(num, node);
                num = 0;
                sb = new StringBuilder();
            }
        }
        return map.get(0);
    }

    public static void main(String[] args) {
        System.out.println(new TreeUtil().recoverFromPreorder("3"));
    }

    class BSTIterator {

        private List<Integer> result;

        private int index = 0;

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

        public BSTIterator(TreeNode root) {
            result = new LinkedList<>();
            inorderTraversal(root, result);
        }

        public int next() {
            return result.get(index++);
        }

        public boolean hasNext() {
            return index < result.size();
        }
    }

}
