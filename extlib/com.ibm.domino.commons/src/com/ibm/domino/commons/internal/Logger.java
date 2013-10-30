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






package com.ibm.domino.commons.internal;

import com.ibm.commons.log.Log;
import com.ibm.commons.log.LogMgr;

/**
 * Internal logger
 */
public class Logger {

    private static final LogMgr LOGGER = Log.load("com.ibm.domino.commons");  //$NON-NLS-1$
    
    public static LogMgr get() {
        return LOGGER;
    }
}
