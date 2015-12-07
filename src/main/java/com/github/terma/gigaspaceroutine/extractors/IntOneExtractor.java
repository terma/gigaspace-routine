package com.github.terma.gigaspaceroutine.extractors;

import com.gigaspaces.document.SpaceDocument;

public class IntOneExtractor implements Extractor<SpaceDocument, Integer> {

    @Override
    public Integer extract(SpaceDocument spaceDocument) {
        return 1;
    }

}
