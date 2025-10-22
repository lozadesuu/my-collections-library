package com.lozadesuu.collections;

/** Простая пара ключ-значение (значение может быть null). */
public class Entry<K, V> {
    private final K key;
    private V value;

    public Entry(K key, V value) {
        this.key = key;
        this.value = value; // допускаем null
    }

    public K getKey() { return key; }
    public V getValue() { return value; }
    public void setValue(V value) { this.value = value; } // допускаем null

    @Override
    public String toString() {
        return key + "=" + value;
    }
}
