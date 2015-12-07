package com.github.terma.gigaspaceroutine;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Columns {

    private final List<Column> columns;

    public Columns(List<Column> columns) {
        this.columns = columns;
    }

    public Column find(String name) {
        for (Column column : columns)
            if (column.getName().equals(name)) return column;
        throw new IllegalArgumentException("Can't find column with name: " + name);
    }

    public Set<String> getNames() {
        Set<String> result = new HashSet<>();
        for (Column column : columns) result.add(column.getName());
        return result;
    }

}
