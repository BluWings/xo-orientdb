package com.smbtec.xo.orientdb.api;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(OrientDbXOProvider.class);

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
