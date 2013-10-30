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
* Date: 22 May 2012
* ExtlibDominoLogger.java
*/
package com.ibm.xsp.extlib.domino;

import com.ibm.commons.log.Log;
import com.ibm.commons.log.LogMgr;

/**
 * 
 * @author Maire Kehoe (mkehoe@ie.ibm.com)
 */
public class ExtlibDominoLogger extends Log {
    public static final LogMgr DOMINO = load("com.ibm.xsp.extlib.domino"); //$NON-NLS-1$
}
