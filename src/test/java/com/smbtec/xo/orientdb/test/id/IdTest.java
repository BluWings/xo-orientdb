package com.smbtec.xo.orientdb.test.id;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.xo.api.CompositeObject;
import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;

import com.smbtec.xo.orientdb.test.AbstractOrientDbXOManagerTest;
import com.smbtec.xo.orientdb.test.id.composite.A;
import com.smbtec.xo.orientdb.test.id.composite.A2B;
import com.smbtec.xo.orientdb.test.id.composite.B;

@RunWith(Parameterized.class)
public class IdTest extends AbstractOrientDbXOManagerTest {

	public IdTest(XOUnit xoUnit) {
		super(xoUnit);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> getXOUnits() throws URISyntaxException {
		return xoUnits(A.class, B.class, A2B.class);
	}

	@Test
	public void id() {
		XOManager xoManager = getXoManager();
		xoManager.currentTransaction().begin();
		A a = xoManager.create(A.class);
		B b = xoManager.create(B.class);
		A2B a2b = xoManager.create(a, A2B.class, b);
		Object aId = xoManager.getId(a);
		assertThat(aId, notNullValue());
		assertThat(((CompositeObject) a).getId(), equalTo(aId));
		Object bId = xoManager.getId(b);
		assertThat(bId, notNullValue());
		assertThat(((CompositeObject) b).getId(), equalTo(bId));
		Object a2bId = xoManager.getId(a2b);
		assertThat(a2bId, notNullValue());
		assertThat(((CompositeObject) a2b).getId(), equalTo(a2bId));
	}

}
