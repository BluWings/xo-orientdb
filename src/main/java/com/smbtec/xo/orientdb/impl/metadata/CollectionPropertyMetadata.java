package com.smbtec.xo.orientdb.impl.metadata;

import com.tinkerpop.blueprints.Direction;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Rick-Rainer Ludwig
 *
 */
public class CollectionPropertyMetadata {

    private final String name;
    private final Direction direction;

    public CollectionPropertyMetadata(final String name, final Direction direction) {
        this.name = name;
        this.direction = direction;
    }

    public String getName() {
        return name;
    }

    public Direction getDirection() {
        return direction;
    }

}
