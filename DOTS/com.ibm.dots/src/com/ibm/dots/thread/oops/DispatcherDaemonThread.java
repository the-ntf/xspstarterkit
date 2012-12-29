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
public abstract class DispatcherDaemonThread extends Thread {
	private boolean keepAlive_;
	private Object sleepMonitor;

	public DispatcherDaemonThread() {
		setName("DOTS Generic Dispatcher");
		setDaemon(true);
	}

	public DispatcherDaemonThread(String threadName) {
		setName(threadName);
		setDaemon(true);
	}

	protected abstract Set<ServerTaskInfo> getTasks();

	protected abstract void execute(ServerTaskInfo task, Object payload);

	protected Object currentPayload_;

	protected Object getCurrentPayload() {
		return currentPayload_;
	}

	@Override
	public void run() {
		while (keepAlive()) {
			Set<ServerTaskInfo> tasks = getTasks();
			synchronized (tasks) {
				// IExtensionManagerEvent curEvent = ServerTaskManager.INSTANCE.getQueuedEvent(eventId);
				// if (curEvent != null) {
				for (ServerTaskInfo info : tasks) {
					execute(info, getCurrentPayload());
				}
				// ServerTaskManager.INSTANCE.scheduleNextTrigger(info, curEvent);
				// }
				// }
			}
		}
	}

	public void dispose() {
		keepAlive_ = false;
	}

	public void wake() {
		synchronized (sleepMonitor) {
			sleepMonitor.notify();
		}
	}

	private boolean keepAlive() {
		return keepAlive_;
	}
}
