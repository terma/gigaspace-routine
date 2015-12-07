package com.github.terma.gigaspaceroutine.comparators;

import java.util.Comparator;
import java.util.Map;

public class StringComparator implements Comparator<Map<String, Object>> {

    private final String key;

    public StringComparator(String key) {
        this.key = key;
    }

    @Override
    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
        String v1 = (String) o1.get(key);
        String v2 = (String) o2.get(key);
        return v1.compareTo(v2);
    }

}
