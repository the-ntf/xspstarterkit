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
public class DominoConfig extends ExtlibPluginConfig {
    public DominoConfig() {
    }

    
    // ===============================================================
    //  Compose the lists of extra config files 
    // ===============================================================
    
    @Override
    public String[] getXspConfigFiles(String[] files) {
        return concat(files, new String[] {
            "com/ibm/xsp/extlib/config/extlib-domino-outline.xsp-config", // $NON-NLS-1$
            "com/ibm/xsp/extlib/config/extlib-domino-picker.xsp-config", // $NON-NLS-1$
            "com/ibm/xsp/extlib/config/extlib-domino-tagcloud.xsp-config", // $NON-NLS-1$
            "com/ibm/xsp/extlib/config/extlib-domino-rest.xsp-config", // $NON-NLS-1$
            "com/ibm/xsp/extlib/config/dwa-calendar.xsp-config", // $NON-NLS-1$
            "com/ibm/xsp/extlib/config/dwa-listview.xsp-config"// $NON-NLS-1$
        });
    }
    
    @Override
    public String[] getFacesConfigFiles(String[] files) {
        return concat(files, new String[] {
            "com/ibm/xsp/extlib/config/dwa-calendar-faces-config.xml", // $NON-NLS-1$
            "com/ibm/xsp/extlib/config/dwa-listview-faces-config.xml" // $NON-NLS-1$
       });
    }
}
