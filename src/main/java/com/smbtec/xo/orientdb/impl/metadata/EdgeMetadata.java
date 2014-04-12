package com.smbtec.xo.orientdb.impl.metadata;

import com.buschmais.xo.spi.datastore.DatastoreRelationMetadata;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Rick-Rainer Ludwig
 *
 */
public class EdgeMetadata implements DatastoreRelationMetadata<String> {

    private final String label;

    public EdgeMetadata(final String label) {
        this.label = label;
    }

    @Override
    public String getDiscriminator() {
        return label;
    }
}