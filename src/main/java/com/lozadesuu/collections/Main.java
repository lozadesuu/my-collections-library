package com.lozadesuu.collections;

        public class Main {
            public static void main(String[] args) {
                MyTreeMap<Integer, String> map = new MyTreeMap<>();
                demoTreeMap(map);
                demoBidiMap();
            }

            private static void demoTreeMap(MyTreeMap<Integer, String> map) {
                title("Вставка ключей");
                int[] keys = {5, 3, 7, 2, 4, 6, 8, 1, 9};
                for (int k : keys) map.put(k, "v" + k);
                map.print();

                title("Навигация по ключам");
                System.out.println("firstKey  = " + map.firstKey());
                System.out.println("lastKey   = " + map.lastKey());
                System.out.println("lowerKey(5)   = " + map.lowerKey(5));
                System.out.println("floorKey(5)   = " + map.floorKey(5));
                System.out.println("ceilingKey(5) = " + map.ceilingKey(5));
                System.out.println("higherKey(5)  = " + map.higherKey(5));
                Entry<Integer, String> fe = map.firstEntry();
                Entry<Integer, String> le = map.lastEntry();
                System.out.println("firstEntry = " + (fe != null ? fe : "null"));
                System.out.println("lastEntry  = " + (le != null ? le : "null"));

                title("Обновление значения (ключ 4 → v4_new)");
                map.put(4, "v4_new");
                System.out.println("get(4) = " + map.get(4));
                map.print();

                title("Удаление листа (key=1)");
                map.remove(1);
                map.print();

                title("Удаление узла с одним ребёнком (key=9)");
                map.remove(9);
                map.print();

                title("Удаление узла с двумя детьми (key=3)");
                map.remove(3);
                map.print();

                title("Проверка containsKey / size");
                System.out.println("containsKey(3) = " + map.containsKey(3));
                System.out.println("containsKey(4) = " + map.containsKey(4));
                System.out.println("size = " + map.size());

                title("Вставка null-значения (key=100, value=null)");
                map.put(100, null);
                System.out.println("get(100) = " + map.get(100));
                map.print();

                title("clear()");
                map.clear();
                System.out.println("isEmpty = " + map.isEmpty() + ", size = " + map.size());
            }

            private static void demoBidiMap() {
                title("Демонстрация MyBidiMap");
                MyBidiMap<Integer, String> bidi = new MyBidiMap<>();
                bidi.put(1, "one");
                bidi.put(2, "two");
                bidi.put(3, "three");

                System.out.println("get(2)     = " + bidi.get(2));
                System.out.println("getKey(\"three\") = " + bidi.getKey("three"));
                System.out.println("size       = " + bidi.size());

                title("Переназначение значения существующему ключу (1 -> \"uno\")");
                bidi.put(1, "uno");
                System.out.println("get(1)         = " + bidi.get(1));
                System.out.println("getKey(\"one\")   = " + bidi.getKey("one"));
                System.out.println("getKey(\"uno\")   = " + bidi.getKey("uno"));
                System.out.println("size            = " + bidi.size());

                title("Удаление по значению (\"two\")");
                bidi.removeValue("two");
                System.out.println("get(2)         = " + bidi.get(2));
                System.out.println("containsValue(\"two\") = " + bidi.containsValue("two"));
                System.out.println("size            = " + bidi.size());

                title("clear()");
                bidi.clear();
                System.out.println("isEmpty = " + bidi.isEmpty() + ", size = " + bidi.size());
            }

            private static void title(String s) {
                System.out.println("\n=== " + s + " ===");
            }
        }
