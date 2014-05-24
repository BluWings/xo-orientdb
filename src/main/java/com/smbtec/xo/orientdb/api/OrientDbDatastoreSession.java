package com.smbtec.xo.orientdb.api;

import com.buschmais.xo.spi.datastore.DatastoreSession;
import com.smbtec.xo.orientdb.impl.metadata.EdgeMetadata;
import com.smbtec.xo.orientdb.impl.metadata.PropertyMetadata;
import com.smbtec.xo.orientdb.impl.metadata.VertexMetadata;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public interface OrientDbDatastoreSession<G extends OrientGraph> extends
        DatastoreSession<Object, Vertex, VertexMetadata, String, Object, Edge, EdgeMetadata, String, PropertyMetadata> {

    G getGraph();

}
