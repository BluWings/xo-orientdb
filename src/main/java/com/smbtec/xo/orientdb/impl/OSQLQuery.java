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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.buschmais.xo.api.ResultIterator;
import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreQuery;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.smbtec.xo.orientdb.api.OrientDbDatastoreSession;
import com.smbtec.xo.orientdb.api.annotation.OSQL;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientElement;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class OSQLQuery implements DatastoreQuery<OSQL> {

    private static final String NODE_COLUMN_NAME = "node";
    private static final String EDGE_COLUMN_NAME = "edge";
    private static final String GRAPH_COLUMN_NAME = "graph";

    private OrientGraph orientGraph;

    public OSQLQuery(OrientDbDatastoreSession<? extends OrientGraph> session) {
        orientGraph = session.getGraph();
    }

    @Override
    public ResultIterator<Map<String, Object>> execute(OSQL query,
            Map<String, Object> parameters) {
        return execute(query.value(), parameters);
    }

    @Override
    public ResultIterator<Map<String, Object>> execute(String query,
            Map<String, Object> parameters) {
        try {
            OCommandSQL sql = new OCommandSQL(query);
            Object result = orientGraph.command(sql).execute();
            return new IterableResultIterator((Iterable<?>) result);
        } catch (Exception e) {
            throw new XOException("Error while executing query", e);
        }
    }

    protected class IterableResultIterator implements
            ResultIterator<Map<String, Object>> {

        private Iterator<?> iterator;

        public IterableResultIterator(Iterable<?> result) {
            iterator = result.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Map<String, Object> next() {
            return entityRepresentation(iterator.next());
        }

        @Override
        public void remove() {
            throw new XOException(
                    "Remove operation is not supported for query results");
        }

        @Override
        public void close() {
        }

    }

    private Map<String, Object> entityRepresentation(Object entity) {
        Map<String, Object> map = new HashMap<>();
        if (entity instanceof OrientVertex) {
            OrientVertex vertex = (OrientVertex) entity;
            if (!vertex.getIdentity().isPersistent()) {
                for (String field : vertex.getPropertyKeys()) {
                    map.put(field,
                            entityRepresentation0(vertex.getProperty(field)));
                }
            } else {
                map.put(NODE_COLUMN_NAME, vertex);
            }
        } else if (entity instanceof OrientEdge) {
            map.put(EDGE_COLUMN_NAME, entity);
        } else if (entity instanceof OrientGraph) {
            map.put(GRAPH_COLUMN_NAME, ((OrientGraph) entity).getRawGraph()
                    .toString());
        } else if (entity instanceof Iterable<?>) {
            map.put("", entityRepresentation0((Iterable<?>) entity));
        } else if (entity instanceof Double || entity instanceof Float) {
            map.put("", ((Number) entity).doubleValue());
        } else if (entity instanceof Long || entity instanceof Integer) {
            map.put("", ((Number) entity).longValue());
        } else if (entity instanceof BigDecimal) {
            map.put("", ((BigDecimal) entity).doubleValue());
        } else if (entity == null) {
            map.put("", null);
        } else {
            map.put("", entity.toString());
        }
        return map;
    }

    private Object entityRepresentation0(Object entity) {
        if (entity instanceof OrientVertex) {
            return entity;
        } else if (entity instanceof OrientEdge) {
            return entity;
        } else if (entity instanceof OrientGraph) {
            return entity;
        } else if (entity instanceof Double || entity instanceof Float) {
            return ((Number) entity);
        } else if (entity instanceof Long || entity instanceof Integer) {
            return ((Number) entity);
        } else if (entity instanceof BigDecimal) {
            return ((Number) entity);
        } else if (entity == null) {
            return null;
        } else if (entity instanceof Iterable<?>) {
            List<Object> list = new ArrayList<>();
            for (Object object : (Iterable<?>) entity) {
                list.add(entityRepresentation0(object));
            }
            return list;
        } else {
            return entity.toString();
        }
    }

}
