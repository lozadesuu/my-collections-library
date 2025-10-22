package com.lozadesuu.collections;

public interface MyMap<K, V> {
    /** Добавляет/заменяет значение по ключу (значение может быть null). */
    void put(K key, V value);

    /** Возвращает значение по ключу или null, если пары нет ИЛИ значение равно null. */
    V get(K key);

    /** Удаляет пару по ключу (ничего не делает, если ключа нет). */
    void remove(K key);

    boolean isEmpty();
    boolean containsKey(K key);
    int size();
    void clear();
}
