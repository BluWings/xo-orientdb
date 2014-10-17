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
package com.smbtec.xo.orientdb.api;

import com.buschmais.xo.spi.datastore.DatastoreSession;

import com.smbtec.xo.orientdb.impl.metadata.EdgeMetadata;
import com.smbtec.xo.orientdb.impl.metadata.PropertyMetadata;
import com.smbtec.xo.orientdb.impl.metadata.VertexMetadata;

import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public interface OrientDbDatastoreSession<G extends OrientGraph>
        extends DatastoreSession<Object, OrientVertex, VertexMetadata, String, Object, OrientEdge, EdgeMetadata, String, PropertyMetadata> {

    G getGraph();

}
