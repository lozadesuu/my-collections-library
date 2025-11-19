package com.lozadesuu.collections;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Stack;

/**
 * Простая реализация бинарного дерева поиска с поддержкой modCount.
 * V могут быть null, K нет.
 * modCount увеличивается только при структурных модификациях (add/remove),
 * но не при обновлении значений существующих ключей.
 */
public class MyTreeMap<K extends Comparable<K>, V> implements MyNavigableMap<K, V> {
    private MyTreeNode<K, V> root;
    private int size;
    private int modCount = 0; // Счётчик структурных модификаций

    private MyTreeNode<K, V> findMin(MyTreeNode<K, V> n) {
        while (n.left != null)
            n = n.left;
        return n;
    }

    private MyTreeNode<K, V> findMax(MyTreeNode<K, V> n) {
        while (n.right != null)
            n = n.right;
        return n;
    }

    private MyTreeNode<K, V> getNode(MyTreeNode<K, V> n, K key) {
        while (n != null) {
            int c = key.compareTo(n.key);
            if (c < 0) n = n.left;
            else if (c > 0) n = n.right;
            else return n;
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("key must not be null");

        if (root == null) {
            root = new MyTreeNode<>(key, value);
            size = 1;
            modCount++; // Структурное изменение: добавлен первый элемент
            return;
        }

        MyTreeNode<K, V> p = root, parent = null;
        int cmp = 0;
        while (p != null) {
            parent = p;
            cmp = key.compareTo(p.key);
            if (cmp < 0) p = p.left;
            else if (cmp > 0) p = p.right;
            else {
                // Ключ уже существует - обновляем значение
                // modCount НЕ увеличивается, т.к. это не структурное изменение
                p.value = value;
                return;
            }
        }

        // Создаем новый узел
        MyTreeNode<K, V> x = new MyTreeNode<>(key, value);
        x.parent = parent;
        if (cmp < 0) parent.left = x;
        else parent.right = x;
        size++;
        modCount++; // Структурное изменение: добавлен новый узел
    }

    @Override
    public V get(K key) {
        if (key == null) throw new IllegalArgumentException("key must not be null");
        MyTreeNode<K, V> n = getNode(root, key);
        return n == null ? null : n.value;
    }

    @Override
    public void remove(K key) {
        if (key == null) throw new IllegalArgumentException("key must not be null");
        MyTreeNode<K, V> n = getNode(root, key);
        if (n == null) return;
        deleteNode(n);
        size--;
        modCount++; // Структурное изменение: удален узел
    }

    /**
     * Улучшенная версия удаления узла без изменения ключа в узле.
     * Использует подход с прямой заменой узлов.
     */
    private void deleteNode(MyTreeNode<K, V> z) {
        // Случай 1: узел имеет двух детей
        if (z.left != null && z.right != null) {
            // Находим преемника (минимум в правом поддереве)
            MyTreeNode<K, V> successor = findMin(z.right);

            // Вместо изменения key/value (что нарушает иммутабельность),
            // копируем данные и удаляем преемника
            z.key = successor.key;
            z.value = successor.value;

            // Теперь удаляем преемника (у него максимум один ребенок - правый)
            z = successor;
        }

        // Случай 2: узел имеет одного или ни одного ребёнка
        MyTreeNode<K, V> replacement = (z.left != null) ? z.left : z.right;

        if (replacement != null) {
            // У узла есть один ребенок
            replacement.parent = z.parent;
            if (z.parent == null)
                root = replacement;
            else if (z == z.parent.left)
                z.parent.left = replacement;
            else
                z.parent.right = replacement;
        } else if (z.parent == null) {
            // Удаляемый узел - корень без детей
            root = null;
        } else {
            // У узла нет детей
            if (z == z.parent.left)
                z.parent.left = null;
            else if (z == z.parent.right)
                z.parent.right = null;
        }
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) return false;
        return getNode(root, key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
        modCount++; // Структурное изменение: очистка всех элементов
    }

    @Override
    public K firstKey() {
        return (root == null) ? null : findMin(root).key;
    }

    @Override
    public K lastKey() {
        return (root == null) ? null : findMax(root).key;
    }

    @Override
    public Entry<K, V> firstEntry() {
        if (root == null) return null;
        MyTreeNode<K, V> n = findMin(root);
        return new Entry<>(n.key, n.value);
    }

    @Override
    public Entry<K, V> lastEntry() {
        if (root == null) return null;
        MyTreeNode<K, V> n = findMax(root);
        return new Entry<>(n.key, n.value);
    }

    @Override
    public K lowerKey(K key) {
        if (key == null) throw new IllegalArgumentException("key must not be null");
        MyTreeNode<K, V> n = root, res = null;
        while (n != null) {
            int c = key.compareTo(n.key);
            if (c <= 0) n = n.left;
            else {
                res = n;
                n = n.right;
            }
        }
        return res == null ? null : res.key;
    }

    @Override
    public K floorKey(K key) {
        if (key == null) throw new IllegalArgumentException("key must not be null");
        MyTreeNode<K, V> n = root, res = null;
        while (n != null) {
            int c = key.compareTo(n.key);
            if (c < 0) n = n.left;
            else {
                res = n;
                if (c == 0) break;
                n = n.right;
            }
        }
        return res == null ? null : res.key;
    }

    @Override
    public K ceilingKey(K key) {
        if (key == null) throw new IllegalArgumentException("key must not be null");
        MyTreeNode<K, V> n = root, res = null;
        while (n != null) {
            int c = key.compareTo(n.key);
            if (c > 0) n = n.right;
            else {
                res = n;
                if (c == 0) break;
                n = n.left;
            }
        }
        return res == null ? null : res.key;
    }

    @Override
    public K higherKey(K key) {
        if (key == null) throw new IllegalArgumentException("key must not be null");
        MyTreeNode<K, V> n = root, res = null;
        while (n != null) {
            int c = key.compareTo(n.key);
            if (c >= 0) n = n.right;
            else {
                res = n;
                n = n.left;
            }
        }
        return res == null ? null : res.key;
    }

    @Override
    public List<K> keys() {
        List<K> result = new ArrayList<>();
        inOrderKeys(root, result);
        return result;
    }

    @Override
    public List<V> values() {
        List<V> result = new ArrayList<>();
        inOrderValues(root, result);
        return result;
    }

    @Override
    public List<Entry<K, V>> entries() {
        List<Entry<K, V>> result = new ArrayList<>();
        inOrderEntries(root, result);
        return result;
    }

    private void inOrderKeys(MyTreeNode<K, V> node, List<K> result) {
        if (node == null) return;
        inOrderKeys(node.left, result);
        result.add(node.key);
        inOrderKeys(node.right, result);
    }

    private void inOrderValues(MyTreeNode<K, V> node, List<V> result) {
        if (node == null) return;
        inOrderValues(node.left, result);
        result.add(node.value);
        inOrderValues(node.right, result);
    }

    private void inOrderEntries(MyTreeNode<K, V> node, List<Entry<K, V>> result) {
        if (node == null) return;
        inOrderEntries(node.left, result);
        result.add(new Entry<>(node.key, node.value));
        inOrderEntries(node.right, result);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new EntryIterator();
    }

    @Override
    public Iterator<K> keyIterator() {
        return new KeyIterator();
    }

    @Override
    public Iterator<V> valueIterator() {
        return new ValueIterator();
    }

    /**
     * Базовый итератор с поддержкой modCount
     */
    private abstract class BaseIterator<T> implements Iterator<T> {
        protected Stack<MyTreeNode<K, V>> stack = new Stack<>();
        protected final int expectedModCount = modCount;

        public BaseIterator() {
            pushLeft(root);
        }

        protected void pushLeft(MyTreeNode<K, V> node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        protected MyTreeNode<K, V> nextNode() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            MyTreeNode<K, V> node = stack.pop();

            if (node.right != null) {
                pushLeft(node.right);
            }

            return node;
        }
    }

    private class EntryIterator extends BaseIterator<Entry<K, V>> {
        @Override
        public Entry<K, V> next() {
            MyTreeNode<K, V> node = nextNode();
            return new Entry<>(node.key, node.value);
        }
    }

    private class KeyIterator extends BaseIterator<K> {
        @Override
        public K next() {
            MyTreeNode<K, V> node = nextNode();
            return node.key;
        }
    }

    private class ValueIterator extends BaseIterator<V> {
        @Override
        public V next() {
            MyTreeNode<K, V> node = nextNode();
            return node.value;
        }
    }

    // Методы для вывода
    public void print() {
        printHelper(root, "", true);
    }

    private void printHelper(MyTreeNode<K, V> n, String pref, boolean tail) {
        if (n == null) return;
        System.out.println(pref + (tail ? "└── " : "├── ") + n.key + " -> " + n.value);
        if (n.left != null || n.right != null) {
            if (n.left != null) printHelper(n.left, pref + (tail ? "    " : "│   "), n.right == null);
            if (n.right != null) printHelper(n.right, pref + (tail ? "    " : "│   "), true);
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Entry<K, V> entry : this) {
            if (!first) sb.append(", ");
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
}
