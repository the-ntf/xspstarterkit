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
package com.ibm.dots.internal.logging;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.eclipse.osgi.framework.debug.FrameworkDebugOptions;

/**
 * @author dtaieb
 * List of dots logging groups
 */
public class LoggingGroups {

	private static final String SERVER_TASK_MANAGER_LOGGER_NAME = "serverTaskManager"; // $NON-NLS-1$
	private static volatile Logger serverTaskManagerLogger;

	/**
	 * 
	 */
	private LoggingGroups() {
	}

	/**
	 * @return
	 */
	public static Logger getServerTaskManagerLogger(){
		if ( serverTaskManagerLogger == null ){
			synchronized( LoggingGroups.class ){
				if ( serverTaskManagerLogger == null ){
					serverTaskManagerLogger = getLogger( SERVER_TASK_MANAGER_LOGGER_NAME );
				}
			}
		}
		return serverTaskManagerLogger;
	}

	/**
	 * @param loggerName
	 * @return
	 */
	private static Logger getLogger(String loggerName ) {
		Logger logger = Logger.getLogger( loggerName );
		String level = FrameworkDebugOptions.getDefault().getOption( "com.ibm.dots/logger/" + loggerName + "/level" ); // $NON-NLS-1$ $NON-NLS-2$
		if ( level == null ){
			level = LogManager.getLogManager().getProperty( loggerName + ".level"); //$NON-NLS-1$
		}

		if ( level != null ){
			logger.setLevel( Level.parse( level ) );
		}
		return logger;
	}

}