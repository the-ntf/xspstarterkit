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
 * Extension library plugin's extension point to provide config files through.
 * 
 * @author Andrejus Chaliapinas
 *
 */
public abstract class ExtlibPluginConfig {
    
    /**
     * Not used anymore
     */
//    public static final String EXTENSION_NAME = "com.ibm.xsp.extlib.plugin.config"; // $NON-NLS-1$
    
    /**
     * @deprecated, unused
     */
    public void installResources() {
    }
    
    public String[] getXspConfigFiles(String[] files) {
        return files;
    }
    
    public String[] getFacesConfigFiles(String[] files) {
        return files;
    }
    
    public static String[] concat(String[] s1, String[] s2) {
        String[] s = new String[s1.length+s2.length];
        System.arraycopy(s1, 0, s, 0, s1.length);
        System.arraycopy(s2, 0, s, s1.length, s2.length);
        return s;
    }
}
