package com.smbtec.xo.orientdb.impl;

import java.util.Map;

import com.buschmais.xo.spi.datastore.DatastorePropertyManager;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.smbtec.xo.orientdb.impl.metadata.PropertyMetadata;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.impls.orient.OrientElement;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class AbstractOrientDbPropertyManager<E extends Element> implements DatastorePropertyManager<E, PropertyMetadata> {

    @Override
    public void setProperty(E element, PrimitivePropertyMethodMetadata<PropertyMetadata> metadata, Object value) {
        element.setProperty(metadata.getDatastoreMetadata().getName(), value);
    }

    @Override
    public boolean hasProperty(E element, PrimitivePropertyMethodMetadata<PropertyMetadata> metadata) {
        return element.getProperty(metadata.getDatastoreMetadata().getName()) != null;
    }

    @Override
    public void removeProperty(E element, PrimitivePropertyMethodMetadata<PropertyMetadata> metadata) {
        element.removeProperty(metadata.getDatastoreMetadata().getName());
    }

    @Override
    public Object getProperty(E element, PrimitivePropertyMethodMetadata<PropertyMetadata> metadata) {
        return element.getProperty(metadata.getDatastoreMetadata().getName());
    }

    public void setProperties(E element, Map<String, Object> properties) {
        ((OrientElement) element).setProperties(properties);
    }

}
