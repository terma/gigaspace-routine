package com.github.terma.gigaspaceroutine;

import com.gigaspaces.async.AsyncResult;
import com.gigaspaces.document.SpaceDocument;
import com.gigaspaces.query.IdsQuery;
import com.github.terma.gigaspaceroutine.comparators.LeftToRightComparator;
import com.github.terma.gigaspaceroutine.comparators.StringKeyComparator;
import com.github.terma.gigaspaceroutine.extractors.PropertyExtractor;
import com.github.terma.gigaspaceroutine.filters.Filter;
import com.j_spaces.core.client.SQLQuery;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.executor.DistributedTask;
import org.openspaces.core.executor.TaskGigaSpace;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class RemoteTable implements Table {

    private final String idProperty;
    private final GigaSpace gigaSpace;
    private final Source source;
    private final List<Column> columns;
    private final Map<String, Filter> filters = new HashMap<>();
    private final List<Join> joins = new ArrayList<>();
    private List<String> sortBy;

    public RemoteTable(String idProperty, GigaSpace gigaSpace, Source source, List<Column> columns) {
        this.idProperty = idProperty;
        this.gigaSpace = gigaSpace;
        if (source == null) throw new IllegalArgumentException("Need not null source!");

        this.source = source;
        this.columns = columns;
    }

    private static <K, V> Map<V, List<Map<K, V>>> groupBy(List<Map<K, V>> list, K key) {
        Map<V, List<Map<K, V>>> grouped = new HashMap<>();
        for (Map<K, V> item : list) {
            V keyValue = item.get(key);
            List<Map<K, V>> v = grouped.get(keyValue);
            if (v == null) {
                v = new ArrayList<>();
                grouped.put(keyValue, v);
            }

            v.add(item);
        }
        return grouped;
    }

    public RemoteTable addFilter(String columnName, Filter filter) {
        filters.put(columnName, filter);
        return this;
    }

    private List<Column> getColumnsForSort() {
        List<Column> result = new ArrayList<>();
        result.add(new Column(new PropertyExtractor(idProperty), idProperty));

        Set<String> joinNames = getJoinColumnNames();
        for (String s : sortBy) {
            if (joinNames.contains(s)) continue; // skip join columns
            result.add(new Columns(getColumns()).find(s));
        }

        for (Join join : joins) {
            result.add(new Columns(getColumns()).find(join.columnName));
        }

        return result;
    }

    private Set<String> getJoinColumnNames() {
        Set<String> result = new HashSet<>();
        for (Join join : joins) result.addAll(new Columns(join.joinTable.getColumns()).getNames());
        return result;
    }

    @Override
    public List<Map<String, Object>> fetch() {
        if (sortBy != null) {
            // need to sort
            List<Column> sortColumnsForFetch = getColumnsForSort();
            List<Map<String, Object>> dataForSort = safeFetch(source, columns, sortColumnsForFetch, filters);

            join(dataForSort);

            List<Comparator<Map<String, Object>>> c = new ArrayList<>();
            for (final String s : sortBy) c.add(new StringKeyComparator(s));
            Collections.sort(dataForSort, new LeftToRightComparator<>(c));

            List<Serializable> uids = new ArrayList<>();
            for (Map<String, Object> d : dataForSort) uids.add((Serializable) d.get(idProperty));

            Source idsSource = source.toIds(uids);
            return safeFetch(idsSource, columns, columns, Collections.<String, Filter>emptyMap());
        } else {
            return safeFetch(source, columns, columns, filters);
        }
    }

    @Override
    public List<Column> getColumns() {
        return columns;
    }

    private List<Map<String, Object>> safeFetch(
            Source source, List<Column> allColumns, List<Column> fetchColumns,
            Map<String, Filter> filters) {
        List<Map<String, Object>> data;
        try {
            data = gigaSpace.execute(new RemoteTableFetchTask(source, allColumns, fetchColumns, filters)).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        join(data);

        return data;
    }

    private void join(List<Map<String, Object>> data) {
        for (final Join join : joins) {
            final List<Map<String, Object>> joinData = join.joinTable.fetch();
            final Map<Object, List<Map<String, Object>>> joinMap = groupBy(joinData, join.joinColumnName);

            for (Map<String, Object> item : data) {
                Object joinValue = item.get(join.columnName);

                List<Map<String, Object>> candidates = joinMap.get(joinValue);
                if (candidates != null && candidates.size() > 0) {
                    for (Map.Entry<String, Object> entry : candidates.get(0).entrySet()) {
                        item.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
    }

    public RemoteTable sortBy(final String firstColumnName, final String... columnNames) {
        sortBy = new ArrayList<String>() {{
            add(firstColumnName);
            addAll(Arrays.asList(columnNames));
        }};
        return this;
    }

    public RemoteTable join(String f, Table joinTable, String joinColumnName) {
        joins.add(new Join(f, joinTable, joinColumnName));
        return this;
    }

    private static class Join {

        public final String columnName;
        public final Table joinTable;
        public final String joinColumnName;

        Join(String columnName, Table joinTable, String joinColumnName) {
            this.columnName = columnName;
            this.joinTable = joinTable;
            this.joinColumnName = joinColumnName;
        }
    }

}

class RemoteTableFetchTask implements DistributedTask<ArrayList<HashMap<String, Serializable>>, List<Map<String, Object>>> {

    private final Source source;
    private final ArrayList<Column> allColumns;
    private final ArrayList<Column> fetchColumns;
    private final HashMap<String, Filter> filters;

    @TaskGigaSpace
    private GigaSpace gigaSpace;

    RemoteTableFetchTask(
            Source source, List<Column> allColumns,
            List<Column> fetchColumns, Map<String, Filter> filters) {
        this.source = source;
        this.allColumns = new ArrayList<>(allColumns);
        this.fetchColumns = new ArrayList<>(fetchColumns);
        this.filters = new HashMap<>(filters);
    }

    @Override
    public List<Map<String, Object>> reduce(List<AsyncResult<ArrayList<HashMap<String, Serializable>>>> list) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        for (AsyncResult<ArrayList<HashMap<String, Serializable>>> part : list) {
            if (part.getException() != null) throw new RuntimeException(part.getException());
            result.addAll((List<Map<String, Object>>) ((Object) part.getResult()));
        }
        return result;
    }

    @Override
    public ArrayList<HashMap<String, Serializable>> execute() throws Exception {
        ArrayList result = new ArrayList();
        tt:
        for (SpaceDocument spaceDocument : source.fetch(gigaSpace)) {
            for (Map.Entry<String, Filter> filter : filters.entrySet()) {
                Object value = new Columns(allColumns).find(filter.getKey()).getExtractor().extract(spaceDocument);
                if (!filter.getValue().check(value)) continue tt;
            }

            HashMap hashMap = new HashMap();
            for (Column column : fetchColumns) {
                Object value = column.getExtractor().extract(spaceDocument);
                hashMap.put(column.getName(), value);
            }
            result.add(hashMap);
        }
        return result;
    }
}

abstract class Source implements Serializable {

    public static Source fromList(List list) {
        return null;
    }

    public static Source fromQueryByIds(IdsQuery query) {
        return new IdsSource(query);
    }

    public static Source fromQueryByIds(SQLQuery<SpaceDocument> query) {
        return new QuerySource(query);
    }

    public abstract ArrayList<SpaceDocument> fetch(GigaSpace gigaSpace);

    public abstract Source toIds(List<Serializable> uids);

}

class QuerySource extends Source {

    private SQLQuery<SpaceDocument> query;

    public QuerySource(SQLQuery query) {
        this.query = query;
    }

    public ArrayList<SpaceDocument> fetch(GigaSpace gigaSpace) {
        return new ArrayList<>(Arrays.asList(gigaSpace.readMultiple(query)));
    }

    @Override
    public Source toIds(List<Serializable> ids) {
        return new IdsSource(new IdsQuery<SpaceDocument>(query.getTypeName(), ids.toArray()));
    }

}

class IdsSource extends Source {

    private final IdsQuery<SpaceDocument> idsQuery;

    public IdsSource(IdsQuery<SpaceDocument> query) {
        this.idsQuery = query;
    }

    public ArrayList<SpaceDocument> fetch(GigaSpace gigaSpace) {
        return new ArrayList<>(Arrays.asList(gigaSpace.readByIds(idsQuery).getResultsArray()));
    }

    @Override
    public Source toIds(List<Serializable> ids) {
        throw new UnsupportedOperationException();
    }

}

