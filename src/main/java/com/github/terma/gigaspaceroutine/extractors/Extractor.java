package com.github.terma.gigaspaceroutine.extractors;

import com.gigaspaces.document.SpaceDocument;

import java.io.Serializable;

public interface Extractor<T> extends Serializable {

    T extract(SpaceDocument spaceDocument);

}
