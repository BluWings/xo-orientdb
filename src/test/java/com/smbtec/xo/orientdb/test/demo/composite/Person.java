package com.smbtec.xo.orientdb.test.demo.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Indexed;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

/**
 * 
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Dirk Mahler
 * 
 */
@Vertex("Person")
public interface Person {

	@Indexed
	String getName();

	void setName(String name);
}
