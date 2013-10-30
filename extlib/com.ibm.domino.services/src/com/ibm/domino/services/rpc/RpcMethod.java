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






package com.ibm.domino.services.rpc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * Remote Method interface.
 * 
 * @author Philippe Riand
 */
public interface RpcMethod {

    public String getName();
    
    public List<RpcArgument> getArguments();
    
    public Object invoke(HttpServletRequest request, int id, Object params) throws Exception;
}
