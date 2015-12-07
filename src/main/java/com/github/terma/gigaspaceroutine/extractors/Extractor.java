package com.github.terma.gigaspaceroutine.extractors;

import java.io.Serializable;

public interface Extractor<I, O> extends Serializable {

    O extract(I input);

}
