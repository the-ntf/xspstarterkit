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
package com.ibm.dots;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;

import com.ibm.dots.internal.InternalNotesJob;
import com.ibm.dots.startup.IStartup;
import com.ibm.dots.task.ServerTaskManager;

/**
 * @author dtaieb
 *
 */
class RunEarlyStartupExtensionJob extends InternalNotesJob{

	private IExtensionRegistry registry;

	/**
	 * @param name
	 */
	public RunEarlyStartupExtensionJob( IExtensionRegistry registry ) {
		super( "Domino OSGi Tasklet Container Run Early Startup Job" ); // $NON-NLS-1$
		setSystem( true );

		this.registry = registry;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.ISafeRunnable#run()
	 */
	public void run() throws Exception {
		IExtensionPoint startupExtensionPoint = registry.getExtensionPoint( Activator.PLUGIN_ID, "startup"); // $NON-NLS-1$
		if ( startupExtensionPoint == null ){
			ServerTaskManager.getInstance().logMessageText( "Unable to find extension point : startup" ); // $NON-NLS-1$
			return;
		}

		IConfigurationElement[] elements = startupExtensionPoint.getConfigurationElements();
		for ( int i = 0; i < elements.length; i++ ){
			IConfigurationElement element = elements[i];
			if (element != null && "startup".equals( element.getName() ) ) { //$NON-NLS-1$
				try {
					IStartup startupClass = (IStartup)element.createExecutableExtension( "class" ); //$NON-NLS-1$
					startupClass.earlyStartup();
				} catch (CoreException e) {
					//This one failed, but let's keep going
					handleException( e );
				}
			}
		}

	}

}
