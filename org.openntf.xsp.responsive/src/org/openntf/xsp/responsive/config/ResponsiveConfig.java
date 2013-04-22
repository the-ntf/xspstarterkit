/**
 * 
 */
package org.openntf.xsp.responsive.config;

import java.util.logging.Logger;

import com.ibm.xsp.extlib.config.ExtlibPluginConfig;

/**
 * @author nfreeman
 * 
 */
public class ResponsiveConfig extends ExtlibPluginConfig {
	private static final Logger log_ = Logger.getLogger(ResponsiveConfig.class.getName());
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.extlib.config.ExtlibPluginConfig#getFacesConfigFiles(java.lang.String[])
	 */
	@Override
	public String[] getFacesConfigFiles(String[] files) {
		return concat(files, new String[] { "org/openntf/xsp/responsive/config/responsive-faces-config.xml", // $NON-NLS-1$
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.extlib.config.ExtlibPluginConfig#getXspConfigFiles(java.lang.String[])
	 */
	@Override
	public String[] getXspConfigFiles(String[] files) {
		return concat(files, new String[] { "org/openntf/xsp/responsive/config/extlib-common.xsp-config", // $NON-NLS-1$
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.extlib.config.ExtlibPluginConfig#installResources()
	 */
	@Override
	public void installResources() {
		// TODO Auto-generated method stub
		super.installResources();
	}
}
