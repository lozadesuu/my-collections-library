package com.lozadesuu.collections;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
public interface MyMap<K, V> {
    void put(K key, V value);
    V get(K key);
    void remove(K key);
    boolean containsKey(K key);
    int size();
    void clear();
}


    








