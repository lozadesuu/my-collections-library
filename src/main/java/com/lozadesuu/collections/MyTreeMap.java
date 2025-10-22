package com.lozadesuu.collections;

import java.util.Objects;

/**
 * Красно-чёрное дерево (аналог TreeMap) с поддержкой null-значений (V может быть null).
 * Ключи K: не-null, K extends Comparable<K>.
 */
public class MyTreeMap<K extends Comparable<K>, V> implements MyNavigableMap<K, V> {
    private MyTreeNode<K, V> root;
    private int size;

    // --------------------- базовые хелперы ---------------------

    private boolean isRed(MyTreeNode<K, V> n) { return n != null && n.isRed; }

    private void rotateLeft(MyTreeNode<K, V> x) {
        MyTreeNode<K, V> y = x.right;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(MyTreeNode<K, V> x) {
        MyTreeNode<K, V> y = x.left;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    private MyTreeNode<K, V> findMin(MyTreeNode<K, V> n) {
        while (n.left != null) n = n.left;
        return n;
    }
    private MyTreeNode<K, V> findMax(MyTreeNode<K, V> n) {
        while (n.right != null) n = n.right;
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

    // --------------------- интерфейс MyMap ---------------------

    @Override
    public void put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("key must not be null");

        if (root == null) {
            root = new MyTreeNode<>(key, value);
            root.isRed = false;
            size = 1;
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
                p.value = value;
                return;
            }
        }

        MyTreeNode<K, V> x = new MyTreeNode<>(key, value);
        x.parent = parent;
        if (cmp < 0) parent.left = x; else parent.right = x;
        size++;
        fixAfterInsertion(x);
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
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) return false;
        return getNode(root, key) != null;
    }

    @Override public int size() { return size; }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public void clear() { root = null; size = 0; }

    // --------------------- RB-вставка ---------------------

    private void fixAfterInsertion(MyTreeNode<K, V> x) {
        while (x != root && isRed(x.parent)) {
            if (x.parent == x.parent.parent.left) {
                MyTreeNode<K, V> y = x.parent.parent.right;
                if (isRed(y)) {
                    x.parent.isRed = false;
                    y.isRed = false;
                    x.parent.parent.isRed = true;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.right) {
                        x = x.parent;
                        rotateLeft(x);
                    }
                    x.parent.isRed = false;
                    x.parent.parent.isRed = true;
                    rotateRight(x.parent.parent);
                }
            } else {
                MyTreeNode<K, V> y = x.parent.parent.left;
                if (isRed(y)) {
                    x.parent.isRed = false;
                    y.isRed = false;
                    x.parent.parent.isRed = true;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.left) {
                        x = x.parent;
                        rotateRight(x);
                    }
                    x.parent.isRed = false;
                    x.parent.parent.isRed = true;
                    rotateLeft(x.parent.parent);
                }
            }
        }
        root.isRed = false;
    }

    // --------------------- RB-удаление ---------------------

    private void deleteNode(MyTreeNode<K, V> z) {
        if (z.left != null && z.right != null) {
            MyTreeNode<K, V> s = findMin(z.right);
            z.key = s.key;
            z.value = s.value;
            z = s;
        }

        MyTreeNode<K, V> rep = (z.left != null) ? z.left : z.right;

        if (rep != null) {
            rep.parent = z.parent;
            if (z.parent == null) root = rep;
            else if (z == z.parent.left) z.parent.left = rep;
            else z.parent.right = rep;

            z.left = z.right = z.parent = null;

            if (!z.isRed) fixAfterDeletion(rep);
        } else if (z.parent == null) {
            root = null;
        } else {
            if (!z.isRed) fixAfterDeletion(z);
            if (z.parent != null) {
                if (z == z.parent.left) z.parent.left = null;
                else if (z == z.parent.right) z.parent.right = null;
                z.parent = null;
            }
        }
    }

    private void fixAfterDeletion(MyTreeNode<K, V> x) {
        while (x != root && !isRed(x)) {
            if (x == x.parent.left) {
                MyTreeNode<K, V> sib = x.parent.right;

                if (isRed(sib)) {
                    sib.isRed = false;
                    x.parent.isRed = true;
                    rotateLeft(x.parent);
                    sib = x.parent.right;
                }

                if (sib == null) {
                    x = x.parent;
                    continue;
                }

                boolean sibLeftRed  = isRed(sib.left);
                boolean sibRightRed = isRed(sib.right);

                if (!sibLeftRed && !sibRightRed) {
                    sib.isRed = true;
                    x = x.parent;
                } else {
                    if (!sibRightRed) {
                        if (sib.left != null) sib.left.isRed = false;
                        sib.isRed = true;
                        rotateRight(sib);
                        sib = x.parent.right;
                    }
                    if (sib != null) {
                        sib.isRed = x.parent.isRed;
                        x.parent.isRed = false;
                        if (sib.right != null) sib.right.isRed = false;
                    }
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                MyTreeNode<K, V> sib = x.parent.left;

                if (isRed(sib)) {
                    sib.isRed = false;
                    x.parent.isRed = true;
                    rotateRight(x.parent);
                    sib = x.parent.left;
                }

                if (sib == null) {
                    x = x.parent;
                    continue;
                }

                boolean sibRightRed = isRed(sib.right);
                boolean sibLeftRed  = isRed(sib.left);

                if (!sibRightRed && !sibLeftRed) {
                    sib.isRed = true;
                    x = x.parent;
                } else {
                    if (!sibLeftRed) {
                        if (sib.right != null) sib.right.isRed = false;
                        sib.isRed = true;
                        rotateLeft(sib);
                        sib = x.parent.left;
                    }
                    if (sib != null) {
                        sib.isRed = x.parent.isRed;
                        x.parent.isRed = false;
                        if (sib.left != null) sib.left.isRed = false;
                    }
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        if (x != null) x.isRed = false;
    }

    // --------------------- навигационные методы ---------------------

    @Override public K firstKey() { return (root == null) ? null : findMin(root).key; }
    @Override public K lastKey()  { return (root == null) ? null : findMax(root).key; }

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
            else { res = n; n = n.right; }
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
            else { res = n; if (c == 0) break; n = n.right; }
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
            else { res = n; if (c == 0) break; n = n.left; }
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
            else { res = n; n = n.left; }
        }
        return res == null ? null : res.key;
    }

    // --------------------- отладка ---------------------

    public void print() { printHelper(root, "", true); }
    private void printHelper(MyTreeNode<K, V> n, String pref, boolean tail) {
        if (n == null) return;
        String col = n.isRed ? "R" : "B";
        System.out.println(pref + (tail ? "└── " : "├── ") + n.key + " -> " + n.value + " [" + col + "]");
        if (n.left != null || n.right != null) {
            if (n.left != null)  printHelper(n.left,  pref + (tail ? "    " : "│   "), n.right == null);
            if (n.right != null) printHelper(n.right, pref + (tail ? "    " : "│   "), true);
        }
    }
}