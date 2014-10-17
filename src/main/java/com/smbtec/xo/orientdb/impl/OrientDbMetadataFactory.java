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

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreMetadataFactory;
import com.buschmais.xo.spi.metadata.method.IndexedPropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.type.TypeMetadata;
import com.buschmais.xo.spi.reflection.AnnotatedElement;
import com.buschmais.xo.spi.reflection.AnnotatedMethod;
import com.buschmais.xo.spi.reflection.AnnotatedType;
import com.buschmais.xo.spi.reflection.PropertyMethod;

import com.smbtec.xo.orientdb.impl.metadata.CollectionPropertyMetadata;
import com.smbtec.xo.orientdb.impl.metadata.EdgeMetadata;
import com.smbtec.xo.orientdb.impl.metadata.IndexedPropertyMetadata;
import com.smbtec.xo.orientdb.impl.metadata.PropertyMetadata;
import com.smbtec.xo.orientdb.impl.metadata.ReferencePropertyMetadata;
import com.smbtec.xo.orientdb.impl.metadata.VertexMetadata;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Indexed;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Property;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Incoming;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Outgoing;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Element;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Rick-Rainer Ludwig
 *
 */
public class OrientDbMetadataFactory implements DatastoreMetadataFactory<VertexMetadata, String, EdgeMetadata, String> {

    @Override
    public VertexMetadata createEntityMetadata(final AnnotatedType annotatedType,
            final Map<Class<?>, TypeMetadata> metadataByType) {
        final Vertex annotation = annotatedType.getAnnotation(Vertex.class);
        String value = null;
        IndexedPropertyMethodMetadata<?> indexedProperty = null;
        if (annotation != null) {
            value = annotation.value();
            if ((value == null) || (value.isEmpty())) {
                value = annotatedType.getName();
            }
            final Class<?> usingIndexOf = annotation.usingIndexedPropertyOf();
            if (!Object.class.equals(usingIndexOf)) {
                final TypeMetadata typeMetadata = metadataByType.get(usingIndexOf);
                indexedProperty = typeMetadata.getIndexedProperty();
            }
        }
        return new VertexMetadata(value, indexedProperty);
    }

    @Override
    public <ImplementedByMetadata> ImplementedByMetadata createImplementedByMetadata(
            final AnnotatedMethod annotatedMethod) {
        return null;
    }

    @Override
    public CollectionPropertyMetadata createCollectionPropertyMetadata(final PropertyMethod propertyMethod) {
        final String name = determinePropertyName(propertyMethod);
        final Direction direction = determineEdgeDirection(propertyMethod);
        return new CollectionPropertyMetadata(name, direction);
    }

    @Override
    public ReferencePropertyMetadata createReferencePropertyMetadata(final PropertyMethod propertyMethod) {
        final String name = determinePropertyName(propertyMethod);
        final Direction direction = determineEdgeDirection(propertyMethod);
        return new ReferencePropertyMetadata(name, direction);
    }

    @Override
    public PropertyMetadata createPropertyMetadata(final PropertyMethod propertyMethod) {
        final Property property = propertyMethod.getAnnotationOfProperty(Property.class);
        final String name = property != null ? property.value() : propertyMethod.getName();
        return new PropertyMetadata(name);
    }

    @Override
    public IndexedPropertyMetadata createIndexedPropertyMetadata(final PropertyMethod propertyMethod) {
        final String name = propertyMethod.getName();
        final Class<?> declaringClass = propertyMethod.getAnnotatedElement().getDeclaringClass();
        Class<? extends Element> type = null;
        if (declaringClass.getAnnotation(Vertex.class) != null) {
            type = com.tinkerpop.blueprints.Vertex.class;
        } else if (declaringClass.getAnnotation(Edge.class) != null) {
            type = com.tinkerpop.blueprints.Edge.class;
        } else {
            throw new XOException("Property '" + name
                    + "' was found with index annotation, but the declaring type is neither a vertex nor an edge.");
        }
        final Indexed indexedAnnotation = propertyMethod.getAnnotation(Indexed.class);
        final boolean unique = indexedAnnotation.unique();
        final Class<?> dataType = propertyMethod.getType();
        return new IndexedPropertyMetadata(name, unique, dataType, type);
    }

    @Override
    public EdgeMetadata createRelationMetadata(final AnnotatedElement<?> annotatedElement,
            final Map<Class<?>, TypeMetadata> metadataByType) {
        Edge relationAnnotation;
        if (annotatedElement instanceof PropertyMethod) {
            relationAnnotation = ((PropertyMethod) annotatedElement).getAnnotationOfProperty(Edge.class);
        } else {
            relationAnnotation = annotatedElement.getAnnotation(Edge.class);
        }
        String name = null;
        if (relationAnnotation != null) {
            final String value = relationAnnotation.value();
            if (!Edge.DEFAULT_VALUE.equals(value)) {
                name = value;
            }
        }
        if (name == null) {
            name = StringUtils.uncapitalize(annotatedElement.getName());
        }
        return new EdgeMetadata(name);
    }

    private String determinePropertyName(final PropertyMethod propertyMethod) {
        final Vertex property = propertyMethod.getAnnotationOfProperty(Vertex.class);
        return property != null ? property.value() : propertyMethod.getName();
    }

    private Direction determineEdgeDirection(final PropertyMethod propertyMethod) {
        final Outgoing outgoingAnnotation = propertyMethod.getAnnotation(Outgoing.class);
        final Incoming incomingAnnotation = propertyMethod.getAnnotation(Incoming.class);
        if ((outgoingAnnotation != null) && (incomingAnnotation != null)) {
            return Direction.BOTH;
        } else if (incomingAnnotation != null) {
            return Direction.IN;
        } else {
            return Direction.OUT;
        }
    }
}
