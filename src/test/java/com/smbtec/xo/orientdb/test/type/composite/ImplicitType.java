package com.smbtec.xo.orientdb.test.type.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex
public interface ImplicitType {

    String getString();

    void setString(String string);

}
