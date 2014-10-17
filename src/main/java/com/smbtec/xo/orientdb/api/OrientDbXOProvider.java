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

import java.net.URI;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.buschmais.xo.spi.bootstrap.XODatastoreProvider;
import com.buschmais.xo.spi.datastore.Datastore;
import com.smbtec.xo.orientdb.impl.OrientDbDatastore;
import com.smbtec.xo.orientdb.impl.metadata.EdgeMetadata;
import com.smbtec.xo.orientdb.impl.metadata.VertexMetadata;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class OrientDbXOProvider implements XODatastoreProvider<VertexMetadata, String, EdgeMetadata, String> {

    @Override
    public Datastore<OrientDbDatastoreSession<OrientGraph>, VertexMetadata, String, EdgeMetadata, String> createDatastore(
            final XOUnit xoUnit) {
        if (xoUnit == null) {
            throw new IllegalArgumentException("XOUnit must not be null");
        }
        final URI uri = xoUnit.getUri();
        if (uri == null) {
            throw new XOException("No URI is specified for the store.");
        }
        return new OrientDbDatastore(uri);
    }

}
