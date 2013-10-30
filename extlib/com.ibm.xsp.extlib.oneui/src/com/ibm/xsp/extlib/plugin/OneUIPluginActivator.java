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

import com.ibm.xsp.extlib.minifier.ExtLibLoaderExtension;
import com.ibm.xsp.extlib.minifier.OneUILoader;

/**
 * @author Andrejus Chaliapinas
 *
 */
public class OneUIPluginActivator extends Plugin {

    public static OneUIPluginActivator instance;
    
    public OneUIPluginActivator() {
        instance = this;
        
        ExtLibLoaderExtension.getExtensions().add(new OneUILoader());
    }
}
