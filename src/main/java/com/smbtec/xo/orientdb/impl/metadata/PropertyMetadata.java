package com.smbtec.xo.orientdb.impl.metadata;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Rick-Rainer Ludwig
 *
 */
public class PropertyMetadata {

    private final String name;

    public PropertyMetadata(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
