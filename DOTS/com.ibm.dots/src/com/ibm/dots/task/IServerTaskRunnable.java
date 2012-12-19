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

import org.eclipse.core.runtime.IProgressMonitor;

import lotus.domino.NotesException;

/**
 * @author dtaieb
 * Interface used to run a task
 */
public interface IServerTaskRunnable {
	
	/**
	 * @param serverTaskInfo
	 * Called before the task is ready to run
	 */
	public void init( ServerTaskInfo serverTaskInfo );

	/**
	 * @param runWhen
	 * @param args: command line arguments
	 * @param monitor
	 * @throws NotesException
	 */
	public void run(RunWhen runWhen, String[] args, IProgressMonitor monitor) throws NotesException;

	/**
	 * Called when the task instance is being discarded
	 * Task should recycle any created backend objects 
	 */
	public void dispose() throws NotesException;
	
	/**
	 * @param message
	 */
	public void logMessage( String message );
	
	/**
	 * @param t
	 */
	public void logException( Throwable t );


}
