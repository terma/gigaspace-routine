package com.github.terma.gigaspaceroutine.sources;

import com.gigaspaces.document.SpaceDocument;
import com.gigaspaces.query.IdsQuery;
import com.j_spaces.core.client.SQLQuery;
import org.openspaces.core.GigaSpace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

abstract public class Source implements Serializable {

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
