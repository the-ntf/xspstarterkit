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






package com.ibm.domino.services.rest.das.view;

import com.ibm.domino.services.ServiceException;



/**
 * Domino View Parameters.
 */
public interface RestViewColumn {

    public String getName();
    public String getColumnName();
    
    public Object evaluate(RestViewService service, RestViewEntry entry) throws ServiceException;
}
