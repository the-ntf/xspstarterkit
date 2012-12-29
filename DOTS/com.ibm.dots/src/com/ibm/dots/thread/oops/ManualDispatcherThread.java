/**
 * 
 */
package com.ibm.dots.thread.oops;

import java.util.Set;

import com.ibm.dots.task.ServerTaskInfo;

/**
 * @author nfreeman
 *
 */
public class ManualDispatcherThread extends DispatcherDaemonThread {

	/**
	 * 
	 */
	public ManualDispatcherThread() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param threadName
	 */
	public ManualDispatcherThread(String threadName) {
		super(threadName);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.thread.DispatcherDaemonThread#getTasks()
	 */
	@Override
	protected Set<ServerTaskInfo> getTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.thread.DispatcherDaemonThread#execute(com.ibm.dots.task.ServerTaskInfo, java.lang.Object)
	 */
	@Override
	protected void execute(ServerTaskInfo task, Object payload) {
		// TODO Auto-generated method stub

	}

}
