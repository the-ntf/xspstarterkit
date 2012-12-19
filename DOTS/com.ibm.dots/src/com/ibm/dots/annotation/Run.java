/*
 * © Copyright IBM Corp. 2009,2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.dots.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author dtaieb
 * Annotation interface allowing registration of a method as a tasklet
 */

@Retention( RetentionPolicy.RUNTIME)
@Target( {ElementType.METHOD })
public @interface Run {

	/**
	 * @return: the id of the tasklet created via annotation. To insure uniqueness, the id used must be className.id
	 * for example, if annotated method is in MyTasklet class and id=manual, then caller must use: 
	 * tell dots run MyTasklet.manual 
	 */
	String id();

}
