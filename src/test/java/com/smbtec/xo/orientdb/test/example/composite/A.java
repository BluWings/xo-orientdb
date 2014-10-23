package com.smbtec.xo.orientdb.test.example.composite;

import java.util.List;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Incoming;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Outgoing;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Indexed;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex
public interface A extends Named {

    @Indexed
    String getValue();

    void setValue(String value);

    @Incoming
    Parent getParent();

    @Outgoing
    List<Parent> getChildren();
}
