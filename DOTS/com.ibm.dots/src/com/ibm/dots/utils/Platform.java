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
package com.ibm.dots.utils;

import lotus.domino.Document;
import lotus.domino.NotesException;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import com.ibm.dots.Activator;
import com.ibm.dots.internal.OSGiProfileProxy;
import com.ibm.dots.internal.preferences.DominoOSGiScope;
import com.ibm.dots.internal.preferences.IPrefConstants;
import com.ibm.dots.preferences.AbstractConfigurationInitializer;

/**
 * @author dtaieb
 * Class used to statically access services from the DOTS runtime
 * e.g plugin preferences, etc...
 */
public final class Platform implements IPrefConstants {
	
	/**
	 * Not instantiable
	 */
	private Platform() {
	}
	
	/**
	 * @return
	 */
	public static String getProfileName() {
		return OSGiProfileProxy.getProfileName();
	}
	
	/**
	 * @param pluginId
	 * @return the eclipse preferences for the plugin
	 * Preferences returned by this method are stored in an NSF Database
	 */
	public static IEclipsePreferences getPreferences(String pluginId) {
		IEclipsePreferences retPrefs = DominoOSGiScope.getInstance().getNode( pluginId );		
		return retPrefs;
	}

	/**
	 * @return
	 */
	public static AbstractConfigurationInitializer getRootConfigurationInitializer() {
		//Return the built-in initializer for root
		AbstractConfigurationInitializer rootInitializer = new AbstractConfigurationInitializer() {				
			@Override
			protected void initializeDefaultConfigurationParameters(Document newDoc) throws NotesException {
				
			}
		};
		rootInitializer.setDxlPath( Activator.PLUGIN_ID, ROOT_CONFIG_FORM_DXLPATH);
		return rootInitializer;
	}
	
	/**
	 * @return the initializer for the default configuration form
	 */
	public static AbstractConfigurationInitializer getDefaultConfigurationInitializer(){
		//Return the built-in initializer for root
		AbstractConfigurationInitializer defaultInitializer = new AbstractConfigurationInitializer() {				
			@Override
			protected void initializeDefaultConfigurationParameters(Document newDoc) throws NotesException {
				
			}
		};
		defaultInitializer.setDxlPath( Activator.PLUGIN_ID, DEFAULT_CONFIG_FORM_DXLPATH);
		return defaultInitializer;		
	}
}
