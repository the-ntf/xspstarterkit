/**
 * 
 */
package com.ibm.dots.tasklet;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.dynamichelpers.ExtensionTracker;
import org.eclipse.core.runtime.dynamichelpers.IExtensionChangeHandler;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;

import com.ibm.dots.Activator;
import com.ibm.dots.internal.OSGIConsoleAdaptor;
import com.ibm.dots.internal.OSGiServerConsoleAdaptor;
import com.ibm.dots.task.AbstractServerTask;
import com.ibm.dots.task.RunWhen;
import com.ibm.dots.task.ServerConsole;
import com.ibm.dots.task.TaskletService;
import com.ibm.dots.tasklet.AbstractTasklet.RunSchedule;
import com.ibm.dots.tasklet.AbstractTasklet.RunStart;
import com.ibm.dots.tasklet.events.DotsEventFactory;
import com.ibm.dots.thread.Guardian;
import com.ibm.dots.thread.Intimidator;

/**
 * @author nfreeman
 * 
 */
public enum TaskletManager implements TaskletService {
	INSTANCE;

	private Guardian scheduleExecutor_;
	private Intimidator triggerExecutor_;
	public static final int DEFAULT_SCHEDULE_THREADS = 2;
	public static final int DEFAULT_TRIGGER_CORE_THREADS = 2;
	public static final int DEFAULT_TRIGGER_MAX_THREADS = 2;
	public static final int DEFAULT_TRIGGER_KEEP_ALIVE_SECONDS = 10;

	private IExtensionRegistry registry;
	private ExtensionTracker extensionTracker;
	private static final String EXTENSIONPOINT_ID = Activator.PLUGIN_ID + ".task"; // $NON-NLS-1$
	private static final String ALIAS_EXTENSIONPOINT_ID = Activator.PLUGIN_ID + ".alias"; // $NON-NLS-1$
	private static final String MULTI_TASK_EXTENSIONPOINT_ID = Activator.PLUGIN_ID + ".multitask"; // $NON-NLS-1$

	private Map<IConfigurationElement, Object> registered = new HashMap<IConfigurationElement, Object>();

	private ServerConsole serverConsole;
	private OSGIConsoleAdaptor osgiServerConsole;
	private String mqName;

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
			try {
				Object extensionInfo = createExtensionInfo(element);
				if (extensionInfo != null) {
					registered.put(element, extensionInfo);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}

		private Object createExtensionInfo(IConfigurationElement element) throws CoreException {
			String name = element.getName();
			// System.out.println("Registering new " + name);

			String id = element.getAttribute("id"); // $NON-NLS-1$

			String description = element.getAttribute("description"); // $NON-NLS-1$
			if ("task".equals(name)) {
				Object exec = element.createExecutableExtension("class");
				if (exec instanceof AbstractTasklet) {
					AbstractTasklet tasklet = (AbstractTasklet) exec;
					// System.out.println("Registering a new Tasklet: " + tasklet.getClass().getName());
					tasklet.setDescription(description);
					tasklet.setId(id);

					Set<RunStart> starts = tasklet.getStarts();
					if (starts != null && !starts.isEmpty()) {
						for (RunStart start : starts) {
							triggerExecutor_.execute(start);
						}
					}

					Set<RunSchedule> schedules = tasklet.getSchedules();
					if (schedules != null && !schedules.isEmpty()) {
						for (RunSchedule sched : schedules) {
							scheduleExecutor_.scheduleAtFixedRate(sched, 0, sched.getEvery(), sched.getUnit());
						}
					}
					Set<Integer> events = tasklet.getAllTriggerEvents();
					for (int event : events) {
						registerTriggerableTasks(event, tasklet);
						System.out.println("Registered tasklet for eventid " + event);
					}
				}
			}

			return null;
		}
	};

	private final Map<Integer, Set<AbstractTasklet>> triggerTaskMap = Collections
			.synchronizedMap(new HashMap<Integer, Set<AbstractTasklet>>());

	public boolean registerTriggerableTasks(int eventId, AbstractTasklet task) {
		boolean result = false;
		Set<AbstractTasklet> set = null;
		synchronized (triggerTaskMap) {
			if (!triggerTaskMap.containsKey(eventId)) {
				set = Collections.synchronizedSet(new HashSet<AbstractTasklet>());
				triggerTaskMap.put(eventId, set);
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

	public Set<AbstractTasklet> getTriggerTasks(int eventid) {
		Set<AbstractTasklet> result = null;
		synchronized (triggerTaskMap) {
			if (triggerTaskMap.containsKey(eventid)) {
				result = triggerTaskMap.get(eventid);
			}
		}
		return result;
	}

	private TaskletManager() {

	}

	public void execute(Runnable event) {
		triggerExecutor_.execute(event);
		if (triggerExecutor_ instanceof ThreadPoolExecutor) {
			ThreadPoolExecutor exec = triggerExecutor_;
			if (exec.getTaskCount() % 1000 == 0) {
				System.out.println("Count: " + exec.getTaskCount() + " (" + exec.getCompletedTaskCount() + ")");
			}
		}
	}

	// public void execute(Runnable event) {
	// Future<?> result = triggerExecutor_.submit(event);
	// System.out.println(result.isDone());
	// }

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

	private void setRegistry(IExtensionRegistry registry) {
		this.registry = registry;
		extensionTracker = new ExtensionTracker(registry);

		// Init server Console
		serverConsole = new ServerConsole();
		osgiServerConsole = new OSGiServerConsoleAdaptor(serverConsole);

		// Register the tasklet Service
		Activator.getDefault().getBundle().getBundleContext().registerService(TaskletService.class.getName(), this, null);
	}

	public void setup(IExtensionRegistry registry) {
		setRegistry(registry);

		scheduleExecutor_ = new Guardian(getFixedScheduleThreads());
		// triggerExecutor_ = Executors.newFixedThreadPool(2, new SessionThreadFactory());
		// if (triggerExecutor_ instanceof ThreadPoolExecutor) {
		// ThreadPoolExecutor exec = (ThreadPoolExecutor) triggerExecutor_;
		// System.out.println(exec.getQueue().getClass().getName());
		// }
		triggerExecutor_ = new Intimidator(getTriggerCoreThreads(), getTriggerMaxThreads(), getTriggerKeepAliveSeconds());
		trackExtensionPoint(EXTENSIONPOINT_ID);
		trackExtensionPoint(ALIAS_EXTENSIONPOINT_ID);
		trackExtensionPoint(MULTI_TASK_EXTENSIONPOINT_ID);
	}

	public void teardown() {
		System.out.println("Shutting down TaskletManager...");
		triggerExecutor_.shutdown();
		scheduleExecutor_.shutdown();
		extensionTracker.close();
	}

	public void processCommand(final String commandBuffer) {
		// System.out.println(commandBuffer);
		String eventChk = DotsEventFactory.runEventsForString(commandBuffer);
		if (eventChk != null) {
			// do some different stuff, probably command line
			System.out.println("NON-EVENT command " + commandBuffer);
		}
	}

	private int scheduleThreadCount = DEFAULT_SCHEDULE_THREADS;

	public int getFixedScheduleThreads() {
		return scheduleThreadCount;
	}

	public void setFixedScheduleThreads(int count) {
		scheduleThreadCount = count;
	}

	private int triggerCoreThreadCount = DEFAULT_TRIGGER_CORE_THREADS;

	public int getTriggerCoreThreads() {
		return triggerCoreThreadCount;
	}

	private int triggerMaxThreadCount = DEFAULT_TRIGGER_MAX_THREADS;

	public int getTriggerMaxThreads() {
		return triggerMaxThreadCount;
	}

	private long triggerKeepAlive = DEFAULT_TRIGGER_KEEP_ALIVE_SECONDS;

	public long getTriggerKeepAliveSeconds() {
		return triggerKeepAlive;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.task.TaskletService#registerTasklet(java.lang.String, com.ibm.dots.task.AbstractServerTask)
	 */
	@Override
	public void registerTasklet(String taskletId, AbstractServerTask tasklet) {
		// TODO Auto-generated method stub
		System.out.println("WARNING: UNIMPLEMENTED");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.task.TaskletService#registerTasklet(java.lang.String, com.ibm.dots.task.AbstractServerTask, java.lang.String)
	 */
	@Override
	public void registerTasklet(String taskletId, AbstractServerTask tasklet, String description) {
		// TODO Auto-generated method stub
		System.out.println("WARNING: UNIMPLEMENTED");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.task.TaskletService#registerTasklet(java.lang.String, com.ibm.dots.task.AbstractServerTask, java.lang.String,
	 * com.ibm.dots.task.RunWhen[])
	 */
	@Override
	public void registerTasklet(String taskletId, AbstractServerTask tasklet, String description, RunWhen... runWhens) {
		// TODO Auto-generated method stub
		System.out.println("WARNING: UNIMPLEMENTED");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.task.TaskletService#scheduleTasklet(java.lang.String, com.ibm.dots.task.RunWhen)
	 */
	@Override
	public void scheduleTasklet(String taskletId, RunWhen runWhen) throws CoreException {
		// TODO Auto-generated method stub
		System.out.println("WARNING: UNIMPLEMENTED");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.task.TaskletService#scheduleTasklet(java.util.Dictionary)
	 */
	@Override
	public void scheduleTasklet(Dictionary<String, String> properties) throws CoreException {
		// TODO Auto-generated method stub
		System.out.println("WARNING: UNIMPLEMENTED");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.task.TaskletService#runTasklet(java.lang.String, java.lang.String[])
	 */
	@Override
	public void runTasklet(String taskletId, String... args) {
		// TODO Auto-generated method stub
		System.out.println("WARNING: UNIMPLEMENTED");
	}

}
