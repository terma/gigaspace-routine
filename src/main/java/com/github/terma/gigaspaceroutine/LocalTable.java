package com.github.terma.gigaspaceroutine;

import java.util.List;
import java.util.Map;

public class LocalTable implements Table {

    private final List<Column> columns;
    private final List<Map<String, Object>> data;

    LocalTable(List<Column> columns, List<Map<String, Object>> data) {
        this.columns = columns;
        this.data = data;
    }

    @Override
    public List<Map<String, Object>> fetch() {
        return data;
    }

    @Override
    public List<Column> getColumns() {
        return columns;
    }

}
