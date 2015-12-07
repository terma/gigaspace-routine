package com.github.terma.gigaspaceroutine;

import com.gigaspaces.document.SpaceDocument;
import com.github.terma.gigaspaceroutine.extractors.Extractor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class IntSumByProperty implements Serializable {

    private final String aggregateProperty;
    private final Extractor<Integer> extractor;
    private final HashMap<Serializable, Integer> counts = new HashMap<>();

    public IntSumByProperty(final String aggregateProperty, Extractor<Integer> extractor) {
        this.aggregateProperty = aggregateProperty;
        this.extractor = extractor;
    }

    public void add(final SpaceDocument spaceDocument) {
        final Serializable aggregateKey = spaceDocument.getProperty(aggregateProperty);
        final Integer value = extractor.extract(spaceDocument);

        Integer resultCount = counts.get(aggregateKey);
        if (resultCount == null) {
            resultCount = value;
        } else {
            resultCount += value;
        }
        counts.put(aggregateKey, resultCount);
    }

    public void add(final IntSumByProperty same) {
        add(same.getResult());
    }

    public void add(final Map<Serializable, Integer> map) {
        for (final Map.Entry<Serializable, Integer> entry : map.entrySet()) {
            Integer resultCount = counts.get(entry.getKey());
            if (resultCount == null) {
                resultCount = entry.getValue();
            } else {
                resultCount += entry.getValue();
            }
            counts.put(entry.getKey(), resultCount);
        }
    }

    public HashMap<Serializable, Integer> getResult() {
        return counts;
    }

}
