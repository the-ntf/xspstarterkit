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
package com.ibm.dots.internal.preferences;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.ibm.dots.Activator;
import com.ibm.dots.internal.OSGiProfileProxy;
import com.ibm.dots.preferences.AbstractConfigurationInitializer;
import com.ibm.dots.preferences.IDominoPreferences;

/**
 * @author dtaieb
 *
 */
@SuppressWarnings("restriction") // $NON-NLS-1$
public class DominoOSGiScope implements IScopeContext {

	private static final String SCOPE = "OSGiInstance"; //$NON-NLS-1$

	private static volatile IPreferencesService prefService;
	private static final DominoOSGiScope singleton = new DominoOSGiScope();

	/**
	 * 
	 */
	private DominoOSGiScope() {
	}

	/**
	 * @return
	 */
	public static DominoOSGiScope getInstance(){
		return singleton;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.preferences.IScopeContext#getName()
	 */
	public String getName() {
		return SCOPE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.preferences.IScopeContext#getNode(java.lang.String)
	 */
	public IEclipsePreferences getNode(String qualifier) {
		if (qualifier == null){
			throw new IllegalArgumentException();
		}
		try {
			if ( OSGiProfileProxy.hasConfigDb() && OSGiProfileProxy.isUsingConfigDb() ){
				return (IDominoPreferences)getDominoRootPreference().node(qualifier);
			}
		} catch (Exception e) {
		}

		//Fall back to Instance Scope
		return (IEclipsePreferences)getPreferenceService().getRootNode().node( InstanceScope.SCOPE );
	}

	/**
	 * @return
	 */
	protected DominoOSGiPreferences getDominoRootPreference() {
		return (DominoOSGiPreferences)getPreferenceService().getRootNode().node(getName());
	}

	/**
	 * @return
	 */
	private synchronized IPreferencesService getPreferenceService() {
		if ( prefService == null ){
			BundleContext context = Activator.getDefault().getBundle().getBundleContext();
			ServiceReference ref = context.getServiceReference( IPreferencesService.class.getName() );
			prefService = (IPreferencesService) context.getService( ref );
			context.ungetService( ref );

			//Initialize the root node with this scope
			new DominoOSGiPreferences().create( prefService.getRootNode(), getName() );
		}
		return prefService;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.preferences.IScopeContext#getLocation()
	 */
	public IPath getLocation() {
		return null;
	}

	/**
	 * @param pluginId
	 * @param dxlPath
	 * @param initializer
	 */
	protected void initializeConfigurationForPlugin(String pluginId, String dxlPath, AbstractConfigurationInitializer initializer) {
		getDominoRootPreference().node( pluginId, dxlPath, initializer );
	}

}
