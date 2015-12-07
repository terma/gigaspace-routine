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
