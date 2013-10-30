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






package com.ibm.xsp.extlib.resources;

import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ibm.xsp.extlib.minifier.ExtLibLoaderExtension;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.webapp.FacesResourceServlet;
import com.ibm.xsp.webapp.resources.URLResourceProvider;

/**
 * Resources provider factory.
 * 
 * @author priand
 */
public class ExtlibResourceProvider extends URLResourceProvider {
    
    public static final String BUNDLE_RES_PATH = "/resources/web/";  // $NON-NLS-1$
    public static final String BUNDLE_RES_PATH_EXTLIB = "/resources/web/extlib/";  // $NON-NLS-1$

    public static final String DWA_RES_PATH = "/resources/web/"; // $NON-NLS-1$
    
    public static final String EXTLIB_PREFIX = ".extlib";  // $NON-NLS-1$
    
    // Resource Path
    public static final String RESOURCE_PATH =    FacesResourceServlet.RESOURCE_PREFIX  // "/.ibmxspres/" 
                                                + ExtlibResourceProvider.EXTLIB_PREFIX      // ".extlib" 
                                                + "/";
    public static final String DOJO_PATH     =    FacesResourceServlet.RESOURCE_PREFIX  // "/.ibmxspres/" 
                                                + ExtlibResourceProvider.EXTLIB_PREFIX;     // ".extlib" 
    
    public ExtlibResourceProvider() {
        super(EXTLIB_PREFIX);
    }
    @Override
    protected boolean shouldCacheResources() {
        return !ExtLibUtil.isDevelopmentMode();
    }

    @Override
    protected URL getResourceURL(HttpServletRequest request, String name) {
        List<ExtLibLoaderExtension> extensions = ExtLibLoaderExtension.getExtensions();
        int size = extensions.size();
        for(int i=0; i<size; i++) {
            URL url = extensions.get(i).getResourceURL(request,name);
            if(url!=null) {
                return url;
            }
        }
        return null;
    }
}
