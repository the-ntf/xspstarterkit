/*
 * © Copyright GBS Inc 2011
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
package org.openntf.xsp.starter.library;

import org.openntf.xsp.starter.Activator;

import com.ibm.xsp.library.AbstractXspLibrary;

public class StarterLibrary extends AbstractXspLibrary {
	private final static String LIBRARY_ID = StarterLibrary.class.getName();
	// change this string to establish a namespace for your resources:
	public final static String LIBRARY_RESOURCE_NAMESPACE = "Starter";
	public final static String LIBRARY_BEAN_PREFIX = "Starter";
	private final static boolean _debug = Activator.isDebug();

	static {
		if (_debug) {
			System.out.println(StarterLibrary.class.getName() + " loaded");
		}
	}

	public StarterLibrary() {
		if (_debug) {
			System.out.println(StarterLibrary.class.getName() + " created");
		}
	}

	public String getLibraryId() {
		return LIBRARY_ID;
	}

	@Override
	public String getPluginId() {
		return Activator.PLUGIN_ID;
	}

	@Override
	public String[] getDependencies() {
		return new String[] { "com.ibm.xsp.core.library", "com.ibm.xsp.extsn.library", "com.ibm.xsp.domino.library",
				"com.ibm.xsp.designer.library" };
	}

	@Override
	public String[] getXspConfigFiles() {
		String[] files = new String[] { "META-INF/starter.xsp-config", "META-INF/html.xsp-config", "META-INF/canvas.xsp-config" };

		return files;
	}

	@Override
	public String[] getFacesConfigFiles() {
		String[] files = new String[] { "META-INF/starter-faces-config.xml", "META-INF/html-faces-config.xml",
				"META-INF/canvas-faces-config.xml" };
		return files;
	}

	private static Boolean GLOBAL = null;

	private static boolean isGlobal() {
		if (GLOBAL = null) {
			GLOBAL = Boolean.FALSE;
			String[] envs = Activator.getEnvironmentStrings();
			for (String s : envs) {
				if (s.equalsIgnoreCase("global")) {
					GLOBAL = Boolean.TRUE;
				}
			}
		}
		return GLOBAL.booleanValue();
	}

	@Override
	public boolean isGlobalScope() {
		return isGlobal();
	}
}
