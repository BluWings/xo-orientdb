package com.smbtec.xo.orientdb.test.generics;

import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;

import com.smbtec.xo.orientdb.test.AbstractOrientDbXOManagerTest;
import com.smbtec.xo.orientdb.test.generics.composite.BoundType;
import com.smbtec.xo.orientdb.test.generics.composite.GenericSuperType;

@RunWith(Parameterized.class)
public class GenericTypeTest extends AbstractOrientDbXOManagerTest {

	 public GenericTypeTest(XOUnit xoUnit) {
	        super(xoUnit);
	    }

	    @Parameterized.Parameters
	    public static Collection<Object[]> getXOUnits() throws URISyntaxException {
	        return xoUnits(GenericSuperType.class, BoundType.class);
	    }

	    @Test
	    public void composite() {
	        XOManager xoManager = getXoManager();
	        xoManager.currentTransaction().begin();
	        BoundType b = xoManager.create(BoundType.class);
	        b.setValue("value");
	        xoManager.currentTransaction().commit();
	    }

}
