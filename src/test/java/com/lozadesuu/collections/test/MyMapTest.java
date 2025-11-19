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

    }