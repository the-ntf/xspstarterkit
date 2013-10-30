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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.osgi.framework.Bundle;

import com.ibm.commons.util.DoubleMap;


/**
 * Loader and resource provider extension for the extlib library
 */
public abstract class ExtLibLoaderExtension {
    
    private static List<ExtLibLoaderExtension> extensions = new ArrayList<ExtLibLoaderExtension>();
    
    public static List<ExtLibLoaderExtension> getExtensions() {
        return extensions;
    }
    
    protected ExtLibLoaderExtension() {        
    }
    
    public abstract Bundle getOSGiBundle();
    
    public void loadDojoShortcuts(DoubleMap<String, String> aliases, DoubleMap<String, String> prefixes) {
    }

    public void loadCSSShortcuts(DoubleMap<String, String> aliases, DoubleMap<String, String> prefixes) {
    }
    
    public URL getResourceURL(HttpServletRequest request, String name) {
        return null;
    }
}
