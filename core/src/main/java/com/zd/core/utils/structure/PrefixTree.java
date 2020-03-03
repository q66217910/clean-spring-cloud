package com.zd.core.utils.structure;

import lombok.Data;

/**
 * 前缀树
 */
@Data
public class PrefixTree {

    private PrefixTreeNode root;

    public PrefixTree() {
        this.root = new PrefixTreeNode();
    }

    public void insert(String word) {
        PrefixTreeNode node = root;
        for (int i = 0; i < word.length(); i++) {
            char currentChar = word.charAt(i);
            if (!node.containsKey(currentChar)) {
                node.put(currentChar, new PrefixTreeNode());
            }
            node = node.get(currentChar);
        }
        node.setEnd();
    }

    public boolean search(String word) {
        PrefixTreeNode node = searchPrefix(word);
        return node != null && node.isEnd();
    }

    public PrefixTreeNode searchPrefix(String word) {
        PrefixTreeNode node = root;
        for (int i = 0; i < word.length(); i++) {
            char currentChar = word.charAt(i);
            if (node.containsKey(currentChar)) {
                node = node.get(currentChar);
            } else {
                return null;
            }
        }
        return node;
    }

    /**
     *  查询当前前缀树最大前缀
     */
    private String searchLongestPrefix() {
        PrefixTreeNode node = root;
        StringBuilder prefix = new StringBuilder();
        while ((node.size == 1) && (!node.isEnd())) {
            prefix.append(node.value);
             node = node.get(node.value);
        }
        return  prefix.toString();
    }

    private String searchLongestPrefix(String word) {
        PrefixTreeNode node = root;
        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char curLetter = word.charAt(i);
            if (node.containsKey(curLetter) && (node.getLinks().length == 1) && (!node.isEnd())) {
                prefix.append(curLetter);
                node = node.get(curLetter);
            } else
                return prefix.toString();

        }
        return prefix.toString();
    }


    public boolean startsWith(String prefix) {
        PrefixTreeNode node = searchPrefix(prefix);
        return node != null;
    }

    /**
     * 前缀树节点
     */
    static class PrefixTreeNode {

        private PrefixTreeNode[] links;

        private int size = 0;

        private char value;

        /**
         * 是否结束
         */
        private boolean isEnd;

        public PrefixTreeNode() {
            /**
             * 26个字母
             */
            int r = 26;
            links = new PrefixTreeNode[r];
        }

        public boolean containsKey(char ch) {
            return links[ch - 'a'] != null;
        }

        public PrefixTreeNode get(char ch) {
            return links[ch - 'a'];
        }

        public void put(char ch, PrefixTreeNode node) {
            links[ch - 'a'] = node;
            size++;
            value = ch;
        }

        public void setEnd() {
            isEnd = true;
        }

        public boolean isEnd() {
            return isEnd;
        }

        public PrefixTreeNode[] getLinks() {
            return links;
        }

        public void setLinks(PrefixTreeNode[] links) {
            this.links = links;
        }
    }
}
