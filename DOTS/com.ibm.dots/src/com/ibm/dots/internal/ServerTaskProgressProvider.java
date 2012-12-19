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

package com.ibm.dots.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.ProgressProvider;

/**
 * @author dtaieb
 * JobManager Progress Provider
 */
public class ServerTaskProgressProvider extends ProgressProvider implements IJobChangeListener {
	
	private static ServerTaskProgressProvider singleton;
	private Map<Job, IProgressMonitor> jobMonitorMap = Collections.synchronizedMap( new HashMap<Job, IProgressMonitor>() );
	private Object lock = new Object();

	/**
	 * 
	 */
	private ServerTaskProgressProvider() {
		Job.getJobManager().setProgressProvider( this );
		Job.getJobManager().addJobChangeListener( this );
	}
	
	/**
	 * @return
	 */
	public static final ServerTaskProgressProvider getInstance(){
		if ( singleton == null ){
			singleton = new ServerTaskProgressProvider();
		}
		return singleton;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.ProgressProvider#createMonitor(org.eclipse.core.runtime.jobs.Job)
	 */
	@Override
	public IProgressMonitor createMonitor(Job job) {
		synchronized( lock ){
			IProgressMonitor retMonitor = jobMonitorMap.get( job );
			if ( retMonitor != null ){
				return retMonitor;
			}
			retMonitor = new ServerTaskProgressMonitor( job );
			jobMonitorMap.put( job, retMonitor );
			return retMonitor;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#aboutToRun(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	public void aboutToRun(IJobChangeEvent event) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#awake(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	public void awake(IJobChangeEvent event) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#done(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	public void done(IJobChangeEvent event) {
		//Remove from map
		Job j = event.getJob();
		jobMonitorMap.remove( j );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#running(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	public void running(IJobChangeEvent event) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#scheduled(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	public void scheduled(IJobChangeEvent event) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#sleeping(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	public void sleeping(IJobChangeEvent event) {
	}

}
