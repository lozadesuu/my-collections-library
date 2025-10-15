package com.lozadesuu.collections;

public class MyTreeNode<K, V> {
    K key;
    V value;
    MyTreeNode<K, V> left;
    MyTreeNode<K, V> right;

    public MyTreeNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.left = null;
        this.right = null;
    }
}
