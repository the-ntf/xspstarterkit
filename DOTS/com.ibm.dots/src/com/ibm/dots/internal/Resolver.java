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

import java.lang.reflect.Method;
import java.util.HashMap;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.Session;
import lotus.domino.NotesThread;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

import com.ibm.dots.Activator;
import com.ibm.dots.task.IArgumentResolver;
import com.ibm.dots.utils.NotesUtils;

public class Resolver extends HashMap<String, String> {
	private static final long								serialVersionUID	= 1L;
	private static final HashMap<String, IArgumentResolver>	argResolvers		= new HashMap<String, IArgumentResolver>();

	Session	_session = null;

	static {
		initArgResolver();
	}

	public Resolver() {

	}

	private static void initArgResolver() {
		IExtensionPoint extPt = Platform.getExtensionRegistry().getExtensionPoint(Activator.PLUGIN_ID + ".argResolver"); // $NON-NLS-1$

		IExtension[] extensions = extPt.getExtensions();

		for (IExtension extension : extensions) {
			IConfigurationElement[] elements = extension.getConfigurationElements();

			for ( IConfigurationElement element : elements ) {
				if ( "argResolver".equals( element.getName() )) { // $NON-NLS-1$
					try {
						String id					= element.getAttribute("id"); // $NON-NLS-1$
						IArgumentResolver resolver	= (IArgumentResolver)element.createExecutableExtension("class"); // $NON-NLS-1$
						argResolvers.put(id, resolver);
					} catch(Throwable t) {
						t.printStackTrace();
					}
				}
			}
		}
	}

	private Session getSession() throws NotesException {
		if (_session == null) {
			NotesThread.sinitThread();
			_session = NotesFactory.createTrustedSession();
		}

		return _session;
	}

	public void dispose() {
		if (_session != null) {
			NotesUtils.recycle(_session);
			NotesThread.stermThread();
		}
	}

	public String resolve(String var) throws NotesException {
		String resolver		= null;
		String resolvedVar	= var;

		if (var == null)
			return "";

		var = var.trim();
		if ((var.charAt(0) != '{') && (var.charAt(var.length() - 1) != '}'))
			return var;

		var = var.substring(1, var.length() - 1);

		// The substitution variable is either of the form:
		// {notesini!OVERRIDE_HELLOWORLD_TASKLET_EVERY:1"} ==> {resolverIdentifer!variableName:defaultValue}
		// or
		// {generateID:5} ==> {methodName:argument}
		int resolverIdx = var.indexOf("!");

		if (resolverIdx > 0) {
			resolver	= var.substring(0, resolverIdx);
			var			= var.substring(resolverIdx + 1);
		}

		String[]	varParts 	= var.split(":"); // varParts[0]:varParts[1] ==> variableName:defaultValue or methodName:argument
		String		varName		= varParts[0];
		String		varArg		= "";

		if (varParts.length > 1)
			varArg = varParts[1];

		try {
			Method m = null;

			for (java.util.Map.Entry<String, IArgumentResolver> entry : argResolvers.entrySet()) {
				if ((resolver != null) && entry.getKey().equalsIgnoreCase(resolver)) {
					try {
						// Specified resolver required a Session
						m			= entry.getValue().getClass().getMethod("resolve", Session.class, String.class, String.class);
						resolvedVar	= (String)m.invoke(entry.getValue(), getSession(), varName, varArg);
					} catch (NoSuchMethodException ex) {
						// Specified resolver does not require a Session
						m			= entry.getValue().getClass().getMethod("resolve", String.class, String.class);
						resolvedVar	= (String)m.invoke(entry.getValue(), varName, varArg);
					}
					
					break;
				}
				else {
					try {
						// Any resolver that has implemented the given method
						m			= entry.getValue().getClass().getMethod( "_" + varName, String.class);
						resolvedVar	= (String)m.invoke(entry.getValue(), varArg);
					} catch (NoSuchMethodException ex) {
						continue;
					}

					break;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return resolvedVar;
	}
}

