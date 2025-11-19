package com.lozadesuu.collections;

import java.util.Iterator;
import java.util.List;

public interface MyMap<K, V> extends Iterable<Entry<K, V>> {
    void put(K key, V value);
    V get(K key);
    void remove(K key);
    boolean isEmpty();
    boolean containsKey(K key);
    int size();
    void clear();

    // Методы для получения всех ключей, значений и пар
    List<K> keys();
    List<V> values();
    List<Entry<K, V>> entries();

    @Override
    Iterator<Entry<K, V>> iterator();

    // Итераторы для ключей и значений
    Iterator<K> keyIterator();
    Iterator<V> valueIterator();
}