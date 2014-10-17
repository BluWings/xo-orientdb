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

import com.tinkerpop.blueprints.Element;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Rick-Rainer Ludwig
 *
 */
public class IndexedPropertyMetadata {

    private final String name;
    private final boolean unique;
    private final Class<? extends Element> type;
    private final Class<?> dataType;

    public IndexedPropertyMetadata(final String name, final boolean unique, final Class<?> dataType, final Class<? extends Element> type) {
        this.name = name;
        this.unique = unique;
        this.dataType = dataType;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public boolean isUnique() {
        return unique;
    }

    public Class<? extends Element> getType() {
        return type;
    }

    public Class<?> getDataType() {
        return dataType;
    }
}
