package org.openntf.apache.commons;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;

public class Activator extends Plugin {
	public static final String PLUGIN_ID = Activator.class.getPackage().getName();

	public static Activator instance;

	public Activator() {
		instance = this;
	}

	private static String version;

	public static String getVersion() {
		if (version == null) {
			version = (String) instance.getBundle().getHeaders().get("Bundle-Version");
		}
		return version;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext )
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
	}

	public static Activator getDefault() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		instance = null;
	}

	private static Boolean DEBUG = null;

	public static boolean isDebug() {
		if (DEBUG = null) {
			DEBUG = Boolean.FALSE;
			String[] envs = getEnvironmentStrings();
			for (String s : envs) {
				if (s.equalsIgnoreCase("debug")) {
					DEBUG = Boolean.TRUE;
				}
			}
		}
		return DEBUG.booleanValue();
	}

	public static String[] getEnvironmentStrings() {
		String[] result = null;
		try {
			String setting = Platform.getInstance().getProperty(PLUGIN_ID); // $NON-NLS-1$
			if (StringUtil.isEmpty(setting)) {
				setting = System.getProperty(PLUGIN_ID); // $NON-NLS-1$
				if (StringUtil.isEmpty(setting)) {
					setting = com.ibm.xsp.model.domino.DominoUtils.getEnvironmentString(PLUGIN_ID); // $NON-NLS-1$
				}
			}
			if (StringUtil.isNotEmpty(setting)) {
				result = StringUtil.splitString(setting, ',');
			}
		} catch (Throwable t) {

		}
		return result;
	}

}
