package com.lozadesuu.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Двунаправленная карта: K <-> V (оба уникальны).
 * K, V не может быть null. С поддержкой modCount.
 * modCount увеличивается только при структурных модификациях.
 */
public class MyBidiMap<K extends Comparable<K>, V extends Comparable<V>> implements MyMap<K, V> {
    private final MyTreeMap<K, V> keyToValue = new MyTreeMap<>();
    private final MyTreeMap<V, K> valueToKey = new MyTreeMap<>();
    private int modCount = 0;

    @Override
    public void put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("key must not be null");
        if (value == null) throw new IllegalArgumentException("value must not be null");

        // Проверяем текущее состояние
        V oldValue = keyToValue.get(key);
        K oldKey = valueToKey.get(value);

        // Если эта же пара уже существует, ничего не делаем
        if (Objects.equals(oldValue, value) && Objects.equals(oldKey, key)) {
            return; // Не структурное изменение
        }

        // Удаляем старые связи, если они существуют
        boolean structuralChange = false;

        if (oldValue != null && !Objects.equals(oldValue, value)) {
            // Удаляем старую обратную связь value->key
            valueToKey.remove(oldValue);
            structuralChange = true;
        }

        if (oldKey != null && !Objects.equals(oldKey, key)) {
            // Удаляем старую прямую связь key->value
            keyToValue.remove(oldKey);
            structuralChange = true;
        }

        // Добавляем новые связи
        if (oldValue == null || !Objects.equals(oldValue, value)) {
            keyToValue.put(key, value);
            structuralChange = true;
        }

        if (oldKey == null || !Objects.equals(oldKey, key)) {
            valueToKey.put(value, key);
            structuralChange = true;
        }

        // Увеличиваем modCount только если произошло структурное изменение
        if (structuralChange) {
            modCount++;
        }
    }

    @Override
    public V get(K key) {
        if (key == null) throw new IllegalArgumentException("key must not be null");
        return keyToValue.get(key);
    }

    public K getKey(V value) {
        if (value == null) throw new IllegalArgumentException("value must not be null");
        return valueToKey.get(value);
    }

    @Override
    public void remove(K key) {
        if (key == null) throw new IllegalArgumentException("key must not be null");
        V v = keyToValue.get(key);
        if (v != null) {
            keyToValue.remove(key);
            valueToKey.remove(v);
            modCount++; // Структурное изменение: удалены элементы
        }
    }

    public void removeValue(V value) {
        if (value == null) throw new IllegalArgumentException("value must not be null");
        K k = valueToKey.get(value);
        if (k != null) {
            valueToKey.remove(value);
            keyToValue.remove(k);
            modCount++; // Структурное изменение: удалены элементы
        }
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) return false;
        return keyToValue.containsKey(key);
    }

    public boolean containsValue(V value) {
        if (value == null) return false;
        return valueToKey.containsKey(value);
    }

    @Override
    public int size() {
        return keyToValue.size();
    }

    @Override
    public boolean isEmpty() {
        return keyToValue.isEmpty();
    }

    @Override
    public void clear() {
        keyToValue.clear();
        valueToKey.clear();
        modCount++; // Структурное изменение: очистка всех элементов
    }

    @Override
    public List<K> keys() {
        return keyToValue.keys();
    }

    @Override
    public List<V> values() {
        return keyToValue.values();
    }

    @Override
    public List<Entry<K, V>> entries() {
        return keyToValue.entries();
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new BidiEntryIterator();
    }

    @Override
    public Iterator<K> keyIterator() {
        return new BidiKeyIterator();
    }

    @Override
    public Iterator<V> valueIterator() {
        return new BidiValueIterator();
    }

    private abstract class BaseBidiIterator<T> implements Iterator<T> {
        protected final Iterator<Entry<K, V>> delegate = keyToValue.iterator();
        protected final int expectedModCount = modCount;

        protected void checkModification() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public boolean hasNext() {
            checkModification();
            return delegate.hasNext();
        }
    }

    private class BidiEntryIterator extends BaseBidiIterator<Entry<K, V>> {
        @Override
        public Entry<K, V> next() {
            checkModification();
            return delegate.next();
        }
    }

    private class BidiKeyIterator extends BaseBidiIterator<K> {
        @Override
        public K next() {
            checkModification();
            return delegate.next().getKey();
        }
    }

    private class BidiValueIterator extends BaseBidiIterator<V> {
        @Override
        public V next() {
            checkModification();
            return delegate.next().getValue();
        }
    }

    public void printEntries() {
        System.out.println("Entries: " + entries());
    }

    public void printKeys() {
        System.out.println("Keys: " + keys());
    }

    public void printValues() {
        System.out.println("Values: " + values());
    }
}
