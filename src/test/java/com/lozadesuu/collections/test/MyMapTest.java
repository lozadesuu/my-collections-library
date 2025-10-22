package com.lozadesuu.collections.test;

import com.lozadesuu.collections.Entry;
import com.lozadesuu.collections.MyBidiMap;
import com.lozadesuu.collections.MyTreeMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class MyTreeMapTest {
    private MyTreeMap<Integer, String> map;

    @BeforeEach
    void setUp() {
        map = new MyTreeMap<>();
    }

    @Test
    void testPutAndGet() {
        map.put(5, "пять");
        map.put(3, "три");
        map.put(7, "семь");

        assertEquals("пять", map.get(5));
        assertEquals("три", map.get(3));
        assertEquals("семь", map.get(7));
    }

    @Test
    void testPutNullValue() {
        map.put(1, null);
        assertTrue(map.containsKey(1));
        assertNull(map.get(1));
    }

    @Test
    void testPutNullKeyThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> map.put(null, "value"));
    }

    @Test
    void testUpdateValue() {
        map.put(10, "old");
        map.put(10, "new");
        assertEquals("new", map.get(10));
        assertEquals(1, map.size());
    }

    @Test
    void testRemove() {
        map.put(1, "один");
        map.put(2, "два");
        map.put(3, "три");

        map.remove(2);
        assertFalse(map.containsKey(2));
        assertNull(map.get(2));
        assertEquals(2, map.size());
    }

    @Test
    void testSize() {
        assertEquals(0, map.size());
        map.put(1, "a");
        assertEquals(1, map.size());
        map.put(2, "b");
        assertEquals(2, map.size());
        map.remove(1);
        assertEquals(1, map.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(map.isEmpty());
        map.put(1, "a");
        assertFalse(map.isEmpty());
    }

    @Test
    void testClear() {
        map.put(1, "a");
        map.put(2, "b");
        map.clear();
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    @Test
    void testContainsKey() {
        map.put(5, "пять");
        assertTrue(map.containsKey(5));
        assertFalse(map.containsKey(10));
    }

    @Test
    void testFirstAndLastKey() {
        map.put(5, "пять");
        map.put(2, "два");
        map.put(8, "восемь");

        assertEquals(2, map.firstKey());
        assertEquals(8, map.lastKey());
    }

    @Test
    void testFirstAndLastEntry() {
        map.put(5, "пять");
        map.put(2, "два");
        map.put(8, "восемь");

        Entry<Integer, String> first = map.firstEntry();
        assertEquals(2, first.getKey());
        assertEquals("два", first.getValue());

        Entry<Integer, String> last = map.lastEntry();
        assertEquals(8, last.getKey());
        assertEquals("восемь", last.getValue());
    }

    @Test
    void testFloorKey() {
        map.put(1, "a");
        map.put(5, "b");
        map.put(10, "c");

        assertEquals(5, map.floorKey(5));
        assertEquals(5, map.floorKey(7));
        assertEquals(1, map.floorKey(3));
        assertNull(map.floorKey(0));
    }

    @Test
    void testCeilingKey() {
        map.put(1, "a");
        map.put(5, "b");
        map.put(10, "c");

        assertEquals(5, map.ceilingKey(5));
        assertEquals(5, map.ceilingKey(3));
        assertEquals(10, map.ceilingKey(7));
        assertNull(map.ceilingKey(11));
    }

    @Test
    void testLowerKey() {
        map.put(1, "a");
        map.put(5, "b");
        map.put(10, "c");

        assertEquals(1, map.lowerKey(5));
        assertEquals(5, map.lowerKey(7));
        assertNull(map.lowerKey(1));
    }

    @Test
    void testHigherKey() {
        map.put(1, "a");
        map.put(5, "b");
        map.put(10, "c");

        assertEquals(10, map.higherKey(5));
        assertEquals(5, map.higherKey(3));
        assertNull(map.higherKey(10));
    }

    @Test
    void testMultipleOperations() {
        for (int i = 1; i <= 20; i++) {
            map.put(i, "val" + i);
        }
        assertEquals(20, map.size());

        for (int i = 2; i <= 20; i += 2) {
            map.remove(i);
        }
        assertEquals(10, map.size());

        for (int i = 1; i <= 20; i += 2) {
            assertTrue(map.containsKey(i));
        }
    }
}

class MyBidiMapTest {
    private MyBidiMap<Integer, String> bidi;

    @BeforeEach
    void setUp() {
        bidi = new MyBidiMap<>();
    }

    @Test
    void testPutAndGet() {
        bidi.put(1, "один");
        bidi.put(2, "два");

        assertEquals("один", bidi.get(1));
        assertEquals("два", bidi.get(2));
    }

    @Test
    void testGetKey() {
        bidi.put(1, "один");
        bidi.put(2, "два");

        assertEquals(1, bidi.getKey("один"));
        assertEquals(2, bidi.getKey("два"));
    }

    @Test
    void testPutNullKeyThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> bidi.put(null, "value"));
    }

    @Test
    void testPutNullValueThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> bidi.put(1, null));
    }

    @Test
    void testOverwriteValue() {
        bidi.put(1, "старое");
        bidi.put(1, "новое");

        assertEquals("новое", bidi.get(1));
        assertEquals(1, bidi.getKey("новое"));
        assertNull(bidi.getKey("старое"));
        assertEquals(1, bidi.size());
    }

    @Test
    void testOverwriteKeyWhenValueExists() {
        bidi.put(1, "значение");
        bidi.put(2, "значение");

        assertNull(bidi.get(1));
        assertEquals("значение", bidi.get(2));
        assertEquals(2, bidi.getKey("значение"));
        assertEquals(1, bidi.size());
    }

    @Test
    void testRemoveByKey() {
        bidi.put(1, "один");
        bidi.put(2, "два");

        bidi.remove(1);
        assertNull(bidi.get(1));
        assertNull(bidi.getKey("один"));
        assertEquals(1, bidi.size());
    }

    @Test
    void testRemoveByValue() {
        bidi.put(1, "один");
        bidi.put(2, "два");

        bidi.removeValue("один");
        assertNull(bidi.get(1));
        assertNull(bidi.getKey("один"));
        assertEquals(1, bidi.size());
    }

    @Test
    void testContainsKey() {
        bidi.put(1, "один");
        assertTrue(bidi.containsKey(1));
        assertFalse(bidi.containsKey(2));
    }

    @Test
    void testContainsValue() {
        bidi.put(1, "один");
        assertTrue(bidi.containsValue("один"));
        assertFalse(bidi.containsValue("два"));
    }

    @Test
    void testSize() {
        assertEquals(0, bidi.size());
        bidi.put(1, "a");
        assertEquals(1, bidi.size());
        bidi.put(2, "b");
        assertEquals(2, bidi.size());
        bidi.remove(1);
        assertEquals(1, bidi.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(bidi.isEmpty());
        bidi.put(1, "a");
        assertFalse(bidi.isEmpty());
    }

    @Test
    void testClear() {
        bidi.put(1, "a");
        bidi.put(2, "b");
        bidi.clear();
        assertTrue(bidi.isEmpty());
        assertEquals(0, bidi.size());
    }

    @Test
    void testBidirectionalIntegrity() {
        bidi.put(1, "a");
        bidi.put(2, "b");
        bidi.put(3, "c");

        assertEquals("a", bidi.get(1));
        assertEquals("b", bidi.get(2));
        assertEquals("c", bidi.get(3));

        assertEquals(1, bidi.getKey("a"));
        assertEquals(2, bidi.getKey("b"));
        assertEquals(3, bidi.getKey("c"));

        bidi.put(1, "new");
        assertNull(bidi.getKey("a"));
        assertEquals(1, bidi.getKey("new"));
    }
}

class EntryTest {
    @Test
    void testEntryCreation() {
        Entry<Integer, String> entry = new Entry<>(1, "один");
        assertEquals(1, entry.getKey());
        assertEquals("один", entry.getValue());
    }

    @Test
    void testEntryWithNullValue() {
        Entry<Integer, String> entry = new Entry<>(1, null);
        assertEquals(1, entry.getKey());
        assertNull(entry.getValue());
    }

    @Test
    void testSetValue() {
        Entry<Integer, String> entry = new Entry<>(1, "старое");
        entry.setValue("новое");
        assertEquals("новое", entry.getValue());
    }

    @Test
    void testToString() {
        Entry<Integer, String> entry = new Entry<>(5, "пять");
        assertEquals("5=пять", entry.toString());
    }
}