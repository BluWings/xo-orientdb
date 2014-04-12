package com.smbtec.xo.orientdb.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.buschmais.xo.spi.annotation.QueryDefinition;

@QueryDefinition
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OSQL {

	/**
	 * @return Returns the OrientDB SQL expression as {@link String}.
	 */
	String value();

}
