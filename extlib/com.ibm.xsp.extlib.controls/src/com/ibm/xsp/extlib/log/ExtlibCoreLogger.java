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






/*
* Author: Maire Kehoe (mkehoe@ie.ibm.com)
* Date: 29 Jul 2011
* ExtlibCoreLogger.java
*/
package com.ibm.xsp.extlib.log;

import com.ibm.commons.log.Log;
import com.ibm.commons.log.LogMgr;

/**
 *
 * @author Maire Kehoe (mkehoe@ie.ibm.com)
 * @author Andrejus Chaliapinas
 */
public class ExtlibCoreLogger extends Log {

    /**
     * Log group for problems in the controls in the /extlib-data.xsp-config file, or their renderers.
     * [Individual log groups may be toggled to a more verbose logging level, 
     * to give finer debugging logging, rather than the default level of info.]
     */
    public static final LogMgr COMPONENT_DATA = load("com.ibm.xsp.extlib.component.data"); //$NON-NLS-1$

    /**
     * Log group for relational support controls
     */
    public static final LogMgr RELATIONAL = load("com.ibm.xsp.extlib.relational"); //$NON-NLS-1$
    
    
    /**
     * Log group for the sbt plugin
     */
    
    public static final LogMgr SBT = load("com.ibm.xsp.extlib.sbt"); //$NON-NLS-1$
}
