package com.lozadesuu.collections;

/** Узел КЧ-дерева (значение может быть null). */
class MyTreeNode<K, V> {
    K key;
    V value; // допускаем null
    MyTreeNode<K, V> left, right, parent;
    boolean isRed; // true = RED, false = BLACK

    MyTreeNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.isRed = true; // новый узел всегда красный
    }
}
