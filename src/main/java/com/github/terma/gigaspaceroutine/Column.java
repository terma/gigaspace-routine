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

import com.github.terma.gigaspaceroutine.extractors.Extractor;

public class Column {

    private final Extractor extractor;
    private final String name;

    public Column(Extractor extractor, String name) {
        if (extractor == null) throw new IllegalArgumentException("Can't create with null extractor!");

        this.extractor = extractor;
        this.name = name;
    }

    public Extractor getExtractor() {
        return extractor;
    }

    public String getName() {
        return name;
    }
}
