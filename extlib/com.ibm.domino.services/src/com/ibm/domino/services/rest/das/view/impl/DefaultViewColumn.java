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






package com.ibm.domino.services.rest.das.view.impl;

import com.ibm.domino.services.ServiceException;
import com.ibm.domino.services.rest.das.view.RestViewColumn;
import com.ibm.domino.services.rest.das.view.RestViewEntry;
import com.ibm.domino.services.rest.das.view.RestViewService;


/**
 * Domino View Service.
 */
public class DefaultViewColumn implements RestViewColumn {

    protected String name;
    protected String columnName;
    
    public DefaultViewColumn() {
    }
    
    public DefaultViewColumn(String name) {
        this.name = name;
    }
    
    public DefaultViewColumn(String name, String columnName) {
        this.name = name;
        this.columnName = columnName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    
    public Object evaluate(RestViewService service, RestViewEntry entry) throws ServiceException {
        return null;
    }
    
}
