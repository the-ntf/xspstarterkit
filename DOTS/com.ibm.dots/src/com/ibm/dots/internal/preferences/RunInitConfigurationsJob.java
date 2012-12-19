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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;

import com.ibm.dots.Activator;
import com.ibm.dots.internal.InternalNotesJob;
import com.ibm.dots.preferences.AbstractConfigurationInitializer;
import com.ibm.dots.task.ServerTaskManager;
import com.ibm.dots.utils.Platform;

/**
 * @author dtaieb
 * Job that initialize NSF based preferences
 */
public class RunInitConfigurationsJob extends InternalNotesJob {

	private IExtensionRegistry registry;

	/**
	 * @param name
	 */
	public RunInitConfigurationsJob( IExtensionRegistry registry ) {
		super( "Initialize Configurations" ); // $NON-NLS-1$
		setSystem( true );

		this.registry = registry;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.ISafeRunnable#run()
	 */
	public void run() throws Exception {
		IExtensionPoint configurationExtensionPoint = registry.getExtensionPoint(   Activator.PLUGIN_ID, "configuration"); // $NON-NLS-1$
		if ( configurationExtensionPoint == null ){
			ServerTaskManager.getInstance().logMessageText( "Unable to find extension point : configuration" ); // $NON-NLS-1$
			return;
		}

		IExtension[] extensions = configurationExtensionPoint.getExtensions();
		for ( IExtension extension : extensions ){
			String dxlPath = null;
			AbstractConfigurationInitializer initializer = null;
			IConfigurationElement[] elements = extension.getConfigurationElements();
			for ( IConfigurationElement element : elements ){
				if ( "form".equals( element.getName() ) ) { //$NON-NLS-1$
					dxlPath = element.getAttribute( "dxlpath" ); // $NON-NLS-1$
				}else if ( "initializer".equals( element.getName() ) ){ //$NON-NLS-1$
					initializer = (AbstractConfigurationInitializer)element.createExecutableExtension( "class" ); // $NON-NLS-1$
				}
			}
			if ( dxlPath != null ){
				DominoOSGiScope.getInstance().initializeConfigurationForPlugin( extension.getContributor().getName(), dxlPath, initializer );
			}
		}
	}

	/**
	 * @param pluginId
	 * @param bIsRoot
	 * @return
	 * @throws CoreException 
	 */
	protected static AbstractConfigurationInitializer findInitializer( String pluginId, boolean bIsRoot ) throws CoreException {
		if ( bIsRoot ){
			return Platform.getRootConfigurationInitializer();
		}
		IExtensionPoint configurationExtensionPoint = Activator.getDefault().getRegistry().getExtensionPoint(   Activator.PLUGIN_ID, "configuration"); // $NON-NLS-1$
		if ( configurationExtensionPoint == null ){
			return null;
		}

		IExtension[] extensions = configurationExtensionPoint.getExtensions();
		for ( IExtension extension : extensions ){
			if ( extension.getContributor().getName().equals( pluginId ) ){
				IConfigurationElement[] elements = extension.getConfigurationElements();
				String dxlPath = null;
				AbstractConfigurationInitializer initializer = null;
				for ( IConfigurationElement element : elements ){
					if ( "form".equals( element.getName() ) ) { //$NON-NLS-1$
						dxlPath = element.getAttribute( "dxlpath" ); // $NON-NLS-1$
					}else if ( "initializer".equals( element.getName() ) ){ //$NON-NLS-1$
						initializer = (AbstractConfigurationInitializer)element.createExecutableExtension( "class" ); // $NON-NLS-1$
					}
				}
				if ( dxlPath != null && initializer != null ){
					initializer.setDxlPath( pluginId, dxlPath );
					return initializer;
				}
				break;
			}
		}
		return null;
	}
}
