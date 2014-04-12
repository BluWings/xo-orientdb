package com.smbtec.xo.orientdb.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.buschmais.xo.api.ResultIterator;
import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastorePropertyManager;
import com.buschmais.xo.spi.datastore.DatastoreTransaction;
import com.buschmais.xo.spi.datastore.TypeMetadataSet;
import com.buschmais.xo.spi.metadata.method.IndexedPropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.type.EntityTypeMetadata;
import com.smbtec.xo.orientdb.api.OrientDbDatastoreSession;
import com.smbtec.xo.orientdb.impl.metadata.EdgeMetadata;
import com.smbtec.xo.orientdb.impl.metadata.PropertyMetadata;
import com.smbtec.xo.orientdb.impl.metadata.VertexMetadata;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.GraphQuery;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

/**
 * 
 * @author Lars Martin - lars.martin@smb-tec.com
 * 
 */
public class OrientDbDatastoreSessionImpl implements OrientDbDatastoreSession<OrientGraph> {

	/**
	 * This constant contains the prefix for discriminator properties.
	 */
	public static final String XO_DISCRIMINATORS_PROPERTY = "_xo_discriminator_";

	private OrientGraph graph;

	public OrientDbDatastoreSessionImpl(OrientGraph graph) {
		this.graph = graph;
	}

	@Override
	public DatastoreTransaction getDatastoreTransaction() {
		return new OrientDbTransaction(graph);
	}

	@Override
	public boolean isEntity(Object o) {
		return Vertex.class.isAssignableFrom(o.getClass());
	}

	@Override
	public boolean isRelation(Object o) {
		return Edge.class.isAssignableFrom(o.getClass());
	}

	@Override
	public Set<String> getEntityDiscriminators(Vertex entity) {
		final Set<String> discriminators = new HashSet<>();
		for (final String key : entity.getPropertyKeys()) {
			if (key.startsWith(XO_DISCRIMINATORS_PROPERTY)) {
				final String discriminator = entity.getProperty(key);
				discriminators.add(discriminator);
			}
		}
		if (discriminators.size() == 0) {
			throw new XOException(
					"A vertex was found without discriminators. Does another framework alter the database?");
		}
		return discriminators;
	}

	@Override
	public String getRelationDiscriminator(Edge relation) {
		return relation.getLabel();
	}

	@Override
	public Object getEntityId(Vertex entity) {
		return entity.getId();
	}

	@Override
	public Object getRelationId(Edge relation) {
		return relation.getId();
	}

	@Override
	public Vertex createEntity(TypeMetadataSet<EntityTypeMetadata<VertexMetadata>> types, Set<String> discriminators) {
		Vertex vertex = graph.addVertex(null);
		for (final String discriminator : discriminators) {
			vertex.setProperty(XO_DISCRIMINATORS_PROPERTY + discriminator, discriminator);
		}
		return vertex;
	}

	@Override
	public void deleteEntity(Vertex entity) {
		entity.remove();
	}

	@Override
	public ResultIterator<Vertex> findEntity(EntityTypeMetadata<VertexMetadata> type, String discriminator, Object value) {
        GraphQuery query = graph.query();
        query = query.has(XO_DISCRIMINATORS_PROPERTY + discriminator);

        IndexedPropertyMethodMetadata<?> indexedProperty = type.getDatastoreMetadata().getIndexedProperty();
        if (indexedProperty == null) {
            indexedProperty = type.getIndexedProperty();
        }
        if (indexedProperty == null) {
            throw new XOException("Type " + type.getAnnotatedType().getAnnotatedElement().getName() + " has no indexed property.");
        }
        final PrimitivePropertyMethodMetadata<PropertyMetadata> propertyMethodMetadata = indexedProperty.getPropertyMethodMetadata();
        query = query.has(propertyMethodMetadata.getDatastoreMetadata().getName(), value);
        final Iterable<Vertex> vertices = query.vertices();
        final Iterator<Vertex> iterator = vertices.iterator();

        return new ResultIterator<Vertex>() {

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Vertex next() {
                return iterator.next();
            }

            @Override
            public void remove() {
                iterator.remove();
            }

            @Override
            public void close() {
                // intentionally left empty
            }
        };
	}

	@Override
	public <QL> ResultIterator<Map<String, Object>> executeQuery(QL query, Map<String, Object> parameters) {
		return null;
	}

	@Override
	public void migrateEntity(Vertex entity, TypeMetadataSet<EntityTypeMetadata<VertexMetadata>> types,
			Set<String> discriminators, TypeMetadataSet<EntityTypeMetadata<VertexMetadata>> targetTypes,
			Set<String> targetDiscriminators) {
	}

	@Override
	public void flushEntity(Vertex entity) {
	}

	@Override
	public void flushRelation(Edge relation) {
	}

	@Override
	public DatastorePropertyManager<Vertex, Edge, PropertyMetadata, EdgeMetadata> getDatastorePropertyManager() {
		return new OrientDbPropertyManager();
	}

	@Override
	public void close() {
	}

	@Override
	public OrientGraph getGraph() {
		return graph;
	}

}
