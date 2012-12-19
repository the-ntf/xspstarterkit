/**
 * 
 */
package org.openntf.apache.commons;

import com.ibm.xsp.library.AbstractXspLibrary;

/**
 * @author nfreeman
 * 
 */
public class CommonsLibrary extends AbstractXspLibrary {
	private final static String LIBRARY_ID = CommonsLibrary.class.getName();

	/**
	 * 
	 */
	public CommonsLibrary() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.library.XspLibrary#getLibraryId()
	 */
	@Override
	public String getLibraryId() {
		return LIBRARY_ID;
	}

	@Override
	public String getPluginId() {
		return Activator.PLUGIN_ID;
	}

	// @Override
	// public String[] getDependencies() {
	// return new String[] { "" };
	// }
	//
	// @Override
	// public String[] getXspConfigFiles() {
	// String[] files = new String[] { "" };
	// return files;
	// }
	//
	// @Override
	// public String[] getFacesConfigFiles() {
	// String[] files = new String[] { "" };
	// return files;
	// }

	private static Boolean GLOBAL = null;

	private static boolean isGlobal() {
		org.slf4j.LoggerFactory lf;
		if (GLOBAL = null) {
			GLOBAL = Boolean.FALSE;
			String[] envs = Activator.getEnvironmentStrings();
			for (String s : envs) {
				if (s.equalsIgnoreCase("global")) {
					GLOBAL = Boolean.TRUE;
				}
			}
		}
		return GLOBAL.booleanValue();
	}

	@Override
	public boolean isGlobalScope() {
		return isGlobal();
	}

}
