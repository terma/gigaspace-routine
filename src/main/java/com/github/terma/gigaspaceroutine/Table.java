package com.github.terma.gigaspaceroutine;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Table {

    List<Map<String, Object>> fetch();

    List<Column> getColumns();

}
