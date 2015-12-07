package com.github.terma.gigaspaceroutine;

import com.gigaspaces.document.SpaceDocument;
import com.gigaspaces.metadata.SpaceTypeDescriptor;
import com.gigaspaces.metadata.SpaceTypeDescriptorBuilder;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openspaces.core.GigaSpace;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class IntCountByPropertyTaskTest {

    private GigaSpace gigaSpace = GigaSpaceUtils.getGigaSpace("/./task");

    public static void registerType(GigaSpace gigaSpace, final String typeName) {
        SpaceTypeDescriptor typeDescriptor = new SpaceTypeDescriptorBuilder(typeName).create();
        gigaSpace.getTypeManager().registerTypeDescriptor(typeDescriptor);
    }

    @Before
    public void clear() {
        gigaSpace.clear(null);

        registerType(gigaSpace, "int-count");
    }

    @Test
    public void emptyResultIfNoData() throws ExecutionException, InterruptedException {
        Map<String, Integer> result = gigaSpace.execute(new IntCountByPropertyTask("agg")).get();

        Assert.assertEquals(0, result.size());
    }

    @Test
    public void countDataByProperty() throws ExecutionException, InterruptedException {
        SpaceDocument spaceDocument1 = new SpaceDocument("int-count");
        spaceDocument1.setProperty("agg", 12);
        gigaSpace.write(spaceDocument1);

        SpaceDocument spaceDocument2 = new SpaceDocument("int-count");
        spaceDocument2.setProperty("agg", true);
        gigaSpace.write(spaceDocument2);

        Map<String, Integer> result = gigaSpace.execute(new IntCountByPropertyTask("agg")).get();

        Assert.assertEquals(2, result.size());
    }

}
