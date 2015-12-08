package com.github.terma.gigaspaceroutine.tasks;

import com.gigaspaces.async.AsyncResult;
import org.openspaces.core.executor.DistributedTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class ListDistributedTask<T extends Serializable> implements DistributedTask<ArrayList<T>, List<T>> {

    @Override
    public List<T> reduce(List<AsyncResult<ArrayList<T>>> list) throws Exception {
        List<T> r = new ArrayList<>();
        for (AsyncResult<ArrayList<T>> portion : list) {
            if (portion.getException() != null) throw new RuntimeException(portion.getException());
            r.addAll(portion.getResult());
        }
        return r;
    }

}