package com.smbtec.xo.orientdb.impl.metadata;

import com.buschmais.xo.spi.datastore.DatastoreEntityMetadata;
import com.buschmais.xo.spi.metadata.method.IndexedPropertyMethodMetadata;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Rick-Rainer Ludwig
 *
 */
public class VertexMetadata implements DatastoreEntityMetadata<String> {

    private final String discriminator;
    private final IndexedPropertyMethodMetadata<?> indexedProperty;

    public VertexMetadata(final String discriminator, final IndexedPropertyMethodMetadata<?> indexedProperty) {
        super();
        this.discriminator = discriminator;
        this.indexedProperty = indexedProperty;
    }

    @Override
    public String getDiscriminator() {
        return discriminator;
    }

    public IndexedPropertyMethodMetadata<?> getIndexedProperty() {
        return indexedProperty;
    }

}
