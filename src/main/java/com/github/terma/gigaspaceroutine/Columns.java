/*
Copyright 2015 Artem Stasiuk
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

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
