package com.smbtec.xo.orientdb.impl;

import java.util.Iterator;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastorePropertyManager;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.type.RelationTypeMetadata;
import com.smbtec.xo.orientdb.impl.metadata.EdgeMetadata;
import com.smbtec.xo.orientdb.impl.metadata.PropertyMetadata;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.VertexQuery;

public class OrientDbPropertyManager implements DatastorePropertyManager<Vertex, Edge, PropertyMetadata, EdgeMetadata> {

    @Override
    public void setEntityProperty(final Vertex entity,
            final PrimitivePropertyMethodMetadata<PropertyMetadata> metadata, final Object value) {
        entity.setProperty(metadata.getDatastoreMetadata().getName(), value);
    }

    @Override
    public void setRelationProperty(final Edge relation,
            final PrimitivePropertyMethodMetadata<PropertyMetadata> metadata, final Object value) {
        relation.setProperty(metadata.getDatastoreMetadata().getName(), value);
    }

    @Override
    public boolean hasEntityProperty(final Vertex entity,
            final PrimitivePropertyMethodMetadata<PropertyMetadata> metadata) {
        return entity.getProperty(metadata.getDatastoreMetadata().getName()) != null;
    }

    @Override
    public boolean hasRelationProperty(final Edge relation,
            final PrimitivePropertyMethodMetadata<PropertyMetadata> metadata) {
        return relation.getProperty(metadata.getDatastoreMetadata().getName()) != null;
    }

    @Override
    public void removeEntityProperty(final Vertex entity,
            final PrimitivePropertyMethodMetadata<PropertyMetadata> metadata) {
        entity.removeProperty(metadata.getDatastoreMetadata().getName());
    }

    @Override
    public void removeRelationProperty(final Edge relation,
            final PrimitivePropertyMethodMetadata<PropertyMetadata> metadata) {
        relation.removeProperty(metadata.getDatastoreMetadata().getName());
    }

    @Override
    public Object getEntityProperty(final Vertex entity,
            final PrimitivePropertyMethodMetadata<PropertyMetadata> metadata) {
        return entity.getProperty(metadata.getDatastoreMetadata().getName());
    }

    @Override
    public Object getRelationProperty(final Edge relation,
            final PrimitivePropertyMethodMetadata<PropertyMetadata> metadata) {
        return relation.getProperty(metadata.getDatastoreMetadata().getName());
    }

    @Override
    public boolean hasSingleRelation(final Vertex source, final RelationTypeMetadata<EdgeMetadata> metadata,
            final RelationTypeMetadata.Direction direction) {
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
    public Edge getSingleRelation(final Vertex source, final RelationTypeMetadata<EdgeMetadata> metadata,
            final RelationTypeMetadata.Direction direction) {
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
        return result;
    }

    @Override
    public Iterable<Edge> getRelations(final Vertex source, final RelationTypeMetadata<EdgeMetadata> metadata,
            final RelationTypeMetadata.Direction direction) {
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
        return query.labels(label).edges();
    }

    @Override
    public Edge createRelation(final Vertex source, final RelationTypeMetadata<EdgeMetadata> metadata,
            final RelationTypeMetadata.Direction direction, final Vertex target) {
        final String name = metadata.getDatastoreMetadata().getDiscriminator();
        switch (direction) {
        case FROM:
            return source.addEdge(name, target);
        case TO:
            return target.addEdge(name, source);
        default:
            throw new XOException("Unknown direction '" + direction.name() + "'.");
        }
    }

    @Override
    public void deleteRelation(final Edge relation) {
        relation.remove();
    }

    @Override
    public Vertex getFrom(final Edge relation) {
        return relation.getVertex(com.tinkerpop.blueprints.Direction.IN);
    }

    @Override
    public Vertex getTo(final Edge relation) {
        return relation.getVertex(com.tinkerpop.blueprints.Direction.OUT);
    }

    private Direction getDirection(final RelationTypeMetadata.Direction direction) {
        switch (direction) {
        case TO:
            return Direction.IN;
        case FROM:
            return Direction.OUT;
        default:
            throw new XOException("Unknown direction '" + direction.name() + "'.");
        }
    }

}
