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

import com.gigaspaces.async.AsyncResult;
import com.gigaspaces.document.SpaceDocument;
import com.j_spaces.core.client.SQLQuery;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.executor.DistributedTask;
import org.openspaces.core.executor.TaskGigaSpace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntCountByPropertyTask implements
        DistributedTask<HashMap<String, Integer>, Map<String, Integer>> {

    private final String aggregateProperty;

    @TaskGigaSpace
    private transient GigaSpace gigaSpace;

    public IntCountByPropertyTask(final String aggregateProperty) {
        this.aggregateProperty = aggregateProperty;
    }

    @Override
    public Map<String, Integer> reduce(List<AsyncResult<HashMap<String, Integer>>> list) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        for (final AsyncResult<HashMap<String, Integer>> asyncResult : list) {
            if (asyncResult.getException() != null) throw asyncResult.getException();

            for (Map.Entry<String, Integer> portionEntry : asyncResult.getResult().entrySet()) {
                Integer resultCount = result.get(portionEntry.getKey());
                if (resultCount == null) {
                    resultCount = portionEntry.getValue();
                } else {
                    resultCount += portionEntry.getValue();
                }
                result.put(portionEntry.getKey(), resultCount);
            }
        }
        return result;
    }

    @Override
    public HashMap<String, Integer> execute() throws Exception {
        final SpaceDocument[] spaceDocuments = gigaSpace.readMultiple(new SQLQuery<SpaceDocument>());
        return new HashMap<>();
    }

}
