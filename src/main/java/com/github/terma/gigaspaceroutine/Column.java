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
