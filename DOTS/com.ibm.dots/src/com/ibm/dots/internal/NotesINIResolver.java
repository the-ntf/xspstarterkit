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

package com.ibm.dots.internal;

import com.ibm.dots.task.IArgumentResolver;
import com.ibm.dots.utils.StringUtils;

import lotus.domino.NotesException;
import lotus.domino.Session;

public class NotesINIResolver implements IArgumentResolver {

	public NotesINIResolver() {

	}

	public String resolve(Session session, String var, String defValue) throws NotesException {
		String resolvedVar = session.getEnvironmentString(var, true);

		if (StringUtils.isEmpty(resolvedVar))
			resolvedVar = defValue;

		return resolvedVar;
	}
}

