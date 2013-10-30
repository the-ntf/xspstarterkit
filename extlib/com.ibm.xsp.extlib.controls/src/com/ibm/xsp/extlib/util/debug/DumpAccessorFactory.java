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






package com.ibm.xsp.extlib.util.debug;


/**
 * Return an object accessor for an object.
 */
public interface DumpAccessorFactory {

    /**
     * Get an object accessor for an object.
     * @param o the object to return the accessor to
     * @return
     */
    public DumpAccessor find(DumpContext dumpContext, Object o);
}
