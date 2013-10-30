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






package com.ibm.domino.services;

import com.ibm.commons.log.Log;
import com.ibm.commons.log.LogMgr;

public class Loggers extends Log {
   
    //Logger for Domino Services project
    public static final LogMgr SERVICES_LOGGER = load( "com.ibm.domino.services" ); // $NON-NLS-1$
        
}
