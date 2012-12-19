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

import java.util.ArrayList;
import java.util.HashMap;

import lotus.domino.NotesException;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author dtaieb
 * Abstract class that provide extra facilities to access arguments
 */
public abstract class AbstractServerTaskExt extends AbstractServerTask {
	
	private HashMap<String, String> arguments = null;
	private ArrayList<String> orderedArgs = null;

	/**
	 * 
	 */
	public AbstractServerTaskExt() {
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.task.IServerTaskRunnable#run(com.ibm.dots.task.RunWhen, java.lang.String[], org.eclipse.core.runtime.IProgressMonitor)
	 */
	public final void run(RunWhen runWhen, String[] args, IProgressMonitor monitor)throws NotesException {
		try{
			//Load the arguments
			loadArguments( args );
			
			//Call the doRun methods, subclasses can call getArgument method to access the run arguments
			doRun( runWhen, monitor );
		}finally{
			//Reset the arguments for the next run
			arguments = null;
			orderedArgs = null;
		}
	}

	/**
	 * @param runWhen
	 * @param monitor
	 */
	protected abstract void doRun(RunWhen runWhen, IProgressMonitor monitor) throws NotesException;

	/**
	 * @param args
	 */
	private void loadArguments(String[] args) {
		arguments = new HashMap<String, String>();
		orderedArgs = new ArrayList<String>();
		
		for ( String arg : args ){
			orderedArgs.add( arg );
			addArgument( arg );
		}
		
	}

	/**
	 * @param arg
	 */
	private void addArgument(String arg) {
		if ( arg.startsWith( "-") ){
			arg = arg.substring( 1 );
		}
		int iIndex = arg.indexOf('=');
		String key = null;
		String value = null;
		if ( iIndex == -1 ){
			key = arg;
		}else{
			key = arg.substring( 0, iIndex );
			if ( iIndex + 1 < arg.length() ){
				value = arg.substring( iIndex + 1 );
			}
		}
		arguments.put( key, value );		
	}
	
	/**
	 * @param key
	 * @return: the value for the keyed argument
	 */
	public String getKeyedArgument( String key ){
		if ( arguments == null ){
			return null;
		}
		return arguments.get( key );
	}
	
	/**
	 * @param n
	 * @return: the Nth arguments as it was passed to the task by the user
	 */
	public String getNthArgument( int n ){
		if ( orderedArgs == null ){
			return null;
		}
		
		if ( n >= orderedArgs.size() ){
			return null;
		}
		
		return orderedArgs.get( n );
	}
	
	/**
	 * @param key
	 * @return true if argument keyed by key is present
	 */
	public boolean hasKeyedArgument( String key ){
		return arguments == null ? false : arguments.containsKey( key );
	}
}
