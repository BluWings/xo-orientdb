package com.smbtec.xo.orientdb.test.id.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex
public interface A {

	A2B getA2B();

}