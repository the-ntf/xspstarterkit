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






package com.ibm.xsp.extlib.resources.domino;

import java.net.URL;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.ibm.xsp.extlib.plugin.DominoPluginActivator;
import com.ibm.xsp.webapp.resources.BundleResourceProvider;

/**
 * @author akosugi
 * 
 *        register resource files to container
 */
public class DominoResourceProvider extends BundleResourceProvider {


    public DominoResourceProvider() {
        super(DominoPluginActivator.instance.getBundle(),DWA_PREFIX);
    }

    @Override
    protected URL getResourceURL(HttpServletRequest request, String name) {
        String path = "/resources/web/dwa/"+name; // $NON-NLS-1$
        int fileNameIndex = path.lastIndexOf('/');
        String fileName = path.substring(fileNameIndex+1);
        path = path.substring(0, fileNameIndex+1);
        // see http://www.osgi.org/javadoc/r4v42/org/osgi/framework/Bundle.html
        //  #findEntries%28java.lang.String,%20java.lang.String,%20boolean%29
        Enumeration<?> urls = getBundle().findEntries(path, fileName, false/*recursive*/);
        if( null != urls && urls.hasMoreElements() ){
            URL url = (URL) urls.nextElement();
            if( null != url ){
                return url;
            }
        }
        return null; // no match, 404 not found.
    }

    public static final String DWA_PREFIX = ".dwa"; // $NON-NLS-1$
    public static final String RESOURCE_PATH = "/.ibmxspres/.dwa/"; // $NON-NLS-1$
    public static final String DOJO_PATH = "/.ibmxspres/.dwa"; // $NON-NLS-1$

}
