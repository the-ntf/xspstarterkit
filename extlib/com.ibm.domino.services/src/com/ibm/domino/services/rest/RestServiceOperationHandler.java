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






package com.ibm.domino.services.rest;

import com.ibm.domino.services.ServiceException;


/**
 * Handler for multiple operations.
 */
public abstract class RestServiceOperationHandler implements RestServiceOperation {
    
    protected Object content;  // the request content
    
    
    public RestServiceOperationHandler(Object content) {
        super();
        this.content = content;
    }

    public abstract void run() throws ServiceException;
    
    public Object getContent() {
        return content;
    }
    
}
