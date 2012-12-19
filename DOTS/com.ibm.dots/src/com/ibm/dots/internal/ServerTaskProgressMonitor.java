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

import java.text.MessageFormat;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;

/**
 * @author dtaieb
 *
 */
public class ServerTaskProgressMonitor implements IProgressMonitor {

	private String taskId;
	private Job job;
	private int jobId;
	private boolean canceled;

	private int totalWork = 100;
	private int worked = 0;
	private String taskName;
	private String subTaskName;

	/**
	 * @param taskId
	 */
	public ServerTaskProgressMonitor( String taskId ) {
		this.taskId = taskId;
	}

	/**
	 * @param job
	 */
	ServerTaskProgressMonitor( Job job ){
		this.job = job;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IProgressMonitor#beginTask(java.lang.String, int)
	 */
	public void beginTask(String name, int totalWork) {
		setTaskName(name);
		this.totalWork = totalWork;
		worked = 0;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IProgressMonitor#done()
	 */
	public void done() {
		worked = totalWork;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IProgressMonitor#internalWorked(double)
	 */
	public void internalWorked(double work) {
		if ( work == 0 ){
			return;
		}
		worked += work;
		if ( worked > totalWork ){
			worked = totalWork;
		}

		if ( worked < 0 ){
			worked = 0;
		}       
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IProgressMonitor#isCanceled()
	 */
	public boolean isCanceled() {
		return canceled;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IProgressMonitor#setCanceled(boolean)
	 */
	public void setCanceled(boolean value) {
		this.canceled = value;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IProgressMonitor#setTaskName(java.lang.String)
	 */
	public void setTaskName(String name) {
		taskName = name;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IProgressMonitor#subTask(java.lang.String)
	 */
	public void subTask(String name) {
		subTaskName = name;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IProgressMonitor#worked(int)
	 */
	public void worked(int work) {
		internalWorked( work );
	}

	/**
	 * @return
	 */
	private String getInternalLabel(){
		if ( taskId != null ){
			return taskId;
		}

		if ( job != null ){
			return job.getName();
		}

		return "";
	}

	/**
	 * @return
	 */
	public String getStatus() {
		StringBuilder sb = new StringBuilder();
		sb.append( MessageFormat.format( "{0} {1} ({2}%%)", getInternalLabel(), getTaskLabel(), (int)((double)worked) / ((double)totalWork) * 100 ) );
		if ( isCanceled() ){
			sb.append( "(Canceled) " ); // $NON-NLS-1$
		}
		return sb.toString();
	}

	/**
	 * @return
	 */
	private String getTaskLabel() {
		if ( taskName == null ){
			return getInternalLabel();
		}else if ( subTaskName == null ){
			return "[" +taskName + "]";
		}
		return "[" +taskName + "-" + subTaskName + "]";
	}

	/**
	 * @return
	 */
	public String getTaskId() {
		return taskId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public int getJobId() {
		return jobId;
	}
}
