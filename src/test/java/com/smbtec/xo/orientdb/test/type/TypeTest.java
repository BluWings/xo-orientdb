package com.smbtec.xo.orientdb.test.type;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.smbtec.xo.orientdb.test.AbstractOrientDbXOManagerTest;
import com.smbtec.xo.orientdb.test.type.composite.ExplicitType;
import com.smbtec.xo.orientdb.test.type.composite.ImplicitType;

@RunWith(Parameterized.class)
public class TypeTest extends AbstractOrientDbXOManagerTest {

    public TypeTest(XOUnit xoUnit) {
        super(xoUnit);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getXOUnits() throws URISyntaxException {
        return xoUnits(ImplicitType.class, ExplicitType.class);
    }

    @Test
    public void implicitLabel() {
        XOManager xoManager = getXoManager();
        xoManager.currentTransaction().begin();
        ImplicitType implicitType = xoManager.create(ImplicitType.class);
        assertThat(executeQuery("SELECT FROM ImplicitType").getColumn("node"), hasItem(implicitType));
        xoManager.currentTransaction().commit();
    }

    @Test
    public void explicitLabel() {
        XOManager xoManager = getXoManager();
        xoManager.currentTransaction().begin();
        ExplicitType explicitType = xoManager.create(ExplicitType.class);
        assertThat(executeQuery("SELECT FROM EXPLICIT_TYPE").getColumn("node"), hasItem(explicitType));
        xoManager.currentTransaction().commit();
    }
}
