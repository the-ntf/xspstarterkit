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






package com.ibm.xsp.extlib.config;

/**
 * @author Andrejus Chaliapinas
 *
 */
public class MobileConfig extends ExtlibPluginConfig {
    public MobileConfig() {
    }
    
    
    @Override
    public String[] getFacesConfigFiles(String[] files) {
        return concat(files, new String[] {
                "com/ibm/xsp/extlib/config/extlib-mobile-faces-config.xml", // $NON-NLS-1$
      });
    }   

    @Override
    public String[] getXspConfigFiles(String[] files) {
        return concat(files, new String[] {
                "com/ibm/xsp/extlib/config/extlib-mobile.xsp-config", // $NON-NLS-1$
        });
    }
}
