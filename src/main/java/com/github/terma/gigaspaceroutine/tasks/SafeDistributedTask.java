/*
Copyright 2015 Artem Stasiuk
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.github.terma.gigaspaceroutine.tasks;

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