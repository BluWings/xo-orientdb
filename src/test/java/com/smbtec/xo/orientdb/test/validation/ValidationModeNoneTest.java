package com.smbtec.xo.orientdb.test.validation;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.xo.api.ConcurrencyMode;
import com.buschmais.xo.api.Transaction;
import com.buschmais.xo.api.ValidationMode;
import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.smbtec.xo.orientdb.test.AbstractOrientDbXOManagerTest;
import com.smbtec.xo.orientdb.test.validation.composite.A;
import com.smbtec.xo.orientdb.test.validation.composite.B;

@RunWith(Parameterized.class)
@Ignore
public class ValidationModeNoneTest extends AbstractOrientDbXOManagerTest {

    public ValidationModeNoneTest(final XOUnit xoUnit) {
        super(xoUnit);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getXOUnits() throws URISyntaxException {
        return xoUnits(asList(A.class, B.class), Collections.<Class<?>> emptyList(), ValidationMode.NONE, ConcurrencyMode.SINGLETHREADED,
                Transaction.TransactionAttribute.MANDATORY);
    }

    @Test
    public void validationOnCommitAfterInsert() {
        final XOManager xoManager = getXoManager();
        xoManager.currentTransaction().begin();
        final A a = xoManager.create(A.class);
        xoManager.currentTransaction().commit();
        xoManager.currentTransaction().begin();
        assertThat(a.getName(), nullValue());
        xoManager.currentTransaction().commit();
    }

}
