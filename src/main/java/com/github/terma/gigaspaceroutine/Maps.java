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
