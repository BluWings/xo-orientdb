package com.smbtec.xo.orientdb.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.xo.api.ConcurrencyMode;
import com.buschmais.xo.api.Transaction;
import com.buschmais.xo.api.ValidationMode;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.buschmais.xo.test.AbstractXOManagerTest;
import com.smbtec.xo.orientdb.api.OrientDbDatastoreSession;
import com.smbtec.xo.orientdb.api.OrientDbXOProvider;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public abstract class AbstractOrientDbXOManagerTest extends AbstractXOManagerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractOrientDbXOManagerTest.class);

    public static final Database PLOCAL = new OrientDB("plocal:target/orientdb");
    public static final Database MEMORY = new OrientDB("memory:orientdb");
    public static final Database REMOTE = new OrientDB("remote:localhost/orientdb");

    protected AbstractOrientDbXOManagerTest(final XOUnit xoUnit) {
        super(xoUnit);
    }

    protected static Collection<Object[]> xoUnits(final Class<?>... types) {
        return xoUnits(Arrays.asList(MEMORY, PLOCAL), Arrays.asList(types), Collections.<Class<?>> emptyList(),
                ValidationMode.AUTO, ConcurrencyMode.SINGLETHREADED, Transaction.TransactionAttribute.NONE);
    }

    protected static Collection<Object[]> xoUnits(final List<? extends Class<?>> types,
            final List<? extends Class<?>> instanceListenerTypes, final ValidationMode validationMode,
            final ConcurrencyMode concurrencyMode, final Transaction.TransactionAttribute transactionAttribute)
            throws URISyntaxException {
        return xoUnits(Arrays.asList(MEMORY, PLOCAL), types, instanceListenerTypes, validationMode, concurrencyMode,
                transactionAttribute);
    }

    @Override
    protected void dropDatabase() {
        getXoManager().currentTransaction().begin();
        final OrientDbDatastoreSession orientDbSession = getXoManager().getDatastoreSession(
                OrientDbDatastoreSession.class);
        final OrientGraph graph = orientDbSession.getGraph();
        final Iterable<Edge> edges = graph.getEdges();
        for (final Edge edge : edges) {
            edge.remove();
        }
        final Iterable<Vertex> vertices = graph.getVertices();
        for (final Vertex vertex : vertices) {
            vertex.remove();
        }
        getXoManager().currentTransaction().commit();
    }

    public static final class OrientDB implements Database {

        private URI uri;

        public OrientDB(final String uri) {
            try {
                this.uri = new URI(uri);
            } catch (final URISyntaxException e) {
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
