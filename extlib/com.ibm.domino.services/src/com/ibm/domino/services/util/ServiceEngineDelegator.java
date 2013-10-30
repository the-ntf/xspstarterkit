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






package com.ibm.domino.services.util;

import com.ibm.domino.services.HttpServiceEngine;



/**
 * Service Engine Delegator.
 * 
 * This provides the basic HTTP service required by REST services implementations. 
 */
public class ServiceEngineDelegator {
    
    private HttpServiceEngine delegate;
    
    public ServiceEngineDelegator(HttpServiceEngine delegate) {
    }
    
    public HttpServiceEngine getDelegate() {
        return delegate;
    }
    
    public void processRequest() {
        delegate.processRequest();
    }
}
