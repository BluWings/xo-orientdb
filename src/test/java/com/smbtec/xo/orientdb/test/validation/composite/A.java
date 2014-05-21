package com.smbtec.xo.orientdb.test.validation.composite;

import javax.validation.constraints.NotNull;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Indexed;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex("A")
public interface A {

    @NotNull
    @Indexed
    String getName();

    void setName(String name);

    @NotNull
    B getB();

    void setB(B b);
}
