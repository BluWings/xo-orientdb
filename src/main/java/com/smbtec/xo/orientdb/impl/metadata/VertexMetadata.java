/*
 * eXtended Objects - OrientDb Binding
 *
 * Copyright (C) 2014 SMB GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.smbtec.xo.orientdb.impl.metadata;

import com.buschmais.xo.spi.datastore.DatastoreEntityMetadata;
import com.buschmais.xo.spi.metadata.method.IndexedPropertyMethodMetadata;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Rick-Rainer Ludwig
 *
 */
public class VertexMetadata implements DatastoreEntityMetadata<String> {

    private final String discriminator;
    private final IndexedPropertyMethodMetadata<?> indexedProperty;

    public VertexMetadata(final String discriminator, final IndexedPropertyMethodMetadata<?> indexedProperty) {
        super();
        this.discriminator = discriminator;
        this.indexedProperty = indexedProperty;
    }

    @Override
    public String getDiscriminator() {
        return discriminator;
    }

    public IndexedPropertyMethodMetadata<?> getIndexedProperty() {
        return indexedProperty;
    }

}
