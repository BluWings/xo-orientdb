package com.smbtec.xo.orientdb.api;

import com.buschmais.xo.spi.datastore.DatastoreSession;
import com.smbtec.xo.orientdb.impl.metadata.EdgeMetadata;
import com.smbtec.xo.orientdb.impl.metadata.VertexMetadata;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

/**
 * 
 * @author Lars Martin - lars.martin@smb-tec.com
 * 
 */
public interface OrientDbDatastoreSession<G extends OrientGraph> extends
		DatastoreSession<Object, Vertex, VertexMetadata, String, Object, Edge, EdgeMetadata, String> {

	G getGraph();

}