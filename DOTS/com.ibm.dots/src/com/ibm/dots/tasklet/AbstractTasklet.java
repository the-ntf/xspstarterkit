/**
 * 
 */
package com.ibm.dots.tasklet;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;

import com.ibm.dots.annotation.Run;
import com.ibm.dots.annotation.RunEvery;
import com.ibm.dots.annotation.RunOnStart;
import com.ibm.dots.annotation.Triggered;
import com.ibm.dots.event.ExtensionManagerBridge;
import com.ibm.dots.tasklet.events.DotsEvent;

/**
 * @author nfreeman
 * 
 */
public abstract class AbstractTasklet implements Observer, IExecutableExtension {
	String id;
	String description;

	static class RunStart implements Runnable {
		private final Method method_;
		private final AbstractTasklet tasklet_;

		public RunStart(Method method, AbstractTasklet tasklet) {
			method_ = method;
			tasklet_ = tasklet;
		}

		@Override
		public void run() {
			try {
				method_.invoke(tasklet_, (Object) null);
			} catch (Throwable t) {
				logMethod();
				t.printStackTrace();
			}
		}

		public void logMethod() {
			StringBuilder sb = new StringBuilder();
			sb.append(method_.getName());
			sb.append(": ");
			Class<?>[] params = method_.getParameterTypes();
			for (Class<?> param : params) {
				sb.append(param.getSimpleName());
				sb.append(",");
			}

			System.out.println(sb.toString());
		}
	}

	static class RunManual implements Runnable {
		private final Method method_;
		private final AbstractTasklet tasklet_;
		private final String id_;

		public RunManual(String id, Method method, AbstractTasklet tasklet) {
			id_ = id;
			method_ = method;
			tasklet_ = tasklet;
		}

		@Override
		public void run() {
			try {
				method_.invoke(tasklet_, (Object[]) null);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	static class RunSchedule implements Runnable {
		private final int every_;
		private final TimeUnit unit_;
		private final Method method_;
		private final AbstractTasklet tasklet_;
		private String startAt_;
		private String stopAt_;

		public RunSchedule(int every, TimeUnit unit, Method method, AbstractTasklet tasklet) {
			every_ = every;
			unit_ = unit;
			method_ = method;
			tasklet_ = tasklet;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				method_.invoke(tasklet_, (Object) null);
			} catch (Throwable t) {
				logMethod();
				// t.printStackTrace();
			}
		}

		public String getStartAt() {
			return startAt_;
		}

		public void setStartAt(String startAt) {
			startAt_ = startAt;
		}

		public String getStopAt() {
			return stopAt_;
		}

		public void setStopAt(String stopAt) {
			stopAt_ = stopAt;
		}

		public int getEvery() {
			return every_;
		}

		public TimeUnit getUnit() {
			return unit_;
		}

		public void logMethod() {
			StringBuilder sb = new StringBuilder();
			sb.append(method_.getName());
			sb.append(": ");
			Class<?>[] params = method_.getParameterTypes();
			for (Class<?> param : params) {
				sb.append(param.getSimpleName());
				sb.append(",");
			}

			System.out.println(sb.toString());
		}
	}

	private boolean isSetup_ = false;
	private boolean isTorndown_ = false;

	/**
	 * 
	 */
	public AbstractTasklet() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable paramObservable, Object paramObject) {
		// TODO Auto-generated method stub

	}

	public abstract boolean isResident();

	public void logMessage(String message) {

	}

	public void logException(Throwable t) {

	}

	public void setup(Object... args) {

	}

	public abstract void localInitialization(Object... args);

	public void teardown(Object... args) {
		if (!isTorndown_) {
			localTeardown(args);
			isTorndown_ = true;
		}
	}

	public abstract void localTeardown(Object... args);

	public abstract String getName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement,
	 * java.lang.String, java.lang.Object)
	 */
	@Override
	public void setInitializationData(IConfigurationElement arg0, String arg1, Object arg2) throws CoreException {
		if (!isSetup_) {
			initAnnotations();
			localInitialization(arg0, arg1, arg2);
			isSetup_ = true;
		}
	}

	protected Map<Integer, List<Method>> triggerAnnotatedMap_;
	protected Map<String, RunManual> manualMap_;
	protected Set<RunSchedule> schedule_;
	protected Set<RunStart> starts_;

	public Set<RunSchedule> getSchedules() {
		if (schedule_ == null) {
			schedule_ = new HashSet<RunSchedule>();
		}
		return schedule_;
	}

	public Set<RunStart> getStarts() {
		if (starts_ == null) {
			starts_ = new HashSet<RunStart>();
		}
		return starts_;
	}

	protected Map<String, RunManual> getManualMap() {
		if (manualMap_ == null) {
			manualMap_ = new HashMap<String, RunManual>();
		}
		return manualMap_;
	}

	protected Map<Integer, List<Method>> getTriggerMap() {
		if (triggerAnnotatedMap_ == null) {
			triggerAnnotatedMap_ = new HashMap<Integer, List<Method>>();
		}
		return triggerAnnotatedMap_;
	}

	private void initAnnotations() {
		try {
			Class<?> cl = getClass();
			Method[] methods = cl.getDeclaredMethods();
			for (Method m : methods) {
				Class<?>[] paramTypes = m.getParameterTypes();
				Annotation runStartChk = m.getAnnotation(RunOnStart.class);
				if (runStartChk instanceof RunOnStart) {
					RunOnStart run = (RunOnStart) runStartChk;
					RunStart man = new RunStart(m, this);
					getStarts().add(man);
				}

				Annotation runChk = m.getAnnotation(Run.class);
				if (runChk instanceof Run) {
					Run run = (Run) runChk;
					String id = run.id();
					RunManual man = new RunManual(id, m, this);
					getManualMap().put(id, man);
				}

				Annotation scheduleChk = m.getAnnotation(RunEvery.class);
				if (scheduleChk instanceof RunEvery) {
					RunEvery scheduled = (RunEvery) scheduleChk;
					int every = scheduled.every();
					TimeUnit unit = scheduled.unit();
					String startAt = scheduled.startAt();
					String stopAt = scheduled.stopAt();
					RunSchedule sched = new RunSchedule(every, unit, m, this);
					getSchedules().add(sched);
					if (startAt != null)
						sched.setStartAt(startAt);
					if (stopAt != null)
						sched.setStopAt(stopAt);

				}

				Annotation triggerChk = m.getAnnotation(Triggered.class);
				if (triggerChk instanceof Triggered) {
					Triggered triggered = (Triggered) triggerChk;
					int[] eventIds = triggered.eventId();
					if (eventIds != null) {
						for (int eventId : eventIds) {
							Class<?> emEventClass = ExtensionManagerBridge.getEMEventClass(eventId);
							if (emEventClass != null) {
								if (Arrays.equals(paramTypes, new Class<?>[] { emEventClass, IProgressMonitor.class })) {
									List<Method> trgMethods = getTriggerMap().get(eventId);
									if (trgMethods == null) {
										trgMethods = new ArrayList<Method>();
										getTriggerMap().put(eventId, trgMethods);
									}
									trgMethods.add(m);
								} else if (Arrays.equals(paramTypes, new Class<?>[] { DotsEvent.class, IProgressMonitor.class })) {
									List<Method> trgMethods = getTriggerMap().get(eventId);
									if (trgMethods == null) {
										trgMethods = new ArrayList<Method>();
										getTriggerMap().put(eventId, trgMethods);
									}
									trgMethods.add(m);
								}
							}

						}
					}
				}

			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public List<Method> getTriggerMethods(int eventid) {
		List<Method> result = getTriggerMap().get(eventid);
		return result;
	}

	public Set<Integer> getAllTriggerEvents() {
		Set<Integer> result = null;
		if (getTriggerMap() != null) {
			result = getTriggerMap().keySet();
		}
		return result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
