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

import java.util.Dictionary;

import org.eclipse.core.runtime.CoreException;

/**
 * @author dtaieb
 * OSGi service for the Domino OSGi Tasklet Container 
 */
public interface TaskletService {
	
	/**
	 * @param taskId
	 * @param tasklet
	 */
	public void registerTasklet( String taskletId,	AbstractServerTask tasklet);
	
	/**
	 * @param taskid
	 * @param tasklet
	 * @param description
	 */
	public void registerTasklet( String taskletId, AbstractServerTask tasklet, String description );
	
	/**
	 * @param taskid
	 * @param tasklet
	 * @param description
	 * @param runWhens
	 */
	public void registerTasklet( String taskletId, AbstractServerTask tasklet, String description, RunWhen... runWhens );
	
	/**
	 * @param taskletid
	 * @param runWhen
	 * @throws CoreException 
	 */
	public void scheduleTasklet( String taskletId, RunWhen runWhen ) throws CoreException;
	
	/**
	 * @param properties
	 * @throws CoreException
	 */
	public void scheduleTasklet( Dictionary<String, String> properties ) throws CoreException;
	
	/**
	 * @param taskletId
	 * @param args
	 */
	public void runTasklet( String taskletId, String... args );
}
