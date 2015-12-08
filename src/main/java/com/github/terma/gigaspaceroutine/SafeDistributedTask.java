package com.github.terma.gigaspaceroutine;

import com.gigaspaces.async.AsyncResult;
import org.openspaces.core.executor.DistributedTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class SafeDistributedTask<R extends Serializable, T> implements DistributedTask<R, T> {

    @Override
    public T reduce(List<AsyncResult<R>> list) throws Exception {
        List<R> r = new ArrayList<>();
        for (AsyncResult<R> portion : list) {
            if (portion.getException() != null) throw new RuntimeException(portion.getException());
            r.add(portion.getResult());
        }
        return safeReduce(r);
    }

    protected abstract T safeReduce(List<R> list) throws Exception;

}