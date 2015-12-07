package com.github.terma.gigaspaceroutine.filters;

import java.io.Serializable;

public interface Filter<T> extends Serializable {

    boolean check(T candidate);

}
