package com.smbtec.xo.orientdb.test.bootstrap;

import org.junit.Test;

import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.XOManagerFactory;
import com.buschmais.xo.api.bootstrap.XO;
import com.smbtec.xo.orientdb.test.bootstrap.composite.A;

/**
 * 
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Dirk Mahler
 * 
 */
public class EmbeddedOrientDbBootstrapTest {

	@Test
	public void bootstrap() {
		XOManagerFactory xoManagerFactory = XO.createXOManagerFactory("OrientDbEmbedded");
		XOManager xoManager = xoManagerFactory.createXOManager();
		xoManager.currentTransaction().begin();
		A a = xoManager.create(A.class);
		a.setName("Test");
		xoManager.currentTransaction().commit();
		xoManager.close();
		xoManagerFactory.close();
	}

}
