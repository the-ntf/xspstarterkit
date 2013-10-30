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





package com.ibm.xsp.extlib;

import java.security.AccessController;
import java.security.PrivilegedAction;

/** Resource handler class 
 * @ibm-not-published
 * **/

public class ResourceHandler {
    private static ExtLibResourceHandlerImpl s_impl;
    private static ExtLibResourceHandlerImpl impl(){
        if (s_impl == null) {
            s_impl = AccessController.doPrivileged(new PrivilegedAction<ExtLibResourceHandlerImpl>() {
                public ExtLibResourceHandlerImpl run() {
                    // privileged code goes here:
                    return new ExtLibResourceHandlerImpl(ResourceHandler.class, 
                            "messages", //$NON-NLS-1$
                            "extlib", //$NON-NLS-1$
                            "specialAudience"); //$NON-NLS-1$
                }
            });
        }
        return s_impl;
    }
    public static String getLoggingString(String key) {
        return impl().getLoggingString(key);
    }
    public static String getString(String key) {
        return impl().getString(key);
    }
    public static String getSpecialAudienceString(String key) {
        return impl().getSpecialAudienceString(key);
    }
    
}
