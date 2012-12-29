/**
 * 
 */
package com.ibm.dots.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author nfreeman
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface ThreadStrategy {
	public static int GENERIC = 0;
	public static int NSF = 1;
	public static int NAMED = 2;
	public static int TASK_EXCLUSIVE = 3;

	public int strategy() default GENERIC;

	public int name();

}
