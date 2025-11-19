package com.lozadesuu.collections;

public interface MyNavigableMap<K, V> extends MyMap<K, V> {
    K firstKey();
    K lastKey();
    K lowerKey(K key);
    K floorKey(K key);
    K ceilingKey(K key);
    K higherKey(K key);
    Entry<K, V> firstEntry();
    Entry<K, V> lastEntry();
}