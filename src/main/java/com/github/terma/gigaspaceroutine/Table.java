package com.github.terma.gigaspaceroutine;

import java.util.List;
import java.util.Map;

public interface Table {

    List<Map<String, Object>> fetch();

    List<Map<String, Object>> fetch(int limit);

    List<Column> getColumns();

}
