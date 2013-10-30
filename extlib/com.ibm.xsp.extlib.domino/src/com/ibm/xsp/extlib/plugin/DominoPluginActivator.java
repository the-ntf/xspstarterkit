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






package com.ibm.xsp.extlib.plugin;

import org.eclipse.core.runtime.Plugin;

/**
 * @author Andrejus Chaliapinas
 *
 */
public class DominoPluginActivator extends Plugin {

    public static DominoPluginActivator instance;
    
    public DominoPluginActivator() {
        instance = this;
    }
}
