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

import lotus.domino.NotesThread;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.ibm.dots.task.ServerTaskManager;

/**
 * @author dtaieb
 * abstract class for all internal jobs
 */
public abstract class InternalNotesJob extends Job implements ISafeRunnable {

	/**
	 * @param name
	 */
	public InternalNotesJob(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.ISafeRunnable#handleException(java.lang.Throwable)
	 */
	public void handleException(Throwable t) {
		ServerTaskManager.getInstance().logMessageText( t.getMessage() );
		t.printStackTrace();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor ) {
		try{
			NotesThread.sinitThread();
			SafeRunner.run( this );
			return Status.OK_STATUS;
		}finally{
			NotesThread.stermThread();
		}
	}
}
