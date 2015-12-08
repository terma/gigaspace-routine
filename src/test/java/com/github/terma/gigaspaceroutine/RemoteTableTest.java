package com.github.terma.gigaspaceroutine;

import com.gigaspaces.document.SpaceDocument;
import com.github.terma.gigaspaceroutine.extractors.NoExtractor;
import com.github.terma.gigaspaceroutine.extractors.PropertyExtractor;
import com.github.terma.gigaspaceroutine.filters.EqFilter;
import com.github.terma.gigaspaceroutine.sources.Source;
import com.j_spaces.core.client.SQLQuery;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openspaces.core.GigaSpace;

import java.util.*;

public class RemoteTableTest {

    private List<Column> columns = new ArrayList<Column>() {{
        add(new Column(new PropertyExtractor("A"), "A1"));
        add(new Column(new PropertyExtractor("B"), "B1"));
        add(new Column(new PropertyExtractor("C"), "C1"));
    }};

    private GigaSpace gigaSpace;
    private Source source;

    private static void writeDocument(GigaSpace gigaSpace, boolean b, String c) {
        final SpaceDocument spaceDocument = new SpaceDocument("X");
        spaceDocument.setProperty("A", Math.random());
        spaceDocument.setProperty("B", b);
        spaceDocument.setProperty("C", c);
        gigaSpace.write(spaceDocument);
    }

    @Before
    public void init() {
        gigaSpace = GigaSpaceUtils.getGigaSpace("/./table");
        gigaSpace.clear(null);
        GigaSpaceUtils.registerType(gigaSpace, "X");
        writeDocument(gigaSpace, true, "Z");
        writeDocument(gigaSpace, false, "A");

        source = Source.fromQueryByIds(new SQLQuery<SpaceDocument>("X", ""));
    }

    @Test
    public void fetchData() {
        List<Map<String, Object>> data = new RemoteTable("A", gigaSpace, source, columns).fetch();
        Assert.assertEquals(2, data.size());
        Assert.assertEquals(3, data.get(0).size());
    }

    @Test
    public void fetchFilteredData() {
        List<Map<String, Object>> data = new RemoteTable("A", gigaSpace, source, columns)
                .addFilter("B1", new EqFilter(true))
                .fetch();
        Assert.assertEquals(1, data.size());
    }

    @Test
    public void fetchFilteredAndSortedData() {
        gigaSpace.clear(null);
        writeDocument(gigaSpace, true, "Z");
        writeDocument(gigaSpace, false, "Z");
        writeDocument(gigaSpace, false, "A");
        writeDocument(gigaSpace, false, "E");

        List<Map<String, Object>> data = new RemoteTable("A", gigaSpace, source, columns)
                .addFilter("B1", new EqFilter(false))
                .sortBy("C1").fetch();

        Assert.assertEquals(3, data.size());
        Assert.assertEquals("A", data.get(0).get("C1"));
        Assert.assertEquals("E", data.get(1).get("C1"));
        Assert.assertEquals("Z", data.get(2).get("C1"));
    }

    @Test
    public void fetchJoinedData() {
        // given
        gigaSpace.clear(null);
        writeDocument(gigaSpace, false, "Z");
        writeDocument(gigaSpace, false, "A");
        writeDocument(gigaSpace, false, "E");
        Table tableA = new LocalTable(Collections.<Column>emptyList(), Arrays.asList(
                (Map<String, Object>) new HashMap<String, Object>() {{
                    put("name", "Roma");
                    put("id", "Z");
                }}, new HashMap<String, Object>() {{
                    put("name", "Oma");
                    put("id", "A");
                }}));

        // when
        List<Map<String, Object>> data = new RemoteTable("A", gigaSpace, source, columns)
                .join("C1", tableA, "id")
                .fetch();

        // then
        Assert.assertEquals(3, data.size());
        Assert.assertEquals("Roma", data.get(0).get("name"));
        Assert.assertEquals("Oma", data.get(1).get("name"));
        Assert.assertEquals(null, data.get(2).get("name"));
    }

    @Test
    public void fetchJoinedAndSorted() {
        // given
        gigaSpace.clear(null);
        writeDocument(gigaSpace, false, "Z");
        writeDocument(gigaSpace, false, "A");
        writeDocument(gigaSpace, false, "E");
        Table tableA = new LocalTable(
                Arrays.asList(
                        new Column(NoExtractor.get(), "id"),
                        new Column(NoExtractor.get(), "name")),
                Arrays.asList(
                        (Map<String, Object>) new HashMap<String, Object>() {{
                            put("name", "Zoma");
                            put("id", "Z");
                        }}, new HashMap<String, Object>() {{
                            put("name", "Aoma");
                            put("id", "A");
                        }}, new HashMap<String, Object>() {{
                            put("name", "Ooma");
                            put("id", "E");
                        }}));

        // when
        List<Map<String, Object>> data = new RemoteTable("A", gigaSpace, source, columns)
                .join("C1", tableA, "id")
                .sortBy("name")
                .fetch();

        // then
        Assert.assertEquals(3, data.size());
        Assert.assertEquals("Aoma", data.get(0).get("name"));
        Assert.assertEquals("Ooma", data.get(1).get("name"));
        Assert.assertEquals("Zoma", data.get(2).get("name"));

    }

    @Test
    public void fetchFilteredByJoin() {
        // given
        gigaSpace.clear(null);
        writeDocument(gigaSpace, false, "Z");
        writeDocument(gigaSpace, false, "A");
        writeDocument(gigaSpace, false, "E");
        Table tableA = new LocalTable(
                Arrays.asList(
                        new Column(NoExtractor.get(), "id"),
                        new Column(NoExtractor.get(), "name")),
                Arrays.asList(
                        (Map<String, Object>) new HashMap<String, Object>() {{
                            put("name", "Zoma");
                            put("id", "Z");
                        }}, new HashMap<String, Object>() {{
                            put("name", "Aoma");
                            put("id", "A");
                        }}, new HashMap<String, Object>() {{
                            put("name", "Ooma");
                            put("id", "E");
                        }}));

        // when
        List<Map<String, Object>> data = new RemoteTable("A", gigaSpace, source, columns)
                .join("C1", tableA, "id")
                .addFilter("name", new EqFilter("Aoma"))
                .fetch();

        // then
        Assert.assertEquals(1, data.size());
    }

    @Test
    public void fetchFirstNSortedAndFiltered() {
        // given
        gigaSpace.clear(null);
        writeDocument(gigaSpace, false, "Z");
        writeDocument(gigaSpace, false, "Z");
        writeDocument(gigaSpace, false, "Z");

        // when
        List<Map<String, Object>> data = new RemoteTable("A", gigaSpace, source, columns)
                .addFilter("C1", new EqFilter("Z"))
                .sortBy("C1")
                .fetch(1);

        // then
        Assert.assertEquals(1, data.size());
    }

    // todo support fetch by batch

}
