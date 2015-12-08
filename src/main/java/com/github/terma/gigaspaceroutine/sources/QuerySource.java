package com.github.terma.gigaspaceroutine.sources;

import com.gigaspaces.document.SpaceDocument;
import com.gigaspaces.query.IdsQuery;
import com.j_spaces.core.client.SQLQuery;
import org.openspaces.core.GigaSpace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
