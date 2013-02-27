/**
 * 
 */
package org.openntf.domino.klepto;

import com.ibm.xsp.library.AbstractXspLibrary;

/**
 * @author nfreeman
 * 
 */
public class KleptoLibrary extends AbstractXspLibrary {
	private final static String LIBRARY_ID = KleptoLibrary.class.getName();

	/**
	 * 
	 */
	public KleptoLibrary() {
		System.out.println(KleptoLibrary.class.getName() + " constructed");
	}

	@Override
	public String[] getFacesConfigFiles() {
		String[] files = new String[] { "META-INF/klepto-faces-config.xml" };
		return files;
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

	@Override
	public String getPluginId() {
		return Activator.PLUGIN_ID;
	}

	@Override
	public boolean isGlobalScope() {
		return false;
	}

}
