package com.smbtec.xo.orientdb.impl;

import com.buschmais.xo.spi.datastore.DatastoreRelationManager;
import com.buschmais.xo.spi.metadata.type.RelationTypeMetadata;
import com.smbtec.xo.orientdb.impl.metadata.EdgeMetadata;
import com.smbtec.xo.orientdb.impl.metadata.PropertyMetadata;

import com.tinkerpop.blueprints.Direction;

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
        return OrientEdge.class.isAssignableFrom(o.getClass());
    }

    @Override
    public String getRelationDiscriminator(OrientEdge relation) {
        return relation.getLabel();
    }

    @Override
    public OrientEdge createRelation(OrientVertex source, RelationTypeMetadata<EdgeMetadata> metadata, RelationTypeMetadata.Direction direction,
            OrientVertex target) {
        final String name = metadata.getDatastoreMetadata().getDiscriminator();
        return source.addEdge(name, target, null, null);
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
    public boolean hasSingleRelation(OrientVertex source, RelationTypeMetadata<EdgeMetadata> metadata, RelationTypeMetadata.Direction direction) {
        return false;
        // final String label =
        // metadata.getDatastoreMetadata().getDiscriminator();
        // long count;
        // switch (direction) {
        // case FROM:
        // count =
        // source.query().direction(Direction.OUT).labels(label).count();
        // break;
        // case TO:
        // count = source.query().direction(Direction.IN).labels(label).count();
        // break;
        // default:
        // throw new XOException("Unkown direction '" + direction.name() +
        // "'.");
        // }
        // if (count > 1) {
        // throw new XOException("Multiple results are available.");
        // }
        // return count == 1;
    }

    @Override
    public OrientEdge getSingleRelation(OrientVertex source, RelationTypeMetadata<EdgeMetadata> metadata, RelationTypeMetadata.Direction direction) {
        return null;
        // final String label =
        // metadata.getDatastoreMetadata().getDiscriminator();
        // Iterable<Edge> edges;
        // switch (direction) {
        // case FROM:
        // edges = source.getEdges(Direction.OUT, label);
        // break;
        // case TO:
        // edges = source.getEdges(Direction.IN, label);
        // break;
        // default:
        // throw new XOException("Unkown direction '" + direction.name() +
        // "'.");
        // }
        // final Iterator<Edge> iterator = edges.iterator();
        // if (!iterator.hasNext()) {
        // throw new XOException("No result is available.");
        // }
        // final Edge result = iterator.next();
        // if (iterator.hasNext()) {
        // throw new XOException("Multiple results are available.");
        // }
        // return result;
    }

    @Override
    public Iterable<OrientEdge> getRelations(OrientVertex source, RelationTypeMetadata<EdgeMetadata> metadata, RelationTypeMetadata.Direction direction) {
        return null;
        // final String label =
        // metadata.getDatastoreMetadata().getDiscriminator();
        // VertexQuery query = source.query();
        // switch (direction) {
        // case TO:
        // query = query.direction(Direction.IN);
        // break;
        // case FROM:
        // query = query.direction(Direction.OUT);
        // break;
        // default:
        // throw new XOException("Unknown direction '" + direction.name() +
        // "'.");
        // }
        // return query.labels(label).edges();
    }

    @Override
    public OrientVertex getFrom(OrientEdge relation) {
        return relation.getVertex(Direction.IN);
    }

    @Override
    public OrientVertex getTo(OrientEdge relation) {
        return relation.getVertex(Direction.OUT);
    }

}
