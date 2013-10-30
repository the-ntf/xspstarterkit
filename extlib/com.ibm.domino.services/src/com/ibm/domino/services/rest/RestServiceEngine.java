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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.domino.services.HttpServiceEngine;


/**
 * Rest Service Engine.
 */
public abstract class RestServiceEngine extends HttpServiceEngine {
    
    public RestServiceEngine(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(httpRequest,httpResponse);
    }
    
    public abstract RestServiceParameters getParameters();
}
