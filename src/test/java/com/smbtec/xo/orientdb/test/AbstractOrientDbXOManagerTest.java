package com.smbtec.xo.orientdb.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import com.buschmais.xo.api.ConcurrencyMode;
import com.buschmais.xo.api.Transaction;
import com.buschmais.xo.api.ValidationMode;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.buschmais.xo.spi.interceptor.XOInterceptor;
import com.buschmais.xo.test.AbstractXOManagerTest;
import com.smbtec.xo.orientdb.api.OrientDbDatastoreSession;
import com.smbtec.xo.orientdb.api.OrientDbXOProvider;
import com.smbtec.xo.tinkerpop.blueprints.api.TinkerPopXOProvider;
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

    protected static Collection<Object[]> xoUnits(final Class<?>... types) {
        return xoUnits(Arrays.asList(MEMORY), Arrays.asList(types), Collections.<Class<?>> emptyList(), ValidationMode.AUTO,
                ConcurrencyMode.SINGLETHREADED, Transaction.TransactionAttribute.NONE);
    }

    protected static Collection<Object[]> xoUnits(final List<? extends Class<?>> types, final List<? extends Class<?>> instanceListenerTypes,
            final ValidationMode validationMode, final ConcurrencyMode concurrencyMode, final Transaction.TransactionAttribute transactionAttribute)
            throws URISyntaxException {
        return xoUnits(Arrays.asList(MEMORY), types, instanceListenerTypes, validationMode, concurrencyMode, transactionAttribute);
    }

    @Override
    protected void dropDatabase() {
        final OrientDbDatastoreSession orientDbSession = getXoManager().getDatastoreSession(OrientDbDatastoreSession.class);
        final Iterable<Edge> edges = orientDbSession.getGraph().getEdges();
        for (final Edge edge : edges) {
            edge.remove();
        }
        final Iterable<Vertex> vertices = orientDbSession.getGraph().getVertices();
        for (final Vertex vertex : vertices) {
            vertex.remove();
        }
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
