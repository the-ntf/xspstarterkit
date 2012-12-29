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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import lotus.domino.NotesException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.ibm.dots.Activator;
import com.ibm.dots.annotation.Run;
import com.ibm.dots.annotation.RunEvery;
import com.ibm.dots.annotation.Triggered;
import com.ibm.dots.event.ExtensionManagerBridge;
import com.ibm.dots.event.IExtensionManagerEvent;
import com.ibm.dots.internal.Resolver;
import com.ibm.dots.task.RunWhen.RunUnit;
import com.ibm.dots.utils.TimeUtils;

/**
 * @author dtaieb
 * 
 */
public class ServerTaskInfo {

	private IConfigurationElement configurationElement;
	private IServerTaskRunnable serverTaskRunnable;
	private String id;
	private String description;
	private boolean bRunOnStart;
	private List<RunWhen> runWhenList;
	private boolean bTriggered;
	private HashMap<Integer, List<Method>> triggeredAnnotatedMap;
	private boolean bIsTriggeredAnnotationRead;
	private RunWhen annotatedRunWhen;

	// Used to track dynamically scheduled runs via OSGi profile configuration
	private static volatile HashMap<String, List<RunWhen>> registeredRunWhen;

	/**
	 * @param element
	 * @throws CoreException
	 * 
	 */
	ServerTaskInfo(IConfigurationElement element) throws CoreException {
		configurationElement = element;
		id = element.getAttribute("id"); // $NON-NLS-1$
		if (id == null) {
			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Missing id attribute")); // $NON-NLS-1$
		}
		description = element.getAttribute("description"); // $NON-NLS-1$
		String runOnStart = element.getAttribute("runOnStart"); // $NON-NLS-1$
		if (runOnStart != null) {
			bRunOnStart = Boolean.parseBoolean(runOnStart);
		}

		String sTriggered = element.getAttribute("triggered"); // $NON-NLS-1$
		if (sTriggered != null) {
			bTriggered = Boolean.parseBoolean(sTriggered);
		}
	}

	/**
	 * @param element
	 * @param id
	 * @param annotatedRunWhen
	 *            Private constructor used only with annotated methods
	 */
	private ServerTaskInfo(IConfigurationElement element, String id, RunWhen annotatedRunWhen) {
		this.configurationElement = element;
		this.id = id;
		runWhenList = Collections.emptyList();
		this.annotatedRunWhen = annotatedRunWhen;
	}

	/**
	 * @param taskletId
	 * @param description
	 * @param tasklet
	 */
	protected ServerTaskInfo(String taskletId, String description, AbstractServerTask tasklet) {
		this.id = taskletId;
		this.description = description;
		this.serverTaskRunnable = tasklet;
	}

	@Override
	public String toString() {
		return getId();
	}

	/**
	 * @return
	 */
	public boolean isRunOnStart() {
		return bRunOnStart;
	}

	/**
	 * @return
	 */
	public boolean isTriggered() {
		return bTriggered;
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param children
	 */
	protected RunWhen[] getRuns() {
		if (runWhenList == null) {
			runWhenList = new ArrayList<RunWhen>();
			if (configurationElement != null) {
				IConfigurationElement[] children = configurationElement.getChildren();
				Resolver resolver = new Resolver();

				try {
					for (IConfigurationElement child : children) {
						RunWhen r = readRun(resolver, child);
						if (r != null) {
							runWhenList.add(r);
						}
					}
				} finally {
					resolver.dispose();
				}
			}

			if (registeredRunWhen != null) {
				List<RunWhen> runWhens = registeredRunWhen.get(id);
				if (runWhens != null) {
					runWhenList.addAll(runWhens);
				}
			}

			// Add methods with RunEvery if any
			try {
				Class<?> cl = getServerTaskRunnable().getClass();
				Method[] methods = cl.getDeclaredMethods();
				Class<?>[] progressMonitorArray = new Class<?>[] { IProgressMonitor.class };
				Class<?>[] array2 = new Class<?>[] { String[].class, IProgressMonitor.class };
				for (Method m : methods) {
					Class<?>[] paramTypes = m.getParameterTypes();
					if (Arrays.equals(paramTypes, progressMonitorArray) || Arrays.equals(paramTypes, array2)) {
						Annotation annotation = m.getAnnotation(RunEvery.class);
						if (annotation instanceof RunEvery) {
							RunEvery runEvery = (RunEvery) annotation;
							// RunWhen r = new RunWhen(runEvery.unit(), runEvery.every(), TimeUtils.getTimeOfDay(runEvery.startAt()),
							// TimeUtils.getTimeOfDay(runEvery.stopAt()));
							// r.annotatedMethod = m;
							// runWhenList.add(r);
						}
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return runWhenList.toArray(new RunWhen[0]);
	}

	/**
	 * @param child
	 * @return
	 */
	private RunWhen readRun(Resolver resolver, IConfigurationElement child) {
		try {
			if ("run".equals(child.getName())) { // $NON-NLS-1$
				int every = getInteger(resolver.resolve(child.getAttribute("every"))); // $NON-NLS-1$
				RunWhen.RunUnit unit = getRunUnit(resolver.resolve(child.getAttribute("unit"))); // $NON-NLS-1$
				if (unit != null) {
					// Every is optional for day unit
					if (unit == RunWhen.RunUnit.day || every > 0) {
						RunWhen runWhen = new RunWhen(unit, every, TimeUtils.getTimeOfDay(resolver.resolve(child.getAttribute("startAt"))), // $NON-NLS-1$
								TimeUtils.getTimeOfDay(resolver.resolve(child.getAttribute("stopAt"))) // $NON-NLS-1$
						);
						return runWhen;
					}
				}
			}
		} catch (NotesException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @param value
	 * @return
	 */
	private RunUnit getRunUnit(String value) {
		if (value == null) {
			return null;
		}
		if ("day".equals(value)) { // $NON-NLS-1$
			return RunUnit.day;
		} else if ("second".equals(value)) { // $NON-NLS-1$
			return RunUnit.second;
		} else if ("minute".equals(value)) { // $NON-NLS-1$
			return RunUnit.minute;
		}
		return null;
	}

	/**
	 * @param value
	 * @return
	 */
	private int getInteger(String value) {
		if (value == null) {
			return 0;
		}
		try {
			return Integer.valueOf(value);
		} catch (NumberFormatException ex) {
			return 0;
		}
	}

	/**
	 * @return
	 * @throws CoreException
	 */
	protected IServerTaskRunnable getServerTaskRunnable() throws CoreException {
		return getServerTaskRunnable(false);
	}

	/**
	 * @param bCreate
	 * @return
	 * @throws CoreException
	 */
	protected IServerTaskRunnable getServerTaskRunnable(boolean bCreate) throws CoreException {
		IServerTaskRunnable retTask = null;
		if (serverTaskRunnable == null || bCreate) {
			if (configurationElement != null) {
				if ("multiTask".equals(configurationElement.getName())) { // $NON-NLS-1$
					retTask = new MultiTaskletRunner(configurationElement);
				} else {
					retTask = (IServerTaskRunnable) configurationElement.createExecutableExtension("class"); // $NON-NLS-1$
				}
				retTask.init(this);
				if (bCreate) {
					return retTask;
				}
			}
		}

		if (serverTaskRunnable == null) {
			serverTaskRunnable = retTask;
		}

		return serverTaskRunnable;
	}

	/**
	 * @return
	 */
	protected boolean canLoad() {
		if (configurationElement == null) {
			return true;
		}

		try {
			ITaskFilter filter = (ITaskFilter) configurationElement.createExecutableExtension("filter"); // $NON-NLS-1$
			return filter.canLoad(getId());
		} catch (Throwable t) {
			// Ignore as it may not have been defined
		}
		// Allow loading by default
		return true;
	}

	/**
	 * @return
	 */
	protected String getClassName() {
		if (configurationElement != null) {
			return configurationElement.getAttribute("class"); // $NON-NLS-1$
		} else if (serverTaskRunnable != null) {
			return serverTaskRunnable.getClass().getName();
		}
		return null;
	}

	/**
	 * @return
	 */
	protected boolean isScheduled() {
		return getRuns().length > 0;
	}

	/**
	 * @param emEvent
	 * @return
	 */
	protected Method[] getTriggeredAnnotatedMethods(IExtensionManagerEvent emEvent) {
		List<Method> methods = getAllTriggeredAnnotations().get(emEvent.getEventId());
		return methods == null ? new Method[0] : methods.toArray(new Method[0]);
	}

	public int[] getTriggeredEventIds() {
		int[] result = new int[0];
		Set<Integer> keys = getAllTriggeredAnnotations().keySet();
		if (keys != null) {
			result = new int[keys.size()];
			int i = 0;
			for (Integer key : keys) {
				result[i] = key.intValue();
				i++;
			}
		}
		return result;
	}

	private HashMap<Integer, List<Method>> getAllTriggeredAnnotations() {
		if (triggeredAnnotatedMap == null) {
			triggeredAnnotatedMap = new HashMap<Integer, List<Method>>();
		}
		if (!bIsTriggeredAnnotationRead) {
			try {
				Class<?> cl = getServerTaskRunnable().getClass();
				Method[] methods = cl.getDeclaredMethods();
				for (Method m : methods) {
					Class<?>[] paramTypes = m.getParameterTypes();
					// Check for Triggered method annotation
					Annotation annotation = m.getAnnotation(Triggered.class);
					if (annotation instanceof Triggered) {
						Triggered triggered = (Triggered) annotation;
						int[] eventIds = triggered.eventId();
						if (eventIds != null) {
							for (int eventId : eventIds) {
								Class<?> emEventClass = ExtensionManagerBridge.getEMEventClass(eventId);
								if (emEventClass != null
										&& Arrays.equals(paramTypes, new Class<?>[] { emEventClass, IProgressMonitor.class })) {

									List<Method> trgMethods = triggeredAnnotatedMap.get(eventId);
									if (trgMethods == null) {
										trgMethods = new ArrayList<Method>();
										triggeredAnnotatedMap.put(eventId, trgMethods);
									}
									trgMethods.add(m);
								}
							}
						}
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
			bIsTriggeredAnnotationRead = true;
		}
		return triggeredAnnotatedMap;
	}

	// /**
	// *
	// */
	// private void readTriggeredAnnotations() {
	// if (bIsTriggeredAnnotationRead) {
	// return;
	// }
	//
	// try {
	// Class<?> cl = getServerTaskRunnable().getClass();
	// Method[] methods = cl.getDeclaredMethods();
	// for (Method m : methods) {
	// Class<?>[] paramTypes = m.getParameterTypes();
	// // Check for Triggered method annotation
	// Annotation annotation = m.getAnnotation(Triggered.class);
	// if (annotation instanceof Triggered) {
	// Triggered triggered = (Triggered) annotation;
	// int[] eventIds = triggered.eventId();
	// if (eventIds != null) {
	// for (int eventId : eventIds) {
	// Class<?> emEventClass = ExtensionManagerBridge.getEMEventClass(eventId);
	// if (emEventClass != null && Arrays.equals(paramTypes, new Class<?>[] { emEventClass, IProgressMonitor.class })) {
	// if (triggeredAnnotatedMap == null) {
	// triggeredAnnotatedMap = new HashMap<Integer, List<Method>>();
	// }
	//
	// List<Method> trgMethods = triggeredAnnotatedMap.get(eventId);
	// if (trgMethods == null) {
	// trgMethods = new ArrayList<Method>();
	// triggeredAnnotatedMap.put(eventId, trgMethods);
	// }
	// trgMethods.add(m);
	// }
	// }
	// }
	// }
	// }
	// } catch (Throwable e) {
	// e.printStackTrace();
	// }
	// bIsTriggeredAnnotationRead = true;
	// }

	/**
	 * @return
	 */
	protected boolean hasTriggerableAnnotatedMethods() {
		return !getAllTriggeredAnnotations().isEmpty();
		// readTriggeredAnnotations();
		// if (triggeredAnnotatedMap == null) {
		// return false;
		// }
		// return !triggeredAnnotatedMap.isEmpty();
	}

	/**
	 * @return
	 */
	protected ServerTaskInfo[] getRunAnnotatedMethods() {
		ArrayList<ServerTaskInfo> runs = new ArrayList<ServerTaskInfo>();

		// Add methods with Run if any
		try {
			Class<?> cl = getServerTaskRunnable().getClass();
			Method[] methods = cl.getDeclaredMethods();
			Class<?>[] progressMonitorArray = new Class<?>[] { IProgressMonitor.class };
			Class<?>[] array2 = new Class<?>[] { String[].class, IProgressMonitor.class };
			for (Method m : methods) {
				Class<?>[] paramTypes = m.getParameterTypes();
				if (Arrays.equals(paramTypes, progressMonitorArray) || Arrays.equals(paramTypes, array2)) {
					Annotation annotation = m.getAnnotation(Run.class);
					if (annotation instanceof Run) {
						Run run = (Run) annotation;
						RunWhen r = new RunWhen(RunUnit.once, 0, 0, 0);
						r.annotatedMethod = m;
						String id = cl.getSimpleName() + "." + run.id();
						runs.add(new ServerTaskInfo(configurationElement, id, r));
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return runs.toArray(new ServerTaskInfo[0]);
	}

	/**
	 * @return
	 */
	protected RunWhen getAnnotatedRunWhen() {
		return annotatedRunWhen;
	}

	/**
	 * @param runWhen
	 */
	protected void registerRunWhen(RunWhen runWhen) {

		if (registeredRunWhen == null) {
			synchronized (getClass()) {
				if (registeredRunWhen == null) {
					registeredRunWhen = new HashMap<String, List<RunWhen>>();
				}
			}
		}

		List<RunWhen> runWhens = registeredRunWhen.get(id);
		if (runWhens == null) {
			runWhens = new ArrayList<RunWhen>();
			registeredRunWhen.put(id, runWhens);
		}

		runWhens.add(runWhen);

		// Reset the cached version
		runWhenList = null;
	}
}
