package com.lozadesuu.collections;

import com.lozadesuu.collections.MyTreeMap;
import com.lozadesuu.collections.MyBidiMap;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Демонстрация MyTreeMap (Красно-черное дерево) ===");
        MyTreeMap<Integer, String> map = new MyTreeMap<>();

        map.put(10, "A");
        map.put(20, "B");
        map.put(15, "C");
        map.put(5,  "D");
        map.put(25, "E");
        map.put(2,  "F");

        System.out.println("\n🔹 Текущее дерево:");
        map.print();

        System.out.println("\n🔹 Проверка поиска:");
        System.out.println("get(15) = " + map.get(15));
        System.out.println("get(5) = " + map.get(5));

        System.out.println("\n🔹 Ключи по порядку:");
        System.out.println("firstKey = " + map.firstKey());
        System.out.println("lastKey = " + map.lastKey());
        System.out.println("floorKey(18) = " + map.floorKey(18));
        System.out.println("ceilingKey(18) = " + map.ceilingKey(18));

        System.out.println("\n🔹 Удаление 10 и 15:");
        map.remove(10);
        map.remove(15);
        map.print();

        System.out.println("\n=== Демонстрация MyBidiMap (Двусторонняя карта) ===");
        MyBidiMap<Integer, String> bidi = new MyBidiMap<>();

        bidi.put(1, "один");
        bidi.put(2, "два");
        bidi.put(3, "три");

        System.out.println("\n🔹 Исходные значения:");
        System.out.println("get(2) = " + bidi.get(2));
        System.out.println("getKey(\"три\") = " + bidi.getKey("три"));

        System.out.println("\n🔹 Перезаписываем ключ 2 новым значением:");
        bidi.put(2, "новое-два");
        System.out.println("get(2) = " + bidi.get(2));
        System.out.println("getKey(\"новое-два\") = " + bidi.getKey("новое-два"));
        System.out.println("getKey(\"два\") = " + bidi.getKey("два")); // должно быть null

        System.out.println("\n🔹 Добавляем значение, уже существующее у другого ключа:");
        bidi.put(4, "один");
        System.out.println("get(1) = " + bidi.get(1)); // должен быть null
        System.out.println("getKey(\"один\") = " + bidi.getKey("один"));
        System.out.println("Размер карты: " + bidi.size());

        System.out.println("\n🔹 Удаляем значение и ключ:");
        bidi.removeValue("новое-два");
        bidi.remove(4);
        System.out.println("Размер после удаления: " + bidi.size());

        System.out.println("\n✅ Демонстрация завершена!");
    }
}
