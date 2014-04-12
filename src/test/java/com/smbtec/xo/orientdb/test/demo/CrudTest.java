package com.smbtec.xo.orientdb.test.demo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.smbtec.xo.orientdb.test.AbstractOrientDbXOManagerTest;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Indexed;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

/**
 * 
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Dirk Mahler
 * 
 */
@RunWith(Parameterized.class)
public class CrudTest extends AbstractOrientDbXOManagerTest {

	public CrudTest(final XOUnit xoUnit) {
		super(xoUnit);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> getXOUnits() throws URISyntaxException {
		return xoUnits(A.class);
	}

	@Test
	public void create() {
		final XOManager xoManager = getXoManager();
		xoManager.currentTransaction().begin();
		A a = xoManager.create(A.class);
		a.setName("Foo");
		xoManager.currentTransaction().commit();
		xoManager.currentTransaction().begin();
		a = xoManager.find(A.class, "Foo").getSingleResult();
		assertThat(a.getName(), equalTo("Foo"));
		a.setName("Bar");
		xoManager.currentTransaction().commit();
		xoManager.currentTransaction().begin();
		xoManager.delete(a);
		xoManager.currentTransaction().commit();
		xoManager.currentTransaction().begin();
		try {
			xoManager.find(A.class, "Bar").getSingleResult();
			Assert.fail("An exception is expected.");
		} catch (final XOException e) {
		}
		xoManager.currentTransaction().commit();
	}

	@Vertex("A")
	public interface A {

		@Indexed
		String getName();

		void setName(String name);

	}

}
