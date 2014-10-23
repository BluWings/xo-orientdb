package com.smbtec.xo.orientdb.test.type.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex("EXPLICIT_TYPE")
public interface ExplicitType {

    String getString();

    void setString(String string);

}
