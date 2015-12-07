package com.github.terma.gigaspaceroutine.extractors;

import com.gigaspaces.document.SpaceDocument;
import com.github.terma.gigaspaceroutine.extractors.Extractor;

public class IntOneExtractor implements Extractor<Integer> {

    @Override
    public Integer extract(SpaceDocument spaceDocument) {
        return 1;
    }

}
