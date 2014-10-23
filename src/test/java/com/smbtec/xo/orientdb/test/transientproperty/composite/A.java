package com.smbtec.xo.orientdb.test.transientproperty.composite;

import com.buschmais.xo.api.annotation.Transient;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Indexed;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex
public interface A {

    @Indexed
    String getValue();

    void setValue(String value);

    @Transient
    String getTransientValue();

    void setTransientValue(String transientValue);
}
