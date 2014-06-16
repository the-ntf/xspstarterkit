package org.openntf.xsp.sdk.components;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface XspPersistent {
	public enum Strategy {
		REFLECTIVE, EXPLICIT
	};

	Strategy strategy() default Strategy.REFLECTIVE;

	String[] fields() default { "" };
}
