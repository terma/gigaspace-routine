package com.github.terma.gigaspaceroutine.sources;

import com.gigaspaces.document.SpaceDocument;
import com.gigaspaces.query.IdsQuery;
import org.openspaces.core.GigaSpace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
