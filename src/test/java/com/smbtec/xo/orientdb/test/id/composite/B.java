package com.smbtec.xo.orientdb.test.id.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex
public interface B {

    A2B getA2B();

}