package com.zd.core.utils.type;

public class BTree<K extends Comparable<K>, V> {

    private Node<K, V> root = new LeafNode<K, V>();

    /**
     * 非叶子节点节点数量
     */
    private static final int M = 4;

    public V find(K key) {
        if (root != null) return root.find(key);
        return null;
    }

    public void insert(K key, V value) {
        Node<K, V> t = this.root.insert(key, value);
        //只有非叶子节点插入，并且直接插入才会返回非null
        if (t != null) {
            this.root = t;
        }
    }

    class Entry<K extends Comparable<K>, V> {

        private K key;

        private V value;

        private Node<K, V> node;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public Entry(Entry<K, V> entry, Node node) {
            this.key = entry.key;
            this.value = entry.value;
            this.node = node;
        }
    }

    /**
     * 节点抽象类（节点分两种，叶子节点和非叶子节点）
     */
    abstract class Node<K extends Comparable<K>, V> {

        protected Node<K, V> parent;//父节点
        protected Entry<K, V>[] entries; //键
        protected int keyNum;//键的数量

        abstract V find(K key);//查询

        abstract Node<K, V> insert(K key, V value);//插入

        protected Node<K, V> insertNode(Node<K, V> node1, Node<K, V> node2, K key) {
            return null;
        }
    }

    /**
     * 非叶子节点，只用来索引不包含数据
     */
    class BPlusNode<K extends Comparable<K>, V> extends Node<K, V> {

        /**
         * 递归查询,只为确定落在哪个区间
         * 找到叶子节点会调用叶子节点的find方法
         */
        @Override
        V find(K key) {
            for (int i = 0; i < this.keyNum; i++) {
                if (key.compareTo(this.entries[i].key) <= 0) {
                    return this.entries[i].node.find(key);
                }
            }
            return null;
        }

        /**
         * 递归查询,只为确定落在哪个区间
         * 在该区间插入
         */
        @Override
        Node<K, V> insert(K key, V value) {
            for (int i = 0; i < this.keyNum; i++) {
                if (key.compareTo(this.entries[i].key) <= 0) {
                    return this.entries[i].node.insert(key, value);
                }
            }
            return this.entries[keyNum - 1].node.insert(key, value);
        }

        /**
         * 当叶子节点，节点数超过m个，分裂结束后，递归向父节点插入元素
         */
        protected Node<K, V> insertNode(Node<K, V> node1, Node<K, V> node2, K key) {
            K oldKey = null;//原始key
            if (this.keyNum > 0) oldKey = this.entries[this.keyNum - 1].key;
            if (key == null || this.keyNum == 0) {
                //空节点，直接插入两个节点
                this.entries[0] = new Entry<>(node1.entries[node1.keyNum - 1], node1);
                this.entries[1] = new Entry<>(node1.entries[node1.keyNum - 1], node2);
                this.keyNum += 2;
                return this;
            }
            //当前节点为非空节点
            int i = 0;
            while (key.compareTo(this.entries[i].key) != 0) {
                i++;
            }
            //左边节点的最大值可以直接插入,右边的要挪一挪再进行插入
            this.entries[i] = node1.entries[node1.keyNum - 1];
            Entry<K, V>[] temp = new Entry[M];
            System.arraycopy(this.entries, 0, temp, 0, i);
            System.arraycopy(this.entries, i, temp, i + 1, this.keyNum - i);
            temp[i + 1] = node2.entries[node2.keyNum - 1];
            this.keyNum++;

            //判断是否分裂
            if (this.keyNum <= M) {
                System.arraycopy(temp, 0, this.entries, 0, this.keyNum);
                return null;
            }

            BPlusNode<K, V> tempNode = new BPlusNode<K, V>();
            tempNode.keyNum = this.keyNum - this.keyNum / 2;
            tempNode.parent = this.parent;
            if (this.parent == null) {
                //若没有父节点,创建一个非叶子节点
                BPlusNode<K, V> parentNode = new BPlusNode<>();
                tempNode.parent = parentNode;
                this.parent = parentNode;
                oldKey = null;
            }
            //将数据复制到新节点,原来的叶子节点只留左半边
            System.arraycopy(temp, this.keyNum / 2, tempNode.entries, 0, tempNode.keyNum);
            for (int j = this.keyNum / 2; j < this.entries.length; j++) this.entries[j] = null;
            return this.parent.insertNode(this, tempNode, oldKey);
        }
    }

    /**
     * 叶子节点，其中包含全部元素的信息,
     * 所有叶子是一个双向链表
     */
    class LeafNode<K extends Comparable<K>, V> extends Node<K, V> {

        protected LeafNode<K, V> left;//链表前节点
        protected LeafNode<K, V> right; //链表后节点

        @Override
        V find(K key) {
            //二分查找
            int l = 0, r = this.keyNum;
            while (l < r) {
                int m = (l + r) >>> 1;
                K midKey = this.entries[m].key;
                if (key.compareTo(midKey) == 0) {
                    return this.entries[m].value;
                } else if (key.compareTo(midKey) < 0) {
                    r = m;
                } else {
                    l = m;
                }
            }
            return null;
        }

        @Override
        Node<K, V> insert(K key, V value) {
            K oldKey = null;//原始key
            if (this.keyNum > 0) oldKey = this.entries[this.keyNum - 1].key;
            //寻找要插入的区间
            int i = 0;
            while (i++ < this.keyNum) {
                if (key.compareTo(this.entries[i].key) < 0) break;
            }

            //copy一个新的子节点数组,第i位后向后移,新元素插入i的位置,当前节点存储数量+1
            Entry<K, V>[] temp = new Entry[M];
            System.arraycopy(this.entries, 0, temp, 0, i);
            System.arraycopy(this.entries, i, temp, i + 1, this.keyNum - i);
            temp[i] = new Entry<>(key, value);
            this.keyNum++;
            if (this.keyNum <= M) {
                //没超过上限不需要分裂
                System.arraycopy(temp, 0, this.entries, 0, this.keyNum);
                //判断是否更新父节点的边界值,递归往上赋值
                Node<K, V> node = this;
                while (node.parent != null) {
                    if (node.entries[node.keyNum - 1].key
                            .compareTo(node.parent.entries[node.parent.keyNum - 1].key) > 0) {
                        //若当前最大节点比父节点最大节点值大
                        node.parent.entries[node.parent.keyNum - 1].key = node.entries[node.keyNum - 1].key;
                    }
                    node = node.parent;
                }
                //不分裂提前返回
                return null;
            }
            //开始分裂
            //新建叶子节点(迁移数量为当前节点数量的一半)
            LeafNode<K, V> tempNode = new LeafNode<K, V>();
            tempNode.keyNum = this.keyNum - this.keyNum / 2;
            tempNode.parent = this.parent;
            if (this.parent == null) {
                //若没有父节点,创建一个非叶子节点
                BPlusNode<K, V> parentNode = new BPlusNode<>();
                tempNode.parent = parentNode;
                this.parent = parentNode;
                oldKey = null;
            }
            //将数据复制到新节点,原来的叶子节点只留左半边
            System.arraycopy(temp, this.keyNum / 2, tempNode.entries, 0, tempNode.keyNum);
            for (int j = this.keyNum / 2; j < this.entries.length; j++) this.entries[j] = null;

            //设置叶子节点链表
            this.right = tempNode;
            tempNode.left = this;
            //插入父节点
            return this.parent.insertNode(this, tempNode, oldKey);
        }
    }
}
