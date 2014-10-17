/*
 * eXtended Objects - OrientDb Binding
 *
 * Copyright (C) 2014 SMB GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.smbtec.xo.orientdb.impl;

import java.net.URI;
import java.util.Collection;

import com.buschmais.xo.spi.datastore.Datastore;
import com.buschmais.xo.spi.datastore.DatastoreMetadataFactory;
import com.buschmais.xo.spi.metadata.type.TypeMetadata;

import com.smbtec.xo.orientdb.api.OrientDbDatastoreSession;
import com.smbtec.xo.orientdb.impl.metadata.EdgeMetadata;
import com.smbtec.xo.orientdb.impl.metadata.VertexMetadata;

import com.tinkerpop.blueprints.impls.orient.OrientGraph;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class OrientDbDatastore implements
        Datastore<OrientDbDatastoreSession<OrientGraph>, VertexMetadata, String, EdgeMetadata, String> {

    private OrientGraph graph;

    public OrientDbDatastore(final URI uri) {
        this.graph = new OrientGraph(uri.toString());
    }

    @Override
    public void init(final Collection<TypeMetadata> registeredMetadata) {
    }

    @Override
    public DatastoreMetadataFactory<VertexMetadata, String, EdgeMetadata, String> getMetadataFactory() {
        return new OrientDbMetadataFactory();
    }

    @Override
    public OrientDbDatastoreSession<OrientGraph> createSession() {
        return new OrientDbDatastoreSessionImpl(graph);
    }

    @Override
    public void close() {
        graph.shutdown();
        graph = null;
    }

}
