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
package org.openntf.xsp.starter;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;

public class Activator extends Plugin {
	public static final String PLUGIN_ID = Activator.class.getPackage().getName();
	private static Boolean DEBUG = null;

	public static boolean isDebug() {
		if (DEBUG = null) {
			DEBUG = Boolean.FALSE;
			String[] envs = getEnvironmentStrings();
			for (String s : envs) {
				if (s.equalsIgnoreCase("debug")) {
					DEBUG = Boolean.TRUE;
				}
			}
		}
		return DEBUG.booleanValue();
	}

	public static String[] getEnvironmentStrings() {
		String[] result = null;
		try {
			String setting = Platform.getInstance().getProperty(PLUGIN_ID); // $NON-NLS-1$
			if (StringUtil.isEmpty(setting)) {
				setting = System.getProperty(PLUGIN_ID); // $NON-NLS-1$
				if (StringUtil.isEmpty(setting)) {
					setting = com.ibm.xsp.model.domino.DominoUtils.getEnvironmentString(PLUGIN_ID); // $NON-NLS-1$
				}
			}
			if (StringUtil.isNotEmpty(setting)) {
				result = StringUtil.splitString(setting, ',');
			}
		} catch (Throwable t) {

		}
		return result;
	}

	public static Activator instance;

	public Activator() {
		instance = this;
	}

	private static String version;

	public static String getVersion() {
		if (version == null) {
			version = (String) instance.getBundle().getHeaders().get("Bundle-Version");
		}
		return version;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		// version = (String) context.getBundle().getHeaders().get("Bundle-Version");
	}

	public static Activator getDefault() {
		return instance;
	}
}