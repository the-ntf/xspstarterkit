/**
 * 
 */
package org.openntf.xsp.responsive.library;

import java.util.ArrayList;
import java.util.List;

import org.openntf.xsp.responsive.Activator;
import org.openntf.xsp.responsive.config.ResponsiveConfig;

import com.ibm.xsp.extlib.config.ExtlibPluginConfig;
import com.ibm.xsp.library.AbstractXspLibrary;

/**
 * @author nfreeman
 * 
 */
public class XspLibrary extends AbstractXspLibrary {
	private final static String LIBRARY_ID = XspLibrary.class.getName();
	private static Boolean GLOBAL;
	private List<ExtlibPluginConfig> plugins_;

	private static boolean isGlobal() {
		if (GLOBAL == null) {
			GLOBAL = Boolean.FALSE;
			String[] envs = Activator.getEnvironmentStrings();
			if (envs != null) {
				for (String s : envs) {
					if (s.equalsIgnoreCase("global")) {
						GLOBAL = Boolean.TRUE;
					}
				}
			}
		}
		return GLOBAL.booleanValue();
	}

	/**
	 * 
	 */
	public XspLibrary() {

	}

	private List<ExtlibPluginConfig> getExtlibPluginConfigs() {
		if (plugins_ == null) {
			List<ExtlibPluginConfig> plugins_ = new ArrayList<ExtlibPluginConfig>();
			plugins_.add(new ResponsiveConfig());

		}
		return plugins_;
	}

	@Override
	public String[] getXspConfigFiles() {
		String[] files = new String[] {};
		List<ExtlibPluginConfig> plugins = getExtlibPluginConfigs();
		for (ExtlibPluginConfig plugin : plugins) {
			files = plugin.getXspConfigFiles(files);
		}
		return files;
	}

	@Override
	public String[] getFacesConfigFiles() {
		String[] files = new String[] {};
		List<ExtlibPluginConfig> plugins = getExtlibPluginConfigs();
		for (ExtlibPluginConfig plugin : plugins) {
			files = plugin.getFacesConfigFiles(files);
		}
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

	@Override
	public String getPluginId() {
		return Activator.PLUGIN_ID;
	}

	@Override
	public boolean isGlobalScope() {
		boolean result = isGlobal();
		return result;
	}
}
