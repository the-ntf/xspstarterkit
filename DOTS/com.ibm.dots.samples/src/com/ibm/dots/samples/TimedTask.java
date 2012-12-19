/**
 * 
 */
package com.ibm.dots.samples;

import java.text.MessageFormat;

import lotus.domino.NotesException;

import org.eclipse.core.runtime.IProgressMonitor;

import com.ibm.dots.task.AbstractServerTask;
import com.ibm.dots.task.RunWhen;

/**
 * @author dtaieb
 *
 */
public class TimedTask extends AbstractServerTask {

	/**
	 * 
	 */
	public TimedTask() {
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.task.IServerTaskRunnable#run(com.ibm.dots.task.RunWhen, java.lang.String[], org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void run(RunWhen runWhen, String[] args, IProgressMonitor monitor) throws NotesException {
		logMessage( MessageFormat.format( "Task scheduled to run between specific times: {0}", runWhen ) );
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.task.IServerTaskRunnable#dispose()
	 */
	public void dispose() throws NotesException {
	}

}
