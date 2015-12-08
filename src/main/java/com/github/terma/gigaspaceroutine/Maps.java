package com.github.terma.gigaspaceroutine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Maps {

    public static <K, V> Map<V, List<Map<K, V>>> groupBy(List<Map<K, V>> list, K key) {
        Map<V, List<Map<K, V>>> grouped = new HashMap<>();
        for (Map<K, V> item : list) {
            V keyValue = item.get(key);
            List<Map<K, V>> v = grouped.get(keyValue);
            if (v == null) {
                v = new ArrayList<>();
                grouped.put(keyValue, v);
            }

            v.add(item);
        }
        return grouped;
    }

}
