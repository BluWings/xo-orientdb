package com.smbtec.xo.orientdb.impl;

import java.net.URI;
import java.util.Collection;

import com.buschmais.xo.spi.datastore.Datastore;
import com.buschmais.xo.spi.datastore.DatastoreMetadataFactory;
import com.buschmais.xo.spi.metadata.type.TypeMetadata;
import com.smbtec.xo.orientdb.api.OrientDbDatastoreSession;
import com.smbtec.xo.orientdb.impl.metadata.EdgeMetadata;
import com.smbtec.xo.orientdb.impl.metadata.VertexMetadata;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

/**
 * 
 * @author Lars Martin - lars.martin@smb-tec.com
 * 
 */
public class OrientDbDatastore implements
		Datastore<OrientDbDatastoreSession<OrientGraph>, VertexMetadata, String, EdgeMetadata, String> {

	private OrientGraph graph;

	public OrientDbDatastore(URI uri) {
		this.graph = new OrientGraph(uri.toString());
	}

	@Override
	public void init(Collection<TypeMetadata> registeredMetadata) {
	}

	@Override
	public DatastoreMetadataFactory<VertexMetadata, String, EdgeMetadata, String> getMetadataFactory() {
		return new OrientDbMetadataFactory();
	}

	@Override
	public OrientDbDatastoreSession<OrientGraph> createSession() {
		return new OrientDbDatastoreSessionImpl(graph);
	}

	@Override
	public void close() {
		graph.shutdown();
		graph = null;
	}

}
