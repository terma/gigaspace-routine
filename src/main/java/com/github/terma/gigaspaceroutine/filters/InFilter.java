package com.github.terma.gigaspaceroutine.filters;

import java.util.HashSet;
import java.util.Set;

public class InFilter<T> implements Filter<T> {

    private final HashSet<T> values;

    public InFilter(Set<T> values) {
        this.values = new HashSet<>(values);
    }

    @Override
    public boolean check(T candidate) {
        return values.contains(candidate);
    }

    @Override
    public String toString() {
        return "in " + values;
    }

}
