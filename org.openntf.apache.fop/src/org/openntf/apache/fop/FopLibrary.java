/**
 * 
 */
package org.openntf.apache.fop;

import org.apache.fop.apps.FopFactory;

import com.ibm.xsp.library.AbstractXspLibrary;

/**
 * @author nfreeman
 * 
 */
public class FopLibrary extends AbstractXspLibrary {
	private final static String LIBRARY_ID = FopLibrary.class.getName();

	/**
	 * 
	 */
	public FopLibrary() {
		// TODO Auto-generated constructor stub
		FopFactory factory = org.apache.fop.apps.FopFactory.newInstance();

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

	@Override
	public String[] getDependencies() {
		return new String[] { "org.openntf.apache.xmlbeans.XmlbeansLibrary" };
	}

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
