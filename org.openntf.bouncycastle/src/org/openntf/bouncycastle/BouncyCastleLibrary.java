/**
 * 
 */
package org.openntf.bouncycastle;

import com.ibm.xsp.library.AbstractXspLibrary;

/**
 * @author nfreeman
 * 
 */
public class BouncyCastleLibrary extends AbstractXspLibrary {
	private final static String LIBRARY_ID = BouncyCastleLibrary.class.getName();

	/**
	 * 
	 */
	public BouncyCastleLibrary() {
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

	@Override
	public boolean isGlobalScope() {
		return false;
	}

}
