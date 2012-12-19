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

package com.ibm.dots;

import java.text.MessageFormat;
import java.util.ArrayList;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import com.ibm.dots.internal.OSGIConsoleAdaptor;
import com.ibm.dots.internal.OSGiProfileProxy;
import com.ibm.dots.internal.ServerTaskProgressProvider;
import com.ibm.dots.internal.preferences.DominoOSGiPreferences;
import com.ibm.dots.internal.preferences.DominoOSGiScope;
import com.ibm.dots.internal.preferences.RunInitConfigurationsJob;
import com.ibm.dots.task.AliasTaskInfo;
import com.ibm.dots.task.ServerTaskManager;

public class Activator extends Plugin implements ServiceTrackerCustomizer, CommandProvider {
	public static final String PLUGIN_ID = "com.ibm.dots"; // $NON-NLS-1$
	private static Activator plugin;

	private IExtensionRegistry registry;
	private ServiceTracker registryTracker;
	private ServiceTracker commandProviderTracker;
	private ServiceRegistration commandProviderSvcReg;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		registryTracker = new ServiceTracker(context, IExtensionRegistry.class.getName(), this);
		registryTracker.open();

		commandProviderTracker = new ServiceTracker(context, CommandProvider.class.getName(), null);
		commandProviderTracker.open();

		commandProviderSvcReg = context.registerService(CommandProvider.class.getName(), this, null);

		// Start the Job Manager Progress Provider
		ServerTaskProgressProvider.getInstance();

		// Initialize the Preference subsystem
		DominoOSGiScope.getInstance().getNode("");
		// System.out.println("DOTS Activated! Ready to receive commands!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;

		if (commandProviderSvcReg != null) {
			commandProviderSvcReg.unregister();
		}

		registryTracker.close();
		commandProviderTracker.close();

		super.stop(context);
	}

	/**
	 * @return
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * @return
	 */
	public static ServiceTracker getCommandProviderTracker() {
		return plugin.commandProviderTracker;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#addingService(org.osgi.framework.ServiceReference)
	 */
	@Override
	public Object addingService(ServiceReference reference) {
		Object service = getBundle().getBundleContext().getService(reference);
		if (service instanceof IExtensionRegistry && registry == null) {
			registry = (IExtensionRegistry) service;

			// Init the ServerTaskManager
			ServerTaskManager.createInstance(registry).start();

			// start the early startup job
			new RunEarlyStartupExtensionJob(registry).schedule();

			// Start the Init configuration job
			new RunInitConfigurationsJob(registry).schedule();
		}
		return service;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#modifiedService(org.osgi.framework.ServiceReference, java.lang.Object)
	 */
	@Override
	public void modifiedService(ServiceReference arg0, Object arg1) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
	 */
	@Override
	public void removedService(ServiceReference reference, Object service) {
		if (registry == service) {
			registry = null;
		}
	}

	/**
	 * @param ci
	 */
	public void _help(CommandInterpreter ci) {
		OSGIConsoleAdaptor.displayHelp(ci);
	}

	/**
	 * @param ci
	 */
	public void _run(CommandInterpreter ci) {
		String taskId = ci.nextArgument();
		if (taskId == null) {
			ci.print("\tMissing task id");
			_help(ci);
			return;
		}
		ArrayList<String> taskArgs = new ArrayList<String>();
		String arg;
		while ((arg = ci.nextArgument()) != null) {
			taskArgs.add(arg);
		}
		ServerTaskManager.getInstance().runTask(ci, taskId, taskArgs.toArray(new String[0]));
	}

	/**
	 * @param ci
	 */
	public void _aliases(CommandInterpreter ci) {
		AliasTaskInfo[] aliases = ServerTaskManager.getInstance().getAliases();
		for (AliasTaskInfo alias : aliases) {
			ci.print(alias);
		}
	}

	/**
	 * @param ci
	 */
	public void _tasklist(CommandInterpreter ci) {
		ServerTaskManager.getInstance().displayTaskList(ci);
	}

	/**
	 * @param ci
	 */
	public void _taskinfo(CommandInterpreter ci) {
		String taskId = ci.nextArgument();
		if (taskId == null) {
			ci.print("Missing task id");
		} else {
			ServerTaskManager.getInstance().displayTaskInfo(ci, taskId);
		}
	}

	/**
	 * @param ci
	 */
	public void _taskstatus(CommandInterpreter ci) {
		ci.print("Display the list of OSGi tasklets currently in progress");
		ServerTaskManager.getInstance().displayStatus(ci);
	}

	/**
	 * @param ci
	 */
	public void _cancel(CommandInterpreter ci) {
		String nextArg = ci.nextArgument();
		if (nextArg == null) {
			ci.print("Canceling all running tasks...");
			ServerTaskManager.getInstance().cancelAllTasks(ci);
		} else {
			ci.print(MessageFormat.format("Canceling task {0}...", nextArg));
			ServerTaskManager.getInstance().cancelTask(nextArg, ci);
		}
	}

	/**
	 * @param ci
	 * @throws Exception
	 */
	public void _profileCreate(CommandInterpreter ci) throws Exception {
		if (!OSGiProfileProxy.hasConfigDb()) {
			ci.println("No configuration db defined. Please use OSGI_CONFIGURATION_DB ini variable to define your configuration database"); // $NON-NLS-1$
			return;
		}
		String profileName = ci.nextArgument();
		if (profileName == null) {
			ci.println("Invalid syntax: no profile name specified"); // $NON-NLS-1$
			return;
		}

		if (OSGiProfileProxy.hasProfile(profileName)) {
			ci.println(MessageFormat.format("Profile {0} already exists in configuration db {1}", profileName,
					OSGiProfileProxy.getOSGiConfDb())); // $NON-NLS-1$
			return;
		}
		DominoOSGiPreferences.createProfile(ci, profileName);
		ci.println(MessageFormat.format("Profile {0} successfully created in configuration db {1}", profileName,
				OSGiProfileProxy.getOSGiConfDb())); // $NON-NLS-1$
	}

	/**
	 * @return
	 */
	public String getHelpCategory() {
		return "dots"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.osgi.framework.console.CommandProvider#getHelp()
	 */
	@Override
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		sb.append("---Domino OSGi Tasklet Container Commands---\n");
		sb.append("\tclose/exit: close the current instance of the Domino OSGi Tasklet Container\n");
		sb.append("\ttasklist: Display list of OSGi server tasks\n");
		sb.append("\taliases : List of tasklet aliases and their description\n");
		sb.append("\trun [taskid]:     Run the task once specified by the task id\n");
		sb.append("\ttaskinfo [taskid]:    Display detailed information about the task\n");
		sb.append("\ttaskstatus:           Display information about the tasks being run\n");
		sb.append("\tcancel [taskNumber]:  Cancel the current task\n");
		sb.append("\tdumpstack [-a]:   Display the stack traces for the running thread (-a for all threads)\n");
		sb.append(" \n---Domino OSGi Tasklet Container Profile Management Commands---\n");
		sb.append("\tshowprofile: Show Info about the current profile\n");
		sb.append("\tprofileCreate [profileName]: Create a new profile\n");
		return sb.toString();
	}

	/**
	 * @return
	 */
	public IExtensionRegistry getRegistry() {
		return registry;
	}
}
