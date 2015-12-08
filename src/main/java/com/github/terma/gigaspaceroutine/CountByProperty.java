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

import com.gigaspaces.document.SpaceDocument;
import com.github.terma.gigaspaceroutine.extractors.IntOneExtractor;

import java.io.Serializable;
import java.util.HashMap;

public class CountByProperty implements Serializable {

    private final IntSumByProperty summator;

    public CountByProperty(final String aggregateProperty) {
        this.summator = new IntSumByProperty(aggregateProperty, new IntOneExtractor());
    }

    public void count(final SpaceDocument spaceDocument) {
        summator.add(spaceDocument);
    }

    public void count(final CountByProperty countByProperty) {
        summator.add(countByProperty.getResult());
    }

    public HashMap<Serializable, Integer> getResult() {
        return summator.getResult();
    }

}
