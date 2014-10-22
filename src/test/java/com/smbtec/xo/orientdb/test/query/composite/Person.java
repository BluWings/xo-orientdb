package com.smbtec.xo.orientdb.test.query.composite;

import java.util.Collection;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex
public interface Person extends Named {

    int getAge();

    void setAge(int age);

    Collection<Person> getFriends();

}
