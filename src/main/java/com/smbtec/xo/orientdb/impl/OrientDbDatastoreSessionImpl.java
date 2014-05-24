package com.smbtec.xo.orientdb.impl;

import java.lang.annotation.Annotation;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreEntityManager;
import com.buschmais.xo.spi.datastore.DatastoreQuery;
import com.buschmais.xo.spi.datastore.DatastoreRelationManager;
import com.buschmais.xo.spi.datastore.DatastoreTransaction;

import com.smbtec.xo.orientdb.api.OrientDbDatastoreSession;
import com.smbtec.xo.orientdb.api.annotation.OSQL;
import com.smbtec.xo.orientdb.impl.metadata.EdgeMetadata;
import com.smbtec.xo.orientdb.impl.metadata.PropertyMetadata;
import com.smbtec.xo.orientdb.impl.metadata.VertexMetadata;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class OrientDbDatastoreSessionImpl implements OrientDbDatastoreSession<OrientGraph> {

    private final OrientGraph graph;
    private final OrientDbVertexManager vertexManager;
    private final OrientDbEdgeManager edgeManager;

    public OrientDbDatastoreSessionImpl(final OrientGraph graph) {
        this.graph = graph;
        this.vertexManager = new OrientDbVertexManager(graph);
        this.edgeManager = new OrientDbEdgeManager(graph);
    }

    @Override
    public DatastoreTransaction getDatastoreTransaction() {
        return new OrientDbTransaction(graph);
    }

    @Override
    public DatastoreEntityManager<Object, Vertex, VertexMetadata, String, PropertyMetadata> getDatastoreEntityManager() {
        return vertexManager;
    }

    @Override
    public DatastoreRelationManager<Vertex, Object, Edge, EdgeMetadata, String, PropertyMetadata> getDatastoreRelationManager() {
        return edgeManager;
    }

    @Override
    public Class<? extends Annotation> getDefaultQueryLanguage() {
        return OSQL.class;
    }

    @Override
    public <QL extends Annotation> DatastoreQuery<QL> createQuery(Class<QL> queryLanguage) {
        if (OSQL.class.equals(queryLanguage)) {
            return (DatastoreQuery<QL>) new OSQLQuery(this);
        }
        throw new XOException("Unsupported query language: " + queryLanguage.getName());
    }

    @Override
    public OrientGraph getGraph() {
        return graph;
    }

    @Override
    public void close() {
        // intentionally left blank
    }

}
