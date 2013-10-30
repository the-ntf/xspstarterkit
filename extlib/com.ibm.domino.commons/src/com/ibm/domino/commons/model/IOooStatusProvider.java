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






package com.ibm.domino.commons.model;

import lotus.domino.Database;

/**
 * Interface for getting and setting Out of Office status. 
 */
public interface IOooStatusProvider {
    
    public OooStatus get(Database database) throws ModelException;
    
    public void put(Database database, OooStatus oooStatus) throws ModelException;

    /**
     * Frees any resources associated with this provider.
     * 
     * <p>The provider assumes this method is called only once at the end of the
     * container's lifecycle.  The container is responsible for making sure that
     * is the case.  Also, after calling this method, the container should not call 
     * other provider methods (e.g. get or put). 
     */
    public void destroy();
}
