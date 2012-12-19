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

import static com.ibm.dots.internal.logging.LoggingGroups.getServerTaskManagerLogger;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.NotesThread;
import lotus.domino.Session;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.dynamichelpers.ExtensionTracker;
import org.eclipse.core.runtime.dynamichelpers.IExtensionChangeHandler;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.osgi.framework.console.CommandInterpreter;

import com.ibm.dots.Activator;
import com.ibm.dots.annotation.RunOnStart;
import com.ibm.dots.event.ExtensionManagerBridge;
import com.ibm.dots.event.IExtensionManagerEvent;
import com.ibm.dots.internal.OSGIConsoleAdaptor;
import com.ibm.dots.internal.OSGiServerConsoleAdaptor;
import com.ibm.dots.task.RunWhen.RunUnit;
import com.ibm.dots.utils.FileLog;
import com.ibm.dots.utils.StringUtils;
import com.ibm.dots.utils.TimeUtils;

/**
 * @author dtaieb Manager class that run all the tasks declared as extension points
 */
public enum ServerTaskManager implements TaskletService {
	INSTANCE;

	private static final String THREAD_SCHEDULED = "Scheduled Runs"; // $NON-NLS-1$
	private static final String TRIGGERED_THREAD = "Triggered Runs"; // $NON-NLS-1$
	private static final String MANUAL_THREAD = "Manual Runs"; // $NON-NLS-1$

	private IExtensionRegistry registry;
	private ExtensionTracker extensionTracker;
	private static final String EXTENSIONPOINT_ID = Activator.PLUGIN_ID + ".task"; // $NON-NLS-1$
	private static final String ALIAS_EXTENSIONPOINT_ID = Activator.PLUGIN_ID + ".alias"; // $NON-NLS-1$
	private static final String MULTI_TASK_EXTENSIONPOINT_ID = Activator.PLUGIN_ID + ".multitask"; // $NON-NLS-1$

	private Map<IConfigurationElement, Object> registered = new HashMap<IConfigurationElement, Object>();

	private IExtensionChangeHandler extensionChangeHandler = new IExtensionChangeHandler() {

		@Override
		public void removeExtension(IExtension extension, Object[] objects) {
			IConfigurationElement[] elements = extension.getConfigurationElements();
			for (int i = 0; i < elements.length; i++) {
				elementRemoved(elements[i]);
			}
		}

		@Override
		public void addExtension(IExtensionTracker tracker, IExtension extension) {
			IConfigurationElement[] elements = extension.getConfigurationElements();
			for (int i = 0; i < elements.length; i++) {
				elementAdded(elements[i]);
			}
		}
	};

	protected static interface ITaskService {

		String getServiceName();

		void dispose();

		void postTask(RunWhen runWhen, ServerTaskInfo serverTaskInfo, String[] args);

		void displayStatus(String prefix, CommandInterpreter ci);

		int cancelTask(String taskId);

		void dumpStack(ServerConsole serverConsole);
	}

	// HashMap of scheduled runs
	private final Map<RunWhen, List<ServerTaskInfo>> scheduledRuns = Collections
			.synchronizedMap(new HashMap<RunWhen, List<ServerTaskInfo>>());

	// HashMap of standalone runs, keyed by id
	private final Map<String, Object> standaloneRuns = Collections.synchronizedMap(new HashMap<String, Object>());

	// HashMap of aliases, keyed by id
	private final Map<String, AliasTaskInfo> aliasRuns = Collections.synchronizedMap(new HashMap<String, AliasTaskInfo>());

	// private Set<ServerTaskInfo> eventTriggerableTasks = null;
	private final Map<Integer, Set<ServerTaskInfo>> triggerTaskMap = Collections
			.synchronizedMap(new HashMap<Integer, Set<ServerTaskInfo>>());

	// private static ServerTaskManager singleton;
	private boolean bPerformOnStartRun = true;
	private ServerConsole serverConsole;
	private OSGIConsoleAdaptor osgiServerConsole;

	// Scheduled, triggered and manual runs run on their own task
	private ThreadGroup threadGroup = new ThreadGroup("Tasklet container"); // $NON-NLS-1$
	private ITaskService scheduledThread;
	private ITaskService triggeredThread;
	private ITaskService manualThread;
	private String mqName;

	/**
	 * @param registry
	 */
	// private ServerTaskManager(IExtensionRegistry registry) {
	//
	// // Only one instance permitted
	// if (singleton != null) {
	// throw new IllegalStateException("Singleton already set"); // $NON-NLS-1$
	// }
	// singleton = this;
	//
	// System.out.println("Created new ServerTaskManager!");
	//
	// this.registry = registry;
	// extensionTracker = new ExtensionTracker(registry);
	//
	// // Init server Console
	// serverConsole = new ServerConsole();
	// osgiServerConsole = new OSGiServerConsoleAdaptor(serverConsole);
	//
	// // Register the tasklet Service
	// Activator.getDefault().getBundle().getBundleContext().registerService(TaskletService.class.getName(), this, null);
	// }

	private ServerTaskManager() {
		System.out.println("Created new ServerTaskManager!");
	}

	private void setRegistry(IExtensionRegistry registry) {
		this.registry = registry;
		extensionTracker = new ExtensionTracker(registry);

		// Init server Console
		serverConsole = new ServerConsole();
		osgiServerConsole = new OSGiServerConsoleAdaptor(serverConsole);

		// Register the tasklet Service
		Activator.getDefault().getBundle().getBundleContext().registerService(TaskletService.class.getName(), this, null);
	}

	/**
	 * @return
	 */
	private ITaskService getManualThread() {
		if (manualThread == null) {
			manualThread = new JobTaskService(MANUAL_THREAD);
		}
		return manualThread;
	}

	/**
	 * @return
	 */
	private ITaskService getTriggeredThread() {
		if (triggeredThread == null) {
			triggeredThread = new ServiceThread(threadGroup, TRIGGERED_THREAD);
		}
		return triggeredThread;
	}

	/**
	 * @return
	 */
	private ITaskService getScheduledThread() {
		if (scheduledThread == null) {
			scheduledThread = new ServiceThread(threadGroup, THREAD_SCHEDULED);
		}
		return scheduledThread;
	}

	/**
	 * @return
	 */
	public AliasTaskInfo[] getAliases() {
		return aliasRuns.values().toArray(new AliasTaskInfo[0]);
	}

	/**
	 * Start listening to changes in the registry
	 */
	public void start() {
		trackExtensionPoint(EXTENSIONPOINT_ID);
		trackExtensionPoint(ALIAS_EXTENSIONPOINT_ID);
		trackExtensionPoint(MULTI_TASK_EXTENSIONPOINT_ID);
	}

	/**
	 * @param extensionpointId
	 */
	private void trackExtensionPoint(String extensionpointId) {
		IExtensionPoint extPt = registry.getExtensionPoint(extensionpointId);
		if (extPt == null) {
			serverConsole.logMessage(MessageFormat.format("Unable to find extension point id : {0}", extensionpointId)); // $NON-NLS-1$
			return;
		}
		extensionTracker.registerHandler(extensionChangeHandler, ExtensionTracker.createExtensionPointFilter(extPt));

		// bootstrap
		IExtension[] extensions = extPt.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			extensionChangeHandler.addExtension(extensionTracker, extensions[i]);
		}
	}

	/**
	 * 
	 */
	public void stop() {
		extensionTracker.close();
	}

	/**
	 * @param element
	 */
	private void elementRemoved(IConfigurationElement element) {
		if (registered.containsKey(element)) {
			registered.remove(element);
		}
	}

	/**
	 * @param element
	 */
	private void elementAdded(IConfigurationElement element) {
		Object extensionInfo = createExtensionInfo(element);
		if (extensionInfo != null) {
			registered.put(element, extensionInfo);
		}
	}

	/**
	 * 
	 */
	public void dispose() {
		boolean bDisplayExitTrace = false;
		String dataDir = null;
		Session session = null;
		try {
			NotesThread.sinitThread();
			session = NotesFactory.createSession();
			String sDumpStack = session.getEnvironmentString("DOTS_DISPLAY_EXIT_TRACE", true); // $NON-NLS-1$
			if (sDumpStack != null && sDumpStack.length() > 0) {
				bDisplayExitTrace = Boolean.parseBoolean(sDumpStack);
			}
			dataDir = session.getEnvironmentString("Directory", true); // $NON-NLS-1$
		} catch (Throwable e) {
		} finally {
			try {
				if (session != null) {
					session.recycle();
				}
			} catch (NotesException e) {
			}
			NotesThread.stermThread();
		}

		// Post a quit message on all threads
		if (scheduledThread != null) {
			scheduledThread.dispose();
		}

		if (manualThread != null) {
			manualThread.dispose();
		}

		if (triggeredThread != null) {
			triggeredThread.dispose();
		}

		if (bDisplayExitTrace) {
			displayExitTrace(dataDir);
		}
	}

	/**
	 * Called by the Dots process to display exit traces based on ini variable
	 */
	public void displayExitTrace(String dataDir) {
		FileLog fileLog = new FileLog(new File(getIBMTechSupportDir(dataDir), "dots_exit.log")); // $NON-NLS-1$
		fileLog.logMessage("*******************************************");
		fileLog.logMessage("Starting Domino OSGi Tasklet container exit trace"); // $NON-NLS-1$
		// User wants all threads
		Map<Thread, StackTraceElement[]> traceMap = Thread.getAllStackTraces();
		for (Thread thread : traceMap.keySet()) {
			if (thread != null) {
				String threadName = thread.getName();
				fileLog.logMessage("StackTrace for thread " + threadName); // $NON-NLS-1$
				StackTraceElement[] traces = thread.getStackTrace();
				for (StackTraceElement trace : traces) {
					fileLog.logMessage("\t" + trace.toString()); // $NON-NLS-1$
				}
				fileLog.logMessage("\n\n"); // $NON-NLS-1$
			}
		}
		fileLog.logMessage("*******************************************");
	}

	/**
	 * @param dataDir
	 * @return
	 */
	private File getIBMTechSupportDir(String dataDir) {
		File f = new File(dataDir, "IBM_TECHNICAL_SUPPORT"); // $NON-NLS-1$
		if (!f.exists() || !f.isDirectory()) {
			if (!f.mkdirs()) {
				throw new IllegalStateException("Unable to access IBM technical support directory"); // $NON-NLS-1$
			}
		}
		return f;
	}

	/**
	 * @return
	 */
	public static ServerTaskManager getInstance() {
		return INSTANCE;
		// return singleton;
	}

	/**
	 * @param numSeconds
	 * @return
	 */
	public native boolean addInSecondsHaveElapsed(int numSeconds);

	/**
	 * @param numDays
	 * @return
	 */
	public native boolean addInDayHasElapsed();

	/**
	 * @param message
	 */
	private native void addInLogMessageText(String message);

	/**
	 * @param message
	 */
	public void logMessageText(String message) {
		if (mqName == null) {
			mqName = "[" + AccessController.doPrivileged(new PrivilegedAction<String>() {
				@Override
				public String run() {
					return System.getProperty("dots.mq.name"); // $NON-NLS-1$
				}
			}) + "] ";
		}
		addInLogMessageText(mqName + message);
	}

	/**
	 * @param numMinutes
	 * @return
	 */
	public boolean addInMinutesHaveElapsed(int numMinutes) {
		return addInSecondsHaveElapsed(numMinutes * 60);
	}

	/**
	 * Main dispatch routine
	 */
	public void addinDispatch() {

		// Perform the run on start if needed
		queueRunOnStartRuns();

		for (RunWhen runWhen : scheduledRuns.keySet()) {
			if (runWhen.getStartAtTime() > 0) {
				Date now = new Date();
				// RunWhen with a start At time have their own way of being scheduled
				// Check if we should run it now
				if (runWhen.getStartAtTime() <= now.getTime()) {
					runWhen.updateToNextStartAtTime();
					queueRun(getScheduledThread(), runWhen, scheduledRuns.get(runWhen));
				}
			} else {
				if (runWhen.getUnit() == RunWhen.RunUnit.second) {
					if (addInSecondsHaveElapsed(runWhen.getEvery())) {
						queueRun(getScheduledThread(), runWhen, scheduledRuns.get(runWhen));
					}
				} else if (runWhen.getUnit() == RunWhen.RunUnit.minute) {
					if (addInMinutesHaveElapsed(runWhen.getEvery())) {
						queueRun(getScheduledThread(), runWhen, scheduledRuns.get(runWhen));
					}
				} else if (runWhen.getUnit() == RunWhen.RunUnit.day) {
					if (addInDayHasElapsed()) {
						queueRun(getScheduledThread(), runWhen, scheduledRuns.get(runWhen));
					}
				}
			}
		}
	}

	/**
	 * 
	 */
	private void queueRunOnStartRuns() {
		if (bPerformOnStartRun) {
			// Do the initial run for task that are not scheduled
			bPerformOnStartRun = false;
			for (Entry<String, Object> entry : standaloneRuns.entrySet()) {
				if (entry.getValue() instanceof IConfigurationElement) {
					IConfigurationElement element = (IConfigurationElement) entry.getValue();
					ServerTaskInfo serverTask = null;
					try {
						serverTask = new ServerTaskInfo(element);
						if (serverTask.isRunOnStart()) {
							queueRun(getManualThread(), RunWhen.RunOnStart, serverTask, new String[0]);
						} else {
							// Check if RunOnStart annotation is set at the class level
							RunOnStart ros = serverTask.getServerTaskRunnable().getClass().getAnnotation(RunOnStart.class);
							if (ros != null) {
								queueRun(getManualThread(), RunWhen.RunOnStart, serverTask, new String[0]);
							}
						}

						// Check for method annotations
						Class<?> cl = serverTask.getServerTaskRunnable().getClass();
						Method[] methods = cl.getDeclaredMethods();
						for (Method m : methods) {
							Class<?>[] paramTypes = m.getParameterTypes();
							if (Arrays.equals(paramTypes, new Class<?>[] { IProgressMonitor.class })
									|| Arrays.equals(paramTypes, new Class<?>[] { String[].class, IProgressMonitor.class })) {
								Annotation annotation = m.getAnnotation(RunOnStart.class);
								if (annotation instanceof RunOnStart) {
									RunWhen r = new RunWhen(RunUnit.onStart, 0, 0, 0);
									r.annotatedMethod = m;
									queueRun(getManualThread(), r, serverTask, new String[0]);
								}
							}
						}
					} catch (Throwable t) {
						serverConsole.logException(t);
					}
				}
			}
		}
	}

	/**
	 * @param taskService
	 * @param runWhen
	 * @param serverTaskInfos
	 */
	private void queueRun(ITaskService taskService, RunWhen runWhen, List<ServerTaskInfo> serverTaskInfos) {
		if (serverTaskInfos == null) {
			return;
		}

		HashSet<ServerTaskInfo> removed = null;
		synchronized (serverTaskInfos) {
			for (ServerTaskInfo task : serverTaskInfos) {
				try {
					queueRun(taskService, runWhen, task, new String[0]);
				} catch (Throwable t) {
					// Remove the task from the list
					if (removed == null) {
						removed = new HashSet<ServerTaskInfo>();
					}
					removed.add(task);
					serverConsole.logException(t);

					// We should not stop all the entire run, just remove this bad one from the scheduled list
					// throw new DotsException( t );
				}
			}

			if (removed != null) {
				for (ServerTaskInfo task : removed) {
					serverTaskInfos.remove(task);
				}
			}
		}
	}

	private boolean isRegistrySet() {
		return registry != null;
	}

	/**
	 * @param registry
	 * @return
	 */
	public static ServerTaskManager createInstance(IExtensionRegistry registry) {
		ServerTaskManager result = ServerTaskManager.INSTANCE;
		if (!result.isRegistrySet()) {
			result.setRegistry(registry);
		}
		return result;
	}

	public Set<ServerTaskInfo> getTriggerableTasks(int eventId) {
		Set<ServerTaskInfo> result = null;
		synchronized (triggerTaskMap) {
			if (triggerTaskMap.containsKey(eventId)) {
				result = triggerTaskMap.get(eventId);
			}
		}
		return result;
	}

	public boolean registerTriggerableTasks(int eventId, ServerTaskInfo task) {
		System.out.println("Registering tasklet for event " + eventId + ": " + task.getDescription());
		boolean result = false;
		Set<ServerTaskInfo> set = null;
		synchronized (triggerTaskMap) {
			if (!triggerTaskMap.containsKey(eventId)) {
				set = Collections.synchronizedSet(new HashSet<ServerTaskInfo>());
				triggerTaskMap.put(eventId, set);
				registerEventQueue(eventId);
			} else {
				set = triggerTaskMap.get(eventId);
			}
		}
		if (set != null) {
			synchronized (set) {
				set.add(task);
			}
			result = true;
		}
		return result;
	}

	/**
	 * @param element
	 * @return
	 */
	private Object createExtensionInfo(IConfigurationElement element) {
		if ("task".equals(element.getName()) || "multiTask".equals(element.getName())) { // $NON-NLS-1$ $NON-NLS-2$
			try {
				ServerTaskInfo serverTask = new ServerTaskInfo(element);
				if (!serverTask.canLoad()) {
					serverConsole.logPlatform(Activator.PLUGIN_ID,
							MessageFormat.format("Tasklet {0} will not be loaded", serverTask.getId())); // $NON-NLS-1$
					return null;
				}

				// Log information to the platform log
				serverConsole.logPlatform(Activator.PLUGIN_ID, "Found Tasklet " + serverTask.getId()); // $NON-NLS-1$

				// Add to standalone list
				standaloneRuns.put(serverTask.getId(), element);

				// Add method annotated with @run
				ServerTaskInfo[] annotatedManualRuns = serverTask.getRunAnnotatedMethods();
				for (ServerTaskInfo annotatedManualRun : annotatedManualRuns) {
					standaloneRuns.put(annotatedManualRun.getId(), annotatedManualRun);
				}

				// Check if the task can be triggered by EM Event
				if (serverTask.hasTriggerableAnnotatedMethods()) {
					System.out.println("Task has triggerable methods " + serverTask.getDescription());
					int[] events = serverTask.getTriggeredEventIds();
					if (events.length > 0) {
						for (int event : events) {
							registerTriggerableTasks(event, serverTask);
						}
					}
				}

				// Add to scheduled list
				RunWhen[] runWhens = serverTask.getRuns();
				for (RunWhen r : runWhens) {
					List<ServerTaskInfo> tasks = scheduledRuns.get(r);
					if (tasks == null) {
						tasks = new ArrayList<ServerTaskInfo>();
						scheduledRuns.put(r, tasks);
					}
					tasks.add(serverTask);
				}
				return serverTask;

			} catch (CoreException e) {
				serverConsole.logException(e);
			}
		} else if ("alias".equals(element.getName())) { // $NON-NLS-1$
			try {
				AliasTaskInfo alias = new AliasTaskInfo(element);
				// Put the alias in the aliasRuns
				aliasRuns.put(alias.getId(), alias);

				return alias;

			} catch (CoreException e) {
				serverConsole.logException(e);
			}
		}
		return null;
	}

	/**
	 * @param ci
	 * @param bInvalidCommand
	 */
	private void displayServerTaskHelp(CommandInterpreter ci, boolean bInvalidCommand) {
		if (bInvalidCommand) {
			System.out.println("\n\tInvalid command. See help below"); // $NON-NLS-1$
		}
		OSGIConsoleAdaptor.displayHelp(ci);
	}

	// private final Queue<IExtensionManagerEvent> eventQueue_ = new ConcurrentLinkedQueue<IExtensionManagerEvent>();
	private final Map<Integer, Queue<IExtensionManagerEvent>> eventQueueMap_ = Collections
			.synchronizedMap(new HashMap<Integer, Queue<IExtensionManagerEvent>>());

	public Queue<IExtensionManagerEvent> getEventQueue(int eventId) {
		Queue<IExtensionManagerEvent> result = null;
		synchronized (eventQueueMap_) {
			if (eventQueueMap_.containsKey(eventId)) {
				result = eventQueueMap_.get(eventId);
			}
		}
		return result;
	}

	public void registerEventQueue(int eventId) {
		synchronized (eventQueueMap_) {
			if (!eventQueueMap_.containsKey(eventId)) {
				System.out.println("Creating new event queue for eventid " + eventId);
				Queue<IExtensionManagerEvent> queue = new ConcurrentLinkedQueue<IExtensionManagerEvent>();
				eventQueueMap_.put(eventId, queue);
			}
		}
	}

	public IExtensionManagerEvent getQueuedEvent(int eventId) {
		IExtensionManagerEvent result = null;
		Queue<IExtensionManagerEvent> queue = getEventQueue(eventId);
		if (queue != null) {
			synchronized (queue) {
				if (!queue.isEmpty()) {
					result = queue.poll();
				}
			}
		}
		if (result != null) {
			// System.out.println("Returning a queued event: " + eventId);
		}
		return result;
	}

	public boolean queueEvent(IExtensionManagerEvent event) {
		boolean result = false;
		int id = event.getEventId();
		Queue<IExtensionManagerEvent> queue = getEventQueue(id);
		if (queue != null) {
			synchronized (queue) {
				queue.add(event);
				result = true;
			}
			// System.out.println("Queued an event: " + id);
		} else {
			// System.out.println("No queue for type " + id);
		}
		return result;
	}

	private void scheduleNextTrigger(ServerTaskInfo info, IExtensionManagerEvent event) {
		RunWhen rw = new RunWhen(event);
		Method[] annotatedMethods = info.getTriggeredAnnotatedMethods(event);
		for (Method annotatedMethod : annotatedMethods) {
			rw.annotatedMethod = annotatedMethod;
			queueRun(getTriggeredThread(), rw, info, new String[0]);
			// System.out.println("queued a run for " + annotatedMethod.getName() + " for an event of type " + event.getEventId());
		}
	}

	private void dispatchEvent(final int eventId) {
		final Set<ServerTaskInfo> tasks = getTriggerableTasks(eventId);

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (tasks) {
					IExtensionManagerEvent curEvent = getQueuedEvent(eventId);
					if (curEvent != null) {
						for (ServerTaskInfo info : tasks) {
							scheduleNextTrigger(info, curEvent);
						}
					} else {
						System.out.println("Attempted to dispatch an eventid of " + eventId + " but no event was found in the queue!");
					}
				}

			}
		});
		if (tasks != null) {
			t.start();
		} else {
			// System.out.println("No triggerable tasks for event " + eventId);
		}
	}

	/**
	 * @param commandBuffer
	 *            Called from the C side to process server adding commands
	 */
	public void processCommand(final String commandBuffer) {
		// Check if it's an extension manager event command
		IExtensionManagerEvent emEvent = ExtensionManagerBridge.getEventFromCommand(commandBuffer);
		if (emEvent != null) {
			if (queueEvent(emEvent)) {
				dispatchEvent(emEvent.getEventId());
			}
			return;
		}
		// Parse the commandBuffer
		StringTokenizer st = new StringTokenizer(commandBuffer, " ");
		ArrayList<String> args = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			String arg = st.nextToken().trim();
			if (!filterArg(arg)) {
				args.add(arg);
			}
		}

		if (commandBuffer != null && args.isEmpty()) {
			// Must be from load command, don't process further
			return;
		}
		_processCommand(args.toArray(new String[0]));
	}

	/**
	 * @param arg
	 * @return: true if the argument should be filtered from the command
	 */
	private boolean filterArg(String arg) {
		if (arg == null || arg.length() == 0) {
			return true;
		}

		arg = arg.toLowerCase();
		if (arg.startsWith("-debug") || arg.equalsIgnoreCase("-optionsFile")) { // $NON-NLS-1$ $NON-NLS-2$
			return true;
		}
		return false;
	}

	/**
	 * @param args
	 */
	private void _processCommand(String[] args) {
		if (args.length == 0) {
			displayServerTaskHelp(osgiServerConsole, true);
			return;
		}

		if ("dumpstack".equalsIgnoreCase(args[0])) { // $NON-NLS-1$
			Map<String, String> argMap = getOptionMap(args);
			if (argMap.containsKey("a")) {
				dumpAllStacks();
			} else {
				// Only the run thread
				dumpStack(scheduledThread);
				dumpStack(triggeredThread);
				dumpStack(manualThread);
			}
		} else {
			if (!osgiServerConsole.runCommand(args)) {
				// Display the help
				StringBuilder sb = new StringBuilder();
				for (String arg : args) {
					sb.append(" " + arg);
				}
				serverConsole.logMessage("Command used: " + sb.toString()); // $NON-NLS-1$
				displayServerTaskHelp(osgiServerConsole, true);
			}
		}
	}

	/**
	 * 
	 */
	private void dumpAllStacks() {
		// User wants all threads
		Map<Thread, StackTraceElement[]> traceMap = Thread.getAllStackTraces();
		for (Thread t : traceMap.keySet()) {
			String threadName = t.getName();
			serverConsole.printAndLog("StackTrace for thread " + threadName); // $NON-NLS-1$
			StackTraceElement[] traces = t.getStackTrace();
			for (StackTraceElement trace : traces) {
				serverConsole.printAndLog("\t" + trace.toString()); // $NON-NLS-1$
			}
			serverConsole.printAndLog("\n\n"); // $NON-NLS-1$
		}
	}

	/**
	 * @param args
	 * @return
	 */
	private Map<String, String> getOptionMap(String[] args) {
		HashMap<String, String> arguments = new HashMap<String, String>();

		for (String arg : args) {
			if (arg.startsWith("-")) {
				StringTokenizer st = new StringTokenizer(arg.substring(1), "=");
				String key = st.nextToken();
				String value = "";
				if (st.hasMoreTokens()) {
					value = st.nextToken();
				}
				arguments.put(key, value);
			}
		}

		return arguments;
	}

	/**
	 * @param serviceThread
	 */
	private int cancelTask(ITaskService taskService) {
		if (taskService == null) {
			return 0;
		}
		return taskService.cancelTask(null);
	}

	/**
	 * @param ci
	 * @param taskService
	 * @param threadName
	 */
	private void displayStatus(CommandInterpreter ci, ITaskService taskService, String taskServiceName) {
		ci.print(MessageFormat.format("{0}", taskServiceName));
		String prefix = "--- ";
		if (taskService == null) {
			ci.print(prefix + "No task running"); // $NON-NLS-1$
		} else {
			taskService.displayStatus(prefix, ci);
		}
	}

	/**
	 * @param taskService
	 */
	void dumpStack(ITaskService taskService) {
		if (taskService != null) {
			taskService.dumpStack(serverConsole);
		}
	}

	/**
	 * @param ci
	 * @param taskId
	 */
	public void displayTaskInfo(CommandInterpreter ci, String taskId) {
		Object element = standaloneRuns.get(taskId);
		if (element == null) {
			ci.println(MessageFormat.format("Invalid taskid : {0}", taskId)); // $NON-NLS-1$
			return;
		}

		try {
			ServerTaskInfo serverTaskInfo = null;
			if (element instanceof IConfigurationElement) {
				serverTaskInfo = new ServerTaskInfo((IConfigurationElement) element);
			} else if (element instanceof ServerTaskInfo) {
				serverTaskInfo = (ServerTaskInfo) element;
			}

			if (serverTaskInfo != null) {
				displayTaskInfo(ci, serverTaskInfo, true);
			} else {
				ci.println(MessageFormat.format("Invalid internal value for taskid : {0}", taskId)); // $NON-NLS-1$
			}
		} catch (CoreException e) {
			serverConsole.logException(e);
		}
	}

	/**
	 * @param ci
	 * @param taskId
	 * @param args
	 */
	public void runTask(CommandInterpreter ci, String taskId, String[] args) {
		try {
			String[][] pargs = new String[][] { args };
			ServerTaskInfo taskInfo = getServerTaskInfo(ci, taskId, pargs);
			if (taskInfo == null) {
				return;
			}
			queueRun(getManualThread(), taskInfo.getAnnotatedRunWhen() == null ? RunWhen.RunOnce : taskInfo.getAnnotatedRunWhen(),
					taskInfo, pargs[0]);
		} catch (Throwable t) {
			ci.printStackTrace(t);
		}
	}

	/**
	 * @param ci
	 * @param taskId
	 * @param pargs
	 * @return
	 * @throws CoreException
	 */
	ServerTaskInfo getServerTaskInfo(CommandInterpreter ci, String taskId, String[][] pargs) throws CoreException {
		if (ci == null) {
			ci = osgiServerConsole;
		}
		Object element = standaloneRuns.get(taskId);
		if (element instanceof ServerTaskInfo) {
			return (ServerTaskInfo) element;
		}

		if (element == null) {
			// Look for aliases
			AliasTaskInfo alias = aliasRuns.get(taskId);
			if (alias != null) {
				// Replace the element and arguments with the alias info
				element = standaloneRuns.get(alias.getTargetTaskId());
				if (pargs.length > 0) {
					pargs[0] = alias.getArguments(pargs[0]);
				}
			}
		}

		if (element == null || !(element instanceof IConfigurationElement)) {
			ci.print(MessageFormat.format("Invalid taskid : {0}", taskId)); // $NON-NLS-1$
			return null;
		}
		return new ServerTaskInfo((IConfigurationElement) element);
	}

	/**
	 * @param taskService
	 * @param runWhen
	 * @param serverTaskInfo
	 * @param args
	 */
	private void queueRun(ITaskService taskService, final RunWhen runWhen, final ServerTaskInfo serverTaskInfo, final String[] args) {
		if (getServerTaskManagerLogger().isLoggable(Level.FINE)) {
			getServerTaskManagerLogger().log(Level.FINE,
					MessageFormat.format("Task run {0} posted to taskService {1} (runWhen = {2})", serverTaskInfo, taskService, runWhen)); // $NON-NLS-1$
		}
		taskService.postTask(runWhen, serverTaskInfo, args);
	}

	/**
	 * 
	 */
	public void displayTaskList(CommandInterpreter ci) {
		try {
			String filter = ci.nextArgument();
			if (filter != null) {
				// conform to regular expression
				filter = filter.replaceAll("\\*", ".*");
			}
			boolean bFoundOne = false;
			for (String taskId : standaloneRuns.keySet()) {
				if (taskId != null && match(taskId, filter)) {
					displayTaskInfo(ci, taskId);
					bFoundOne = true;
				}
			}
			if (!bFoundOne) {
				ci.println("No task found"); // $NON-NLS-1$
			}
		} catch (Throwable e) {
			serverConsole.logException(e);
		}
	}

	/**
	 * @param id
	 * @param filter
	 * @return: true if id matches the filter
	 */
	private boolean match(String id, String filter) {
		if (filter == null) {
			return true;
		}
		return id.matches(filter);
	}

	/**
	 * @param serverTask
	 * @param bDetails
	 */
	private void displayTaskInfo(CommandInterpreter ci, ServerTaskInfo serverTask, boolean bDetails) {
		ci.println();
		ci.print("\tid:\t" + serverTask.getId()); // $NON-NLS-1$
		ci.print("\tdescription:\t" + serverTask.getDescription()); // $NON-NLS-1$
		// ci.print("\tclass Name:\t" + serverTask.getClassName() );
		if (bDetails) {
			ci.print("\tRun on Start:\t" + serverTask.isRunOnStart()); // $NON-NLS-1$
			if (serverTask.isScheduled()) {
				RunWhen[] runWhens = serverTask.getRuns();
				for (RunWhen runWhen : runWhens) {
					ci.print("\t" + runWhen); // $NON-NLS-1$
				}
			} else {
				ci.print("\tNot scheduled");
			}
		}
	}

	/**
	 * @param ci
	 */
	public void displayStatus(CommandInterpreter ci) {
		displayStatus(ci, scheduledThread, THREAD_SCHEDULED);
		displayStatus(ci, triggeredThread, TRIGGERED_THREAD);
		displayStatus(ci, manualThread, MANUAL_THREAD);
	}

	/**
	 * @param ci
	 */
	public void cancelAllTasks(CommandInterpreter ci) {
		int iNumCancelled = 0;
		iNumCancelled += cancelTask(scheduledThread);
		iNumCancelled += cancelTask(triggeredThread);
		iNumCancelled += cancelTask(manualThread);
		if (iNumCancelled == 0) {
			ci.print("No task to cancel");
		} else {
			ci.print(MessageFormat.format("{0} task(s) cancelled", iNumCancelled));
		}

	}

	/**
	 * @param taskId
	 * @param ci
	 */
	public void cancelTask(String taskId, CommandInterpreter ci) {
		if (manualThread == null || (manualThread.cancelTask(taskId) == 0)) {
			ci.println(MessageFormat.format("Unable to find task {0}. The task may already be done", taskId));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.task.TaskletService#registerTasklet(java.lang.String, com.ibm.dots.task.AbstractServerTask)
	 */
	@Override
	public void registerTasklet(String taskletId, AbstractServerTask tasklet) {
		registerTasklet(taskletId, tasklet, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.task.TaskletService#registerTasklet(java.lang.String, com.ibm.dots.task.AbstractServerTask, java.lang.String)
	 */
	@Override
	public void registerTasklet(String taskletId, AbstractServerTask tasklet, String description) {
		registerTasklet(taskletId, tasklet, description, new RunWhen[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.task.TaskletService#registerTasklet(java.lang.String, com.ibm.dots.task.AbstractServerTask, java.lang.String,
	 * com.ibm.dots.task.RunWhen[])
	 */
	@Override
	public void registerTasklet(String taskletId, AbstractServerTask tasklet, String description, RunWhen... runWhens) {
		if (runWhens == null || runWhens.length == 0) {
			// Add the tasklet to the standalone runs
			standaloneRuns.put(taskletId, new ServerTaskInfo(taskletId, description, tasklet));
		} else {
			// use the scheduled runs
			ServerTaskInfo serverTask = new ServerTaskInfo(taskletId, description, tasklet);
			for (RunWhen r : runWhens) {
				List<ServerTaskInfo> tasks = scheduledRuns.get(r);
				if (tasks == null) {
					tasks = new ArrayList<ServerTaskInfo>();
					scheduledRuns.put(r, tasks);
				}
				tasks.add(serverTask);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.task.TaskletService#scheduleTasklet(java.lang.String, com.ibm.dots.task.RunWhen)
	 */
	@Override
	public void scheduleTasklet(String taskletId, RunWhen runWhen) throws CoreException {
		List<ServerTaskInfo> tasks = scheduledRuns.get(runWhen);
		if (tasks != null) {
			// Check if we already have a task with the same tasklet id
			for (ServerTaskInfo taskInfo : tasks) {
				if (taskInfo.getId().equals(taskletId)) {
					// Nothing to do, it's already there
					return;
				}
			}
		}

		// Look in the standalone if we find one
		ServerTaskInfo taskInfo = getServerTaskInfo(null, taskletId, null);
		if (taskInfo != null) {
			// Add it to the ServerTaskInfo
			taskInfo.registerRunWhen(runWhen);
			if (tasks == null) {
				tasks = new ArrayList<ServerTaskInfo>();
				scheduledRuns.put(runWhen, tasks);
			}
			tasks.add(taskInfo);
		}
	}

	@Override
	public void scheduleTasklet(Dictionary<String, String> properties) throws CoreException {
		String taskletId = properties.get("id");
		String every = properties.get("every");
		String unit = properties.get("unit");
		String startAt = properties.get("startAt");
		String stopAt = properties.get("stopAt");

		if (StringUtils.isEmpty(taskletId) || StringUtils.isEmpty(every) || StringUtils.isEmpty(unit)) {
			serverConsole.logPlatform(Activator.PLUGIN_ID,
					MessageFormat.format("scheduleTasklet for {0} failed because of invalid Arguments", taskletId));
			return;
		}

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(MessageFormat.format("Scheduling tasklet {0} : every = {1} - unit = {2}", taskletId, every, unit));
			if (!StringUtils.isEmpty(startAt)) {
				sb.append(MessageFormat.format(" - startAt = {0}", startAt));
			}
			if (!StringUtils.isEmpty(stopAt)) {
				sb.append(MessageFormat.format(" - stopAt = {0}", stopAt));
			}
			serverConsole.logPlatform(Activator.PLUGIN_ID, sb.toString());
			scheduleTasklet(taskletId, new RunWhen(RunUnit.valueOf(unit), Integer.valueOf(every), TimeUtils.getTimeOfDay(startAt),
					TimeUtils.getTimeOfDay(stopAt)));
		} catch (Throwable t) {
			serverConsole.logPlatform(Activator.PLUGIN_ID, "scheduleTasklet error", t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.task.TaskletService#runTasklet(java.lang.String, java.lang.String[])
	 */
	@Override
	public void runTasklet(String taskletId, String... args) {
		runTask(null, taskletId, args);
	}
}
