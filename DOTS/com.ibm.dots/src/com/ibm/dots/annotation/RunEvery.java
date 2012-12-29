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
import java.util.concurrent.TimeUnit;

/**
 * @author dtaieb
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RunEvery {

	/**
	 * @return: the frequency for which the tasklet method is being called
	 */
	int every();

	TimeUnit unit();

	/**
	 * @return The tasklet starting time Optional attribute to start the run at a particular time, value must be compliant with the
	 *         java.text.DateFormat SHORT pattern (e.g. 10:00 AM) This attribute takes a different meaning based on the value of the every
	 *         and unit attribute: 1. unit = day: the tasklet run every day at the same time 2. unit = minute or second. The tasklet run at
	 *         times synchronized with the startAt attribute. For example, if startAt=1PM and run is every 1 hour. then the tasklet will run
	 *         every hour on the hour
	 */
	String startAt() default "";

	/**
	 * @return The tasklet stop time Optional attribute to specify the time at which the run will stop. If not defined, run will execute all
	 *         day. Valid only if startAt has been defined.
	 */
	String stopAt() default "";

}
