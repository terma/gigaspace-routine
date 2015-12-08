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

package com.github.terma.gigaspaceroutine;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.executor.DistributedTask;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

public class GigaSpaceRoutine {

    public static <R extends Serializable, T> T execute(
            final GigaSpace gigaSpace, final DistributedTask<R, T> distributedTask) {
        try {
            return gigaSpace.execute(distributedTask).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
