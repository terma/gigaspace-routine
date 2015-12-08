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
    public List<Map<String, Object>> fetch(int limit) {
        return data;
    }

    @Override
    public List<Column> getColumns() {
        return columns;
    }

}
