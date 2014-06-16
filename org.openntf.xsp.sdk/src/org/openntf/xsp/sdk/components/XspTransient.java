package org.openntf.xsp.sdk.components;

import java.lang.annotation.*;

/*
 * Indicates that the field should be excluded from XSP Persistence processing
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface XspTransient {

}
