/*
 * © Copyright IBM Corp. 2009,2010
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

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

import com.ibm.dots.Activator;

/**
 * @author dtaieb
 * Class manages access to the server console
 */
public class ServerConsole {

	private String taskPrefix;  //prefix used when sending message to the console

	/**
	 * @param taskPrefix
	 */
	public ServerConsole( String taskPrefix ) {
		this.taskPrefix = taskPrefix;
	}

	/**
	 * default constructor
	 */
	public ServerConsole(){
		taskPrefix = "";
	}

	/**
	 * @param message
	 */
	public void logMessage( String message ){
		if ( message == null ){
			message = "null"; // $NON-NLS-1$
		}
		//Print the message one by one
		String[] lines = message.split("\n"); // $NON-NLS-1$
		for ( String line : lines ){
			ServerTaskManager.getInstance().logMessageText( taskPrefix + line );
		}

	}

	/**
	 * @param t
	 */
	public void logException( Throwable t ){
		String message = t.getMessage();
		if ( message == null ){
			message = t.getClass().getName();
		}

		logMessage( "A Java Exception occurred: " + message );
		StringWriter sw = new StringWriter();
		t.printStackTrace( new PrintWriter( sw ) );
		logMessage( sw.toString() );
	}

	/**
	 * @param pluginId
	 * @param message
	 */
	public void logPlatform( String pluginId, String message ) {
		Platform.getLog( Activator.getDefault().getBundle() ).log( new Status( Status.ERROR, pluginId, message) );      
	}
    /**
     * @param pluginId
     * @param message
     * @param t
     */
    public void logPlatform( String pluginId, String message, Throwable t ){
    	Platform.getLog( Activator.getDefault().getBundle() ).log( new Status( Status.ERROR, pluginId, message, t ) );
    }

	/**
	 * @param message
	 */
	public void printAndLog(String message) {
		logMessage( message );
		logPlatform( Activator.PLUGIN_ID, message );
	}

	/**
	 * @param objects
	 */
	public void printAndLog(Object[] objects) {
		for ( Object object : objects ){
			if ( object != null ){
				printAndLog( "\t" + object.toString() ); // $NON-NLS-1$
			}
		}
	}

}
