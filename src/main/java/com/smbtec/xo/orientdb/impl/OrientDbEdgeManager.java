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

import java.util.Iterator;
import java.util.Map;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreRelationManager;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.type.RelationTypeMetadata;
import com.smbtec.xo.orientdb.impl.metadata.EdgeMetadata;
import com.smbtec.xo.orientdb.impl.metadata.PropertyMetadata;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.VertexQuery;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class OrientDbEdgeManager extends AbstractOrientDbPropertyManager<OrientEdge> implements
        DatastoreRelationManager<OrientVertex, Object, OrientEdge, EdgeMetadata, String, PropertyMetadata> {

    private final OrientGraph graph;

    public OrientDbEdgeManager(OrientGraph graph) {
        this.graph = graph;
    }

    @Override
    public boolean isRelation(Object o) {
        return Edge.class.isAssignableFrom(o.getClass());
    }

    @Override
    public String getRelationDiscriminator(OrientEdge relation) {
        return relation.getLabel();
    }

    @Override
    public OrientEdge createRelation(OrientVertex source, RelationTypeMetadata<EdgeMetadata> metadata,
            com.buschmais.xo.spi.metadata.type.RelationTypeMetadata.Direction direction, OrientVertex target,
            Map<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> exampleEntity) {
        final String name = metadata.getDatastoreMetadata().getDiscriminator();
        switch (direction) {
        case FROM:
            return (OrientEdge) source.addEdge(name, target, convertProperties(exampleEntity));
        case TO:
            return (OrientEdge) target.addEdge(name, source, convertProperties(exampleEntity));
        default:
            throw new XOException("Unknown direction '" + direction.name() + "'.");
        }
    }

    @Override
    public void deleteRelation(OrientEdge relation) {
        relation.remove();
    }

    @Override
    public Object getRelationId(OrientEdge relation) {
        return relation.getId();
    }

    @Override
    public void flushRelation(OrientEdge relation) {
        // intentionally left blank
    }

    @Override
    public boolean hasSingleRelation(OrientVertex source, RelationTypeMetadata<EdgeMetadata> metadata,
            com.buschmais.xo.spi.metadata.type.RelationTypeMetadata.Direction direction) {
        final String label = metadata.getDatastoreMetadata().getDiscriminator();
        long count;
        switch (direction) {
        case FROM:
            count = source.query().direction(Direction.OUT).labels(label).count();
            break;
        case TO:
            count = source.query().direction(Direction.IN).labels(label).count();
            break;
        default:
            throw new XOException("Unkown direction '" + direction.name() + "'.");
        }
        if (count > 1) {
            throw new XOException("Multiple results are available.");
        }
        return count == 1;
    }

    @Override
    public OrientEdge getSingleRelation(OrientVertex source, RelationTypeMetadata<EdgeMetadata> metadata,
            com.buschmais.xo.spi.metadata.type.RelationTypeMetadata.Direction direction) {
        final String label = metadata.getDatastoreMetadata().getDiscriminator();
        Iterable<Edge> edges;
        switch (direction) {
        case FROM:
            edges = source.getEdges(Direction.OUT, label);
            break;
        case TO:
            edges = source.getEdges(Direction.IN, label);
            break;
        default:
            throw new XOException("Unkown direction '" + direction.name() + "'.");
        }
        final Iterator<Edge> iterator = edges.iterator();
        if (!iterator.hasNext()) {
            throw new XOException("No result is available.");
        }
        final Edge result = iterator.next();
        if (iterator.hasNext()) {
            throw new XOException("Multiple results are available.");
        }
        return (OrientEdge) result;
    }

    @Override
    public Iterable<OrientEdge> getRelations(OrientVertex source, RelationTypeMetadata<EdgeMetadata> metadata,
            com.buschmais.xo.spi.metadata.type.RelationTypeMetadata.Direction direction) {
        final String label = metadata.getDatastoreMetadata().getDiscriminator();
        VertexQuery query = source.query();
        switch (direction) {
        case TO:
            query = query.direction(Direction.IN);
            break;
        case FROM:
            query = query.direction(Direction.OUT);
            break;
        default:
            throw new XOException("Unknown direction '" + direction.name() + "'.");
        }
        return new OrientDbEdgeIterable(query.labels(label).edges());
    }

    @Override
    public OrientVertex getFrom(final OrientEdge relation) {
        return relation.getVertex(com.tinkerpop.blueprints.Direction.OUT);
    }

    @Override
    public OrientVertex getTo(final OrientEdge relation) {
        return relation.getVertex(com.tinkerpop.blueprints.Direction.IN);
    }

    protected class OrientDbEdgeIterable implements Iterable<OrientEdge> {

        private Iterable<Edge> edges;

        protected OrientDbEdgeIterable(Iterable<Edge> edges) {
            this.edges = edges;
        }

        @Override
        public Iterator<OrientEdge> iterator() {
            return new OrientDbEdgeIterator(edges.iterator());
        }

    }

    protected class OrientDbEdgeIterator implements Iterator<OrientEdge> {

        private Iterator<Edge> edges;

        protected OrientDbEdgeIterator(Iterator<Edge> edges) {
            this.edges = edges;
        }

        @Override
        public boolean hasNext() {
            return edges.hasNext();
        }

        @Override
        public OrientEdge next() {
            return (OrientEdge) edges.next();
        }

        @Override
        public void remove() {
            throw new XOException("Remove operation is not supported for query results.");
        }

    }

}
