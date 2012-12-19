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
package com.ibm.dots.samples;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.NotesThread;
import lotus.domino.Session;

import com.ibm.dots.task.ITaskFilter;
import com.ibm.dots.utils.NotesUtils;

/**
 * @author dtaieb
 *
 */
public class ScheduledTaskFilter implements ITaskFilter {

	/**
	 * 
	 */
	public ScheduledTaskFilter() {
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.task.ITaskFilter#canLoad(java.lang.String)
	 */
	public boolean canLoad(String taskId) {
		NotesThread.sinitThread();
		Session session = null;
		try{
			session = NotesFactory.createSession();
			String load = session.getEnvironmentString( "DOTS_SAMPLE_LOAD_SCHEDULED_TASK", true );
			if ( load != null && !load.isEmpty() && !Boolean.valueOf( load ) ){
				return false;
			}
			return true;
		} catch (NotesException e) {
			e.printStackTrace();
			return false;
		}finally{
			NotesUtils.recycle( session );
			NotesThread.stermThread();
		}
	}

}
