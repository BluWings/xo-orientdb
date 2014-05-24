package com.smbtec.xo.orientdb.impl;

import java.util.Map;

import com.buschmais.xo.api.ResultIterator;
import com.buschmais.xo.spi.datastore.DatastoreQuery;
import com.smbtec.xo.orientdb.api.OrientDbDatastoreSession;
import com.smbtec.xo.orientdb.api.annotation.OSQL;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class OSQLQuery implements DatastoreQuery<OSQL> {

    public OSQLQuery(OrientDbDatastoreSession<?> session) {
    }

    @Override
    public ResultIterator<Map<String, Object>> execute(String query, Map<String, Object> parameters) {
        return null;
    }

    @Override
    public ResultIterator<Map<String, Object>> execute(OSQL query, Map<String, Object> parameters) {
        return null;
    }

}
