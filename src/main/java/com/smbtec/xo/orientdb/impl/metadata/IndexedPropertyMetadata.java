package com.smbtec.xo.orientdb.impl.metadata;

import com.tinkerpop.blueprints.Element;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Rick-Rainer Ludwig
 *
 */
public class IndexedPropertyMetadata {

    private final String name;
    private final boolean unique;
    private final Class<? extends Element> type;
    private final Class<?> dataType;

    public IndexedPropertyMetadata(final String name, final boolean unique, final Class<?> dataType, final Class<? extends Element> type) {
        this.name = name;
        this.unique = unique;
        this.dataType = dataType;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public boolean isUnique() {
        return unique;
    }

    public Class<? extends Element> getType() {
        return type;
    }

    public Class<?> getDataType() {
        return dataType;
    }
}
