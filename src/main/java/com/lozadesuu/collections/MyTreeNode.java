package com.lozadesuu.collections;

class MyTreeNode<K, V> {
    K key;
    V value;
    MyTreeNode<K, V> left, right, parent;

    MyTreeNode(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
