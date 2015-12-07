package com.github.terma.gigaspaceroutine.extractors;

import com.gigaspaces.document.SpaceDocument;

public class PropertyExtractor implements Extractor<Object> {

    private final String property;

    public PropertyExtractor(String property) {
        this.property = property;
    }

    @Override
    public Object extract(SpaceDocument spaceDocument) {
        return spaceDocument.getProperty(property);
    }
}
