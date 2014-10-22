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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.xo.api.ResultIterator;
import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreEntityManager;
import com.buschmais.xo.spi.datastore.TypeMetadataSet;
import com.buschmais.xo.spi.metadata.method.IndexedPropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.type.EntityTypeMetadata;
import com.smbtec.xo.orientdb.impl.metadata.PropertyMetadata;
import com.smbtec.xo.orientdb.impl.metadata.VertexMetadata;
import com.tinkerpop.blueprints.GraphQuery;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class OrientDbVertexManager extends AbstractOrientDbPropertyManager<OrientVertex> implements
        DatastoreEntityManager<Object, OrientVertex, VertexMetadata, String, PropertyMetadata> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrientDbVertexManager.class);

    /**
     * This constant contains the prefix for discriminator properties.
     */
    public static final String XO_DISCRIMINATORS_PROPERTY = "_xo_discriminator_";

    private final OrientGraph graph;

    public OrientDbVertexManager(OrientGraph graph) {
        this.graph = graph;
    }

    @Override
    public boolean isEntity(Object o) {
        return OrientVertex.class.isAssignableFrom(o.getClass());
    }

    @Override
    public Set<String> getEntityDiscriminators(OrientVertex entity) {
        final Set<String> discriminators = new HashSet<>();
        for (final String key : entity.getPropertyKeys()) {
            if (key.startsWith(XO_DISCRIMINATORS_PROPERTY)) {
                final String discriminator = entity.getProperty(key);
                discriminators.add(discriminator);
            }
        }
        if (discriminators.isEmpty()) {
            throw new XOException(
                    "A vertex was found without discriminators. Does another framework alter the database?");
        }
        return discriminators;
    }

    @Override
    public Object getEntityId(OrientVertex entity) {
        return entity.getId();
    }

    @Override
    public OrientVertex createEntity(TypeMetadataSet<EntityTypeMetadata<VertexMetadata>> types,
            Set<String> discriminators, Map<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> exampleEntity) {
        final OrientVertex vertex = graph.addVertex(getDiscriminator(discriminators), (String) null);
        setProperties(vertex, getProperties(discriminators, exampleEntity));
        return vertex;
    }

    @Override
    public void deleteEntity(OrientVertex entity) {
        entity.remove();
    }

    @Override
    public ResultIterator<OrientVertex> findEntity(EntityTypeMetadata<VertexMetadata> entityTypeMetadata,
            String discriminator, Map<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> values) {
        if (values.size() > 1) {
            throw new XOException("Only one property value is supported for find operation");
        }
        Map.Entry<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> entry = values.entrySet().iterator()
                .next();
        PrimitivePropertyMethodMetadata<PropertyMetadata> propertyMethodMetadata = entry.getKey();
        if (propertyMethodMetadata == null) {
            IndexedPropertyMethodMetadata<?> indexedProperty = entityTypeMetadata.getDatastoreMetadata()
                    .getIndexedProperty();
            if (indexedProperty == null) {
                throw new XOException("Type " + entityTypeMetadata.getAnnotatedType().getAnnotatedElement().getName()
                        + " has no indexed property.");
            }
            propertyMethodMetadata = indexedProperty.getPropertyMethodMetadata();
        }
        PropertyMetadata propertyMetadata = propertyMethodMetadata.getDatastoreMetadata();
        Object value = entry.getValue();

        GraphQuery query = graph.query();
        query = query.has(XO_DISCRIMINATORS_PROPERTY + discriminator);

        query = query.has(propertyMethodMetadata.getDatastoreMetadata().getName(), value);
        final Iterator<Vertex> iterator = query.vertices().iterator();

        return new ResultIterator<OrientVertex>() {

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public OrientVertex next() {
                return (OrientVertex) iterator.next();
            }

            @Override
            public void remove() {
                throw new XOException("Remove operation is not supported for find results.");
            }

            @Override
            public void close() {
                // intentionally left blank
            }
        };
    }

    @Override
    public void migrateEntity(OrientVertex entity, TypeMetadataSet<EntityTypeMetadata<VertexMetadata>> types,
            Set<String> discriminators, TypeMetadataSet<EntityTypeMetadata<VertexMetadata>> targetTypes,
            Set<String> targetDiscriminators) {
    }

    @Override
    public void flushEntity(OrientVertex entity) {
        // intentionally left blank
    }

    private Map<String, Object> getProperties(Set<String> discriminators,
            Map<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> exampleEntity) {
        Map<String, Object> properties = new HashMap<String, Object>();
        for (final String discriminator : discriminators) {
            properties.put(XO_DISCRIMINATORS_PROPERTY + discriminator, discriminator);
        }
        for (Map.Entry<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> entry : exampleEntity.entrySet()) {
            properties.put(entry.getKey().getDatastoreMetadata().getName(), entry.getValue());
        }
        return properties;

    }

    private String getDiscriminator(Set<String> discriminators) {
        if (discriminators.isEmpty()) {
            return null;
        } else {
            return discriminators.iterator().next();
        }
    }

}
