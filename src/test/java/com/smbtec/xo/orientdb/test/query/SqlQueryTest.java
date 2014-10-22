package com.smbtec.xo.orientdb.test.query;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Collection;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.xo.api.CompositeObject;
import com.buschmais.xo.api.Example;
import com.buschmais.xo.api.Query.Result;
import com.buschmais.xo.api.Query.Result.CompositeRowObject;
import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.smbtec.xo.orientdb.test.AbstractOrientDbXOManagerTest;
import com.smbtec.xo.orientdb.test.query.composite.Person;

@RunWith(Parameterized.class)
public class SqlQueryTest extends AbstractOrientDbXOManagerTest {

    private Person john;
    private Person mary;
    private Person jDow;

    public SqlQueryTest(XOUnit xoUnit) {
        super(xoUnit);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getXOUnits() throws URISyntaxException {
        return xoUnits(Person.class);
    }

    @Before
    public void populate() {
        XOManager xoManager = getXoManager();
        xoManager.currentTransaction().begin();
        john = xoManager.create(new Example<Person>() {

            @Override
            public void prepare(Person example) {
                example.setFirstname("John");
                example.setLastname("Doe");
                example.setAge(25);
            }

        }, Person.class);
        mary = xoManager.create(new Example<Person>() {

            @Override
            public void prepare(Person example) {
                example.setFirstname("Mary");
                example.setLastname("Doe");
                example.setAge(20);
            }

        }, Person.class);

        jDow = xoManager.create(new Example<Person>() {

            @Override
            public void prepare(Person example) {
                example.setFirstname("Jon");
                example.setLastname("Dow");
                example.setAge(31);
            }

        }, Person.class);
        john.getFriends().add(mary);
        john.getFriends().add(jDow);
        xoManager.currentTransaction().commit();
    }

    @Test
    public void selectVertexById() {
        XOManager xoManager = getXoManager();
        xoManager.currentTransaction().begin();
        Object id = ((CompositeObject) john).getId();
        Person person = xoManager.createQuery("SELECT FROM [" + id + "]", Person.class).execute().getSingleResult();
        assertThat(person, equalTo(john));
        xoManager.currentTransaction().commit();
    }

    @Test
    public void countVerticesWithGivenField() {
        XOManager xoManager = getXoManager();
        xoManager.currentTransaction().begin();
        Long count = xoManager.createQuery("SELECT count(*) FROM V WHERE firstname = 'John'", Long.class).execute()
                .getSingleResult();
        assertThat(count, equalTo(1L));
        xoManager.currentTransaction().commit();

    }

    @Test
    public void selectOutEdgesFromVertex() {
        XOManager xoManager = getXoManager();
        xoManager.currentTransaction().begin();
        Result<CompositeRowObject> result = xoManager.createQuery("SELECT outE() FROM V WHERE firstname = 'John'")
                .execute();
        assertThat(result.hasResult(), is(true));
        xoManager.currentTransaction().commit();
    }

    @Test
    public void countOutEdgesFromVertexWithGivenField() {
        XOManager xoManager = getXoManager();
        xoManager.currentTransaction().begin();
        Object id = ((CompositeObject) john).getId();
        Integer count = xoManager.createQuery("SELECT out().size() FROM [" + id + "]", Integer.class).execute()
                .getSingleResult();
        assertThat(count, equalTo(2));
        xoManager.currentTransaction().commit();
    }

    @Test
    public void selectAllVertices() {
        XOManager xoManager = getXoManager();
        xoManager.currentTransaction().begin();
        Result<Person> result = xoManager.createQuery("SELECT FROM V", Person.class).execute();
        assertThat(result, IsIterableContainingInAnyOrder.<Person> containsInAnyOrder(john, mary, jDow));
        xoManager.currentTransaction().commit();
    }

    @Test
    public void selectTypedVertex() {
        XOManager xoManager = getXoManager();
        xoManager.currentTransaction().begin();
        Person person = xoManager.createQuery("SELECT FROM Person WHERE firstname = 'John'", Person.class).execute()
                .getSingleResult();
        assertThat(person, is(equalTo(john)));
        xoManager.currentTransaction().commit();
    }
}
