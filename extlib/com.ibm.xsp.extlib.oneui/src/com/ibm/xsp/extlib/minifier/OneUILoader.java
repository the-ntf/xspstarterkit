/* ***************************************************************** */
/* Licensed Materials - Property of IBM                              */
/*                                                                   */
/* Copyright IBM Corp. 1985, 2013 All Rights Reserved                */
/*                                                                   */
/* US Government Users Restricted Rights - Use, duplication or       */
/* disclosure restricted by GSA ADP Schedule Contract with           */
/* IBM Corp.                                                         */
/*                                                                   */
/* ***************************************************************** */






package com.ibm.xsp.extlib.minifier;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.osgi.framework.Bundle;

import com.ibm.commons.util.DoubleMap;
import com.ibm.xsp.extlib.plugin.OneUIPluginActivator;
import com.ibm.xsp.extlib.resources.ExtlibResourceProvider;
import com.ibm.xsp.extlib.util.ExtLibUtil;


/**
 * Resource Loader that loads the resource from extlib.
 */
public class OneUILoader extends ExtLibLoaderExtension {

    public OneUILoader() {
    }

    @Override
    public Bundle getOSGiBundle() {
        return OneUIPluginActivator.instance.getBundle();
    }

    
    // ========================================================
    //  Handling Dojo
    // ========================================================
    
    @Override
    public void loadDojoShortcuts(DoubleMap<String, String> aliases, DoubleMap<String, String> prefixes) {
        super.loadDojoShortcuts(aliases, prefixes);
        
        /// ALIASES
        if(aliases!=null) {
            aliases.put("@EOa","extlib.dijit.OneUIDialog"); //$NON-NLS-1$ //$NON-NLS-2$
            aliases.put("@EOb","extlib.dijit.OneUINavigator"); //$NON-NLS-1$ //$NON-NLS-2$
            aliases.put("@EOc","extlib.dijit.OneUIPickerCheckbox"); //$NON-NLS-1$ //$NON-NLS-2$
            aliases.put("@EOd","extlib.dijit.OneUIPickerList"); //$NON-NLS-1$ //$NON-NLS-2$
            aliases.put("@EOe","extlib.dijit.OneUIPickerListSearch"); //$NON-NLS-1$ //$NON-NLS-2$
            aliases.put("@EOf","extlib.dijit.OneUIPickerName"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        /// PREFIXES
        if(prefixes!=null) {
        }
    }
    
    
    // ========================================================
    //  Handling CSS
    // ========================================================
    
    @Override
    public void loadCSSShortcuts(DoubleMap<String, String> aliases, DoubleMap<String, String> prefixes) {
    }

    
    // ========================================================
    // Serving resources
    // ========================================================

    @Override
    public URL getResourceURL(HttpServletRequest request, String name) {
        String path = ExtlibResourceProvider.BUNDLE_RES_PATH_EXTLIB+name;
        return ExtLibUtil.getResourceURL(OneUIPluginActivator.instance.getBundle(), path);
    }
}
