package com.github.terma.gigaspaceroutine.comparators;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class LeftToRightComparator<T> implements Comparator<T> {

    private final List<Comparator<T>> comparators;

    public LeftToRightComparator(List<Comparator<T>> comparators) {
        this.comparators = comparators;
    }

    @Override
    public int compare(T o1, T o2) {
        for (Comparator<T> comparator : comparators) {
            int v = comparator.compare(o1, o2);
            if (v != 0) return v;
        }
        return 0;
    }

}

