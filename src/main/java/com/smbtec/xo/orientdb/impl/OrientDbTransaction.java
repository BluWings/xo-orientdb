package com.smbtec.xo.orientdb.impl;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreTransaction;
import com.tinkerpop.blueprints.TransactionalGraph;

public class OrientDbTransaction implements DatastoreTransaction {

    private boolean active = false;

    private final TransactionalGraph graph;

    public OrientDbTransaction(final TransactionalGraph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph must not be null");
        }
        this.graph = graph;
    }

    @Override
    public void begin() {
        if (active) {
            throw new XOException("There is already an active transaction");
        }
        active = true;
    }

    @Override
    public void commit() {
        active = false;
        graph.commit();
    }

    @Override
    public void rollback() {
        active = false;
        graph.rollback();
    }

    @Override
    public boolean isActive() {
        return active;
    }

}
