package com.lozadesuu.collections;

import java.util.Objects;

/**
 * Двунаправленная карта на двух MyTreeMap.
 * ВНИМАНИЕ: значения V используются как ключ во втором дереве → V не может быть null.
 * (MyTreeMap допускает null-значения, но именно в MyBidiMap это ограничение необходимо.)
 */
public class MyBidiMap<K extends Comparable<K>, V extends Comparable<V>> implements MyMap<K, V> {
    private final MyTreeMap<K, V> keyToValue = new MyTreeMap<>();
    private final MyTreeMap<V, K> valueToKey = new MyTreeMap<>();

    @Override
    public void put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("key must not be null");
        if (value == null) throw new IllegalArgumentException("BidiMap forbids null values (value is a key in inverse map)");

        V oldV = keyToValue.get(key);
        if (Objects.equals(oldV, value)) return;

        if (oldV != null) valueToKey.remove(oldV);
        K oldK = valueToKey.get(value);
        if (oldK != null) keyToValue.remove(oldK);

        keyToValue.put(key, value);
        valueToKey.put(value, key);
    }

    @Override
    public V get(K key) { return keyToValue.get(key); }

    /** Обратный поиск: ключ по значению (значение не может быть null). */
    public K getKey(V value) {
        if (value == null) return null;
        return valueToKey.get(value);
    }

    @Override
    public void remove(K key) {
        V v = keyToValue.get(key);
        if (v != null) {
            keyToValue.remove(key);
            valueToKey.remove(v);
        }
    }

    /** Удаление по значению. */
    public void removeValue(V value) {
        if (value == null) return;
        K k = valueToKey.get(value);
        if (k != null) {
            valueToKey.remove(value);
            keyToValue.remove(k);
        }
    }

    @Override public boolean containsKey(K key) { return keyToValue.containsKey(key); }
    public boolean containsValue(V value) { return value != null && valueToKey.containsKey(value); }
    @Override public int size() { return keyToValue.size(); }
    @Override public boolean isEmpty() { return keyToValue.isEmpty(); }
    @Override public void clear() { keyToValue.clear(); valueToKey.clear(); }
}
