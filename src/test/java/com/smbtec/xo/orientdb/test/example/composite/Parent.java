package com.smbtec.xo.orientdb.test.example.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Incoming;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Outgoing;

@Edge
public interface Parent extends Named {

    @Outgoing
    A getParent();

    @Incoming
    A getChild();

}
