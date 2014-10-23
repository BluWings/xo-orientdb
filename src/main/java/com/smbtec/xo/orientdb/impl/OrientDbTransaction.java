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

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreTransaction;
import com.tinkerpop.blueprints.TransactionalGraph;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
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
