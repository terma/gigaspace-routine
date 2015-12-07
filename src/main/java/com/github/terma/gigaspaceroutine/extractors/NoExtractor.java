package com.github.terma.gigaspaceroutine.extractors;

public class NoExtractor<I, O> implements Extractor<I, O> {

    public static <I, O> Extractor<I, O> get() {
        return new NoExtractor<>();
    }

    @Override
    public O extract(I input) {
        throw new UnsupportedOperationException();
    }

}
