package com.smbtec.xo.orientdb.test.demo.composite;

import java.util.List;

import com.buschmais.xo.api.annotation.ResultOf;
import com.buschmais.xo.api.annotation.ResultOf.Parameter;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

/**
 * 
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Dirk Mahler
 * 
 */
@Vertex("Group")
public interface Group {

	List<Person> getMembers();

	@ResultOf
	MemberByName getMemberByName(@Parameter("name") String name);

	public interface MemberByName {
		Person getMember();
	}

}
