package com.github.terma.gigaspaceroutine.filters;

public class EqFilter<T> implements Filter<T> {

    private final T value;

    public EqFilter(T value) {
        this.value = value;
    }

    @Override
    public boolean check(T candidate) {
        return value.equals(candidate);
    }
}
