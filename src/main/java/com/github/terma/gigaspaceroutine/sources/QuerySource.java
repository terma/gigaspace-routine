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
