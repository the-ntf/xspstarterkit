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
package com.ibm.dots.task;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import lotus.domino.NotesFactory;
import lotus.domino.NotesThread;
import lotus.domino.Session;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.framework.console.CommandInterpreter;

import com.ibm.dots.annotation.HungPossibleAfter;
import com.ibm.dots.event.ExtensionManagerBridge;
import com.ibm.dots.event.IExtensionManagerEvent;
import com.ibm.dots.internal.ServerTaskProgressMonitor;
import com.ibm.dots.task.ServerTaskManager.ITaskService;
import com.ibm.dots.utils.ThreadMonitor;

/**
 * @author dtaieb
 * 
 */
class ServiceThread extends Thread implements ITaskService {
	static class LocalSession extends ThreadLocal<Session> {
		private boolean isCleared = false;

		@Override
		protected Session initialValue() {
			Session result = null;
			if (!isCleared) {
				try {
					result = NotesFactory.createTrustedSession();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			return result;
		};

		public void recycle() {
			try {
				super.get().recycle();
			} catch (Throwable t) {
				// t.printStackTrace();
			}
		}

		public void clear() {
			// System.out.println("Clearing a thread local session object...");
			isCleared = true;
			recycle();
			super.set(null);
		}

		@Override
		public Session get() {
			if (super.get() == null) {
				try {
					super.set(NotesFactory.createTrustedSession());
					// System.out.println("Created a new trusted session in thread local!");
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			return super.get();
		}
	}

	private static LocalSession localSession = new LocalSession();

	private Object sleepMonitor = new Object(); // Monitor the sleeping of the thread
	// private volatile boolean bRunning = false; //indicate that the Thread is currently running
	private volatile boolean shouldStop = false; // indicate that the Thread should stop at the next opportunity
	private long serviceTaskLastPickupTime = 0;
	private long lastLogTime = 0;

	private ThreadMonitor threadMonitor = new ThreadMonitor(this);

	// final Set<ServiceTask> tasks = Collections.synchronizedSet(new LinkedHashSet<ServiceTask>());
	private final Queue<ServiceTask> taskQueue_ = new ConcurrentLinkedQueue<ServiceTask>();

	// private volatile ServiceTask currentTask;

	HashSet<IServerTaskRunnable> scheduledTasks = null;

	// Private class used to service the task
	private class ServiceTask {
		ServerTaskInfo serverTaskInfo;
		RunWhen runWhen;
		String[] args;
		ServerTaskProgressMonitor progressMonitor;
		private long taskTimeOut;

		ServiceTask(ServerTaskInfo serverTaskInfo, RunWhen runWhen, String[] args) {
			this.serverTaskInfo = serverTaskInfo;
			this.runWhen = runWhen;
			this.args = args;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof ServiceTask) {
				ServiceTask sTask = (ServiceTask) o;
				return equals(serverTaskInfo.getId(), sTask.serverTaskInfo.getId()) && equals(runWhen, sTask.runWhen)
						&& equalsArgs(args, sTask.args);
			}
			return super.equals(o);
		}

		/**
		 * @param args1
		 * @param args2
		 * @return
		 */
		private boolean equalsArgs(String[] args1, String[] args2) {
			if (args1 == args2) {
				return true;
			}
			if (args1 == null || args2 == null) {
				return false;
			}
			if (args1.length != args2.length) {
				return false;
			}
			for (int i = 0; i < args1.length; i++) {
				if (!equals(args1[i], args2[i])) {
					return false;
				}
			}
			return true;
		}

		@Override
		public int hashCode() {
			int retValue = serverTaskInfo.getId().hashCode();
			if (runWhen != null) {
				retValue += runWhen.hashCode();
			}
			if (args != null) {
				for (String arg : args) {
					retValue += arg.hashCode();
				}
			}
			return retValue;
		}

		/**
		 * @param o1
		 * @param o2
		 * @return
		 */
		private boolean equals(Object o1, Object o2) {
			if (o1 == o2) {
				return true;
			}
			if (o1 == null || o2 == null) {
				return false;
			}
			return o1.equals(o2);
		}

		/**
		 * @param session
		 */
		public void run(Session session) {
			IServerTaskRunnable taskRunnable = null;
			try {
				taskRunnable = serverTaskInfo.getServerTaskRunnable();

				// Add to the list of task to be disposed
				if (scheduledTasks == null) {
					scheduledTasks = new HashSet<IServerTaskRunnable>();
				}
				if (!scheduledTasks.contains(taskRunnable)) {
					scheduledTasks.add(taskRunnable);
				}
				HungPossibleAfter hpa = taskRunnable.getClass().getAnnotation(HungPossibleAfter.class);
				if (hpa != null) {
					taskTimeOut = hpa.timeInMinutes() * 60 * 1000L;
				}

				if (taskRunnable instanceof AbstractServerTask) {

					((AbstractServerTask) taskRunnable).setSession(session);
				}
				progressMonitor = new ServerTaskProgressMonitor(serverTaskInfo.getId());

				if (runWhen.annotatedMethod == null) {
					taskRunnable.run(runWhen, args, progressMonitor);
				} else {
					// Invoke the annotated method
					hpa = runWhen.annotatedMethod.getAnnotation(HungPossibleAfter.class);
					if (hpa != null) {
						taskTimeOut = hpa.timeInMinutes() * 60 * 1000L;
					}
					IExtensionManagerEvent emEvent = runWhen.getExtensionManagerEvent();
					if (emEvent == null) {
						runWhen.annotatedMethod.invoke(taskRunnable, progressMonitor);
					} else {
						// This is a em triggered tasklet
						Class<?> emEventClass = ExtensionManagerBridge.getEMEventClass(emEvent);
						if (emEventClass == null) {
							// System.out.println("Unable to find emEventClass from emEvent " + emEvent.getEventId());
						} else {
							if (Arrays.equals(runWhen.annotatedMethod.getParameterTypes(), new Class<?>[] { emEventClass,
									IProgressMonitor.class })) {
								runWhen.annotatedMethod.invoke(taskRunnable, emEvent, progressMonitor);
							} else {
								StringBuilder sb = new StringBuilder();
								for (Class c : runWhen.annotatedMethod.getParameterTypes()) {
									sb.append("Class " + c.getName());
								}
								// System.out.println("Method " + runWhen.annotatedMethod.getName() + " has wrong param signature. "
								// + sb.toString() + " when we have a " + emEventClass.getName());
							}
						}
					}
				}
			} catch (Throwable e) {
				taskRunnable.logException(e);
				e.printStackTrace();
			} finally {
				progressMonitor = null;
				if (taskRunnable instanceof AbstractServerTask) {
					((AbstractServerTask) taskRunnable).clearSession();
				}
				// System.out.println("Completed running a tasklet " + serverTaskInfo);
			}
		}

		/**
		 * @return
		 */
		public long getTimeOut() {
			return taskTimeOut;
		}

		/**
		 * @return
		 */
		public String getDisplayName() {
			String retValue = serverTaskInfo.getId();
			if (runWhen != null && runWhen.annotatedMethod != null) {
				retValue += "@" + runWhen.annotatedMethod.getName();
			}
			return retValue;
		}
	}

	/**
	 * 
	 */
	public ServiceThread() {
		this(null, null);
	}

	/**
	 * @param threadName
	 */
	public ServiceThread(String threadName) {
		this(null, threadName);
	}

	@Override
	public String toString() {
		return getServiceName();
	}

	/**
	 * @param group
	 * @param threadName
	 */
	public ServiceThread(ThreadGroup group, String threadName) {
		super(group, threadName);
		setDaemon(true);

		// Start the thread
		start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.task.ServerTaskManager.ITaskService#getServiceName()
	 */
	@Override
	public String getServiceName() {
		return getName();
	}

	/**
	 * @return
	 */
	private boolean keepAlive() {
		return !shouldStop;
	}

	/**
	 * Wake up the thread by notifying the sleep monitor
	 */
	private void wakeUp() {
		synchronized (sleepMonitor) {
			sleepMonitor.notify();
		}
	}

	@Override
	public synchronized void start() {
		// bRunning = true;
		super.start();
	}

	private Session getSession() {
		return localSession.get();
	}

	@Override
	public void run() {
		// Initialize the thread
		NotesThread.sinitThread();
		// bRunning = true;

		try {
			// Create a session for the duration of this thread

			// Main loop
			while (keepAlive()) {
				ServiceTask task = this.getNextTask();
				while (task != null) {
					try {
						task.run(getSession());
						// Yield to other threads
						Thread.yield();
					} catch (Throwable t) {
						t.printStackTrace();
					} finally {
						// reset the threadMonitor in preparation for the next tasklet
						threadMonitor.reset();
					}
					task = this.getNextTask();
				}

				localSession.clear();
				if (keepAlive()) {
					try {
						synchronized (sleepMonitor) {
							sleepMonitor.wait(15000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
				}
			}

			// Clean up the scheduled task
			if (scheduledTasks != null) {
				for (IServerTaskRunnable taskRunnable : scheduledTasks) {
					try {
						taskRunnable.dispose();
					} catch (Throwable t) {
						taskRunnable.logException(t);
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			localSession.clear();

			NotesThread.stermThread();

			// bRunning = false;
		}
	}

	/**
	 * @return
	 */
	private ServiceTask getNextTask() {
		ServiceTask result = null;
		int size = 0;
		synchronized (taskQueue_) {
			if (!taskQueue_.isEmpty() && keepAlive()) {
				result = taskQueue_.poll();
				// size = taskQueue_.size();
				serviceTaskLastPickupTime = new Date().getTime();
			}
		}
		// logMessage("Remaining tasks in " + getServiceName() + " queue: " + size);
		return result;
	}

	/**
	 * @param runWhen
	 * @param serverTaskInfo
	 * @param args
	 */
	@Override
	public void postTask(final RunWhen runWhen, final ServerTaskInfo serverTaskInfo, final String[] args) {
		ServiceTask newServiceTask = new ServiceTask(serverTaskInfo, runWhen, args);
		synchronized (taskQueue_) {
			taskQueue_.add(newServiceTask);
		}
		wakeUp();
	}

	/**
	 * @param newServiceTask
	 * @return
	 */
	private boolean isTaskInProgress(ServiceTask newServiceTask) {
		boolean result = false;
		synchronized (taskQueue_) {
			if (taskQueue_.contains(newServiceTask)) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * @param currentTask
	 * @param now
	 * @return
	 */
	private boolean isMaybeHung(ServiceTask currentTask, long now) {
		if (serviceTaskLastPickupTime == 0) {
			// We don't really know since no task has been picked up yet
			return false;
		}
		long timeout = (currentTask == null || currentTask.getTimeOut() == 0) ? getDefaultTimeout() : currentTask.getTimeOut();
		return (now - serviceTaskLastPickupTime) >= timeout;
	}

	/**
	 * @return
	 */
	private long getDefaultTimeout() {
		// 5 mns seems like a long time for the task to complete
		return (5 * 60 * 1000L);
	}

	/**
	 * 
	 */
	@Override
	public void dispose() {
		// if ( !bRunning ){
		// //already stopped, nothing to do
		// return;
		// }
		System.out.println("Disposing of thread " + getName());
		localSession.clear();
		shouldStop = true;

		// Cancel the current task if any
		ServerTaskProgressMonitor monitor = getCurrentProgressMonitor();
		String taskId = monitor == null ? "None" : monitor.getTaskId(); // $NON-NLS-1$
		if (monitor != null) {
			logMessage(MessageFormat.format("Canceling task {0}", monitor.getTaskId()));
			monitor.setCanceled(true);
		}

		wakeUp();

		long curTime = new Date().getTime();
		boolean bThreadInterrupted = false;

		// while (bRunning) {
		// try {
		// Thread.sleep(1000L);
		// } catch (InterruptedException e) {
		// }
		//
		// long elapsedTime = new Date().getTime() - curTime;
		// if (elapsedTime > 30 * 1000L) {
		// threadMonitor.takeSample();
		// if (monitor == null || elapsedTime > 2 * 60 * 1000L) {
		// if (!bThreadInterrupted) {
		// logMessage(MessageFormat.format(
		// "Task {0} seems to not respond anymore (stack trace to follow). DOTS will force quit.", taskId));
		// if (ServerTaskManager.getInstance() != null) {
		// ServerTaskManager.getInstance().dumpStack(this);
		// }
		// interrupt();
		// bThreadInterrupted = true;
		// curTime = new Date().getTime(); // Reset the time
		// } else {
		// logMessage("The thread is not responding to interrupt. Quitting now, please restart the Domino Server.");
		// break;
		// }
		// } else {
		// logMessage(MessageFormat.format("Waiting for task {0} to complete", taskId));
		// }
		// }
		// }
	}

	/**
	 * @param message
	 */
	private void logMessage(String message) {
		if (ServerTaskManager.getInstance() != null) {
			ServerTaskManager.getInstance().logMessageText(message);
		} else {
			System.out.println(message);
		}

	}

	/**
	 * @return
	 */
	private ServerTaskProgressMonitor getCurrentProgressMonitor() {
		return null;
		// return currentTask == null ? null : currentTask.progressMonitor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.task.ServerTaskManager.ITaskService#cancelTask(java.lang.String)
	 */
	@Override
	public int cancelTask(String taskid) {
		ServerTaskProgressMonitor monitor = getCurrentProgressMonitor();
		if (monitor == null) {
			return 0;
		}
		monitor.setCanceled(true);
		return 1;
	}

	/**
	 * @param prefix
	 * @param ci
	 */
	@Override
	public void displayStatus(String prefix, CommandInterpreter ci) {
		ServerTaskProgressMonitor monitor = getCurrentProgressMonitor();
		if (monitor == null) {
			ci.print(prefix + "No task running");
		} else {
			ci.print(prefix + monitor.getStatus());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.task.ServerTaskManager.ITaskService#dumpStack(com.ibm.dots.task.ServerConsole)
	 */
	@Override
	public void dumpStack(ServerConsole serverConsole) {
		synchronized (threadMonitor) {
			if (threadMonitor.isLooping()) {
				serverConsole.printAndLog(MessageFormat.format(
						"Thread {0} appears to be stuck in an infinite loop in the following stackframe:", getName()));
			} else {
				serverConsole.printAndLog(MessageFormat.format("Thread {0} appears to be deadlocked:", getName()));
			}

			serverConsole.printAndLog("\n\n"); // $NON-NLS-1$
			serverConsole.printAndLog(threadMonitor.getStackTrace());
			serverConsole.printAndLog("\n\n"); // $NON-NLS-1$
		}
	}
}
