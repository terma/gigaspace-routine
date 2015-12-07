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
