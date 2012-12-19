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
package com.ibm.dots.task;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.ibm.dots.Activator;

/**
 * @author dtaieb
 *
 */
public class AliasTaskInfo {

	private String id;
	private String targetTask;
	private String description;
	private HashMap<String, String> args = new HashMap<String, String>();

	/**
	 * @throws CoreException 
	 * 
	 */
	public AliasTaskInfo( IConfigurationElement element ) throws CoreException {
		id = element.getAttribute("id"); // $NON-NLS-1$
		if ( id == null ){
			throw new CoreException( new Status( IStatus.ERROR, Activator.PLUGIN_ID, "Missing id attribute")); // $NON-NLS-1$
		}

		description = element.getAttribute( "description" ); // $NON-NLS-1$

		targetTask = element.getAttribute("targetTask"); // $NON-NLS-1$
		if ( targetTask == null ){
			throw new CoreException( new Status( IStatus.ERROR, Activator.PLUGIN_ID, "Missing targetTask attribute")); // $NON-NLS-1$
		}

		//Arguments
		IConfigurationElement[] children = element.getChildren();
		for ( IConfigurationElement child : children ){
			if ( "arg".equalsIgnoreCase( child.getName() )){ // $NON-NLS-1$
				addArg( child );
			}
		}
	}

	/**
	 * @param child
	 * @throws CoreException 
	 */
	private void addArg(IConfigurationElement child) throws CoreException {
		String name = child.getAttribute( "name" ); // $NON-NLS-1$
		String value = child.getAttribute( "value" ); // $NON-NLS-1$

		if ( name == null ){
			throw new CoreException( new Status( IStatus.ERROR, Activator.PLUGIN_ID, "Missing name attribute for an argument")); // $NON-NLS-1$
		}

		args.put( name, value);
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return
	 */
	public String getTargetTaskId() {
		return targetTask;
	}

	/**
	 * @param orgArgs: arguments to merge with the alias arguments
	 * @return
	 */
	public String[] getArguments( String[] orgArgs ) {
		HashSet<String> arguments = new HashSet<String>();
		for ( String argName : args.keySet() ){
			String argValue = args.get( argName );
			String s = argName;
			if ( argValue != null ){
				s += ( "=" + argValue );
			}
			arguments.add( s );
		}

		//Add the orgArgs, note that they won't be added if already present
		if ( orgArgs != null ){
			for ( String arg : orgArgs ){
				arguments.add( arg );
			}
		}

		return arguments.toArray( new String[0] );
	}

	@Override
	public String toString() {
		if ( description != null ){
			return id + " : " + description;
		}
		return id;
	}

}
