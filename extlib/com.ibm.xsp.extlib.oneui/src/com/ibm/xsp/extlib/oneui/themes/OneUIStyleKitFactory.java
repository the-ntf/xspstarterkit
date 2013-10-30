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






package com.ibm.xsp.extlib.oneui.themes;

import java.io.InputStream;

import com.ibm.xsp.stylekit.StyleKitFactory;


/**
 * OneUI V2 Theme Factory.
 * @author Philippe Riand
 * @author Tony McGuckin
 */
public class OneUIStyleKitFactory implements StyleKitFactory {

    public OneUIStyleKitFactory() {
    }
    
    //@Override
    public InputStream getThemeAsStream(String themeId, int scope) {
        // No global themes are contributed
        return null;
    }

    //@Override
    public InputStream getThemeFragmentAsStream(String themeId, int scope) {
        // Contribute the mobile themes at a global level
        if(scope==StyleKitFactory.STYLEKIT_GLOBAL) {
            String folderPath = "com/ibm/xsp/extlib/oneui/themes/"; //$NON-NLS-1$
            if(themeId.equals("oneuiv3.0")){ //$NON-NLS-1$
                return getThemeFromBundle(folderPath+"oneuiv3_extlib.theme"); //$NON-NLS-1$
            } else if(themeId.startsWith("oneuiv3.0.2")){ //$NON-NLS-1$
                return getThemeFromBundle(folderPath+"oneuiv3.0.2_extlib.theme"); //$NON-NLS-1$
            } else if(themeId.startsWith("oneui")) { //$NON-NLS-1$
                return getThemeFromBundle(folderPath+"oneui_extlib.theme"); //$NON-NLS-1$
            }
        }
        return null;
    }

    private InputStream getThemeFromBundle(final String fileName) {
        // The class loader doesn't require the security manager to be enabled...
        // But this requires the bundle to be packaged as a single jar plug-in
        ClassLoader cl = getClass().getClassLoader();
        return cl.getResourceAsStream(fileName);
        
//      // We have to read the theme within a privileged block as the security manager
//      // will not let us access the bundle.
//        return AccessController.doPrivileged(new PrivilegedAction<InputStream>() {
//            public InputStream run() {
//              try {
//                  URL url = ExtLibCompUtil.getResourceURL(ExtlibActivator.instance.getBundle(), fileName);
//                  if(url!=null) {
//                      return url.openStream();
//                  }
//              } catch(Throwable ex) {
//                  Platform.getInstance().log(ex);
//              }                
//              return null;
//            }
//        });
    }
}
