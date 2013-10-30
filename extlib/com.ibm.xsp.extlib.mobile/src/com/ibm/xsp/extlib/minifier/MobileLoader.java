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
import com.ibm.xsp.extlib.plugin.MobilePluginActivator;
import com.ibm.xsp.extlib.resources.ExtlibResourceProvider;
import com.ibm.xsp.extlib.util.ExtLibUtil;


/**
 * Resource Loader that loads the resource from extlib.
 */
public class MobileLoader extends ExtLibLoaderExtension {

    public MobileLoader() {
    }

    @Override
    public Bundle getOSGiBundle() {
        return MobilePluginActivator.instance.getBundle();
    }
    
    
    // ========================================================
    //  Handling Dojo
    // ========================================================

    @Override
    public void loadDojoShortcuts(DoubleMap<String, String> aliases, DoubleMap<String, String> prefixes) {
        /// ALIASES
        if(aliases!=null) {
            //aliases.put("@EMa","extlib.dijit.Mobile");
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
        /// ALIASES
        if(aliases!=null) {
            //aliases.put("@Ea","/.ibmxspres/.extlib/css/customAndroid.css");
        }

        /// PREFIXES
        if(prefixes!=null) {
        }
    }

    
    // ========================================================
    // Serving resources
    // ========================================================
    
    @Override
    public URL getResourceURL(HttpServletRequest request, String name) {
        String path = ExtlibResourceProvider.BUNDLE_RES_PATH_EXTLIB+name;
        return ExtLibUtil.getResourceURL(MobilePluginActivator.instance.getBundle(), path);
    }
}
