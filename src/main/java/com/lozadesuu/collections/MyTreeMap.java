package com.lozadesuu.collections;

public class MyTreeMap<K extends Comparable<K>, V> implements MyMap<K, V> {
    private MyTreeNode<K, V> root;
    private int size;

    public MyTreeMap() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Ключ не может быть null");
        }
        root = putHelper(root, key, value);
    }

    private MyTreeNode<K, V> putHelper(MyTreeNode<K, V> node, K key, V value) {
        if (node == null) {
            size++;
            return new MyTreeNode<>(key, value);
        }

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            node.left = putHelper(node.left, key, value);
        } else if (cmp > 0) {
            node.right = putHelper(node.right, key, value);
        } else {
            node.value = value;
        }

        return node;
    }

    @Override
    public V get(K key) {
        if (key == null) {
            return null;
        }
        MyTreeNode<K, V> node = getNode(root, key);
        return node != null ? node.value : null;
    }

    private MyTreeNode<K, V> getNode(MyTreeNode<K, V> node, K key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            return getNode(node.left, key);
        } else if (cmp > 0) {
            return getNode(node.right, key);
        } else {
            return node;
        }
    }

    @Override
    public void remove(K key) {
        if (key == null) {
            return;
        }
        root = removeHelper(root, key);
    }

    private MyTreeNode<K, V> removeHelper(MyTreeNode<K, V> node, K key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            node.left = removeHelper(node.left, key);
        } else if (cmp > 0) {
            node.right = removeHelper(node.right, key);
        } else {
            size--;

            if (node.left == null && node.right == null) {
                return null;
            }
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }

            MyTreeNode<K, V> minNode = findMin(node.right);
            node.key = minNode.key;
            node.value = minNode.value;
            node.right = removeHelper(node.right, minNode.key);
            size++;
        }

        return node;
    }

    private MyTreeNode<K, V> findMin(MyTreeNode<K, V> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    public void print() {
        printHelper(root, "", true);
    }

    private void printHelper(MyTreeNode<K, V> node, String prefix, boolean isTail) {
        if (node == null) {
            return;
        }

        System.out.println(prefix + (isTail ? "└── " : "├── ") + node.key + ": " + node.value);

        if (node.left != null || node.right != null) {
            if (node.left != null) {
                printHelper(node.left, prefix + (isTail ? "    " : "│   "), node.right == null);
            }
            if (node.right != null) {
                printHelper(node.right, prefix + (isTail ? "    " : "│   "), true);
            }
        }
    }
}