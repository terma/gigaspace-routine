package com.github.terma.gigaspaceroutine;

import java.util.List;
import java.util.Map;

public class LocalTable implements Table {

    private final List<Map<String, Object>> data;

    LocalTable(List<Map<String, Object>> data) {
        this.data = data;
    }

    @Override
    public List<Map<String, Object>> fetch() {
        return data;
    }

}
