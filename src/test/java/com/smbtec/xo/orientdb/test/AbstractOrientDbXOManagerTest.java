package com.smbtec.xo.orientdb.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.buschmais.xo.api.ConcurrencyMode;
import com.buschmais.xo.api.Transaction;
import com.buschmais.xo.api.ValidationMode;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.buschmais.xo.test.AbstractXOManagerTest;
import com.smbtec.xo.orientdb.api.OrientDbDatastoreSession;
import com.smbtec.xo.orientdb.api.OrientDbXOProvider;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

/**
 * 
 * @author Lars Martin - lars.martin@smb-tec.com
 * 
 */
public abstract class AbstractOrientDbXOManagerTest extends AbstractXOManagerTest {

	public static final Database PLOCAL = new OrientDB("plocal:target/orientdb");
	public static final Database MEMORY = new OrientDB("memory:orientdb");
	public static final Database REMOTE = new OrientDB("remote:localhost/orientdb");

	protected AbstractOrientDbXOManagerTest(final XOUnit xoUnit) {
		super(xoUnit);
	}

	protected static Collection<Object[]> xoUnits(Class<?>... types) {
		return xoUnits(Arrays.asList(MEMORY, PLOCAL), Arrays.asList(types), Collections.<Class<?>> emptyList(),
				ValidationMode.AUTO, ConcurrencyMode.SINGLETHREADED, Transaction.TransactionAttribute.NONE);
	}

	@Override
	protected void dropDatabase() {
		OrientDbDatastoreSession orientDbSession = getXoManager().getDatastoreSession(OrientDbDatastoreSession.class);
		Iterable<Edge> edges = orientDbSession.getGraph().getEdges();
		for (Edge edge : edges) {
			edge.remove();
		}
		Iterable<Vertex> vertices = orientDbSession.getGraph().getVertices();
		for (Vertex vertex : vertices) {
			vertex.remove();
		}
	}

	public static final class OrientDB implements Database {

		private URI uri;

		public OrientDB(String uri) {
			try {
				this.uri = new URI(uri);
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public URI getUri() {
			return uri;
		}

		@Override
		public Class<?> getProvider() {
			return OrientDbXOProvider.class;
		}

	}

}
