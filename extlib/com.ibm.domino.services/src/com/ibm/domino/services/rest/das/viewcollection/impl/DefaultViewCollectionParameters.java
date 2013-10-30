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






package com.ibm.domino.services.rest.das.viewcollection.impl;

import com.ibm.domino.services.rest.das.viewcollection.ViewCollectionParameters;

/**
 * Domino Database Service.
 */

public class DefaultViewCollectionParameters implements ViewCollectionParameters {
        
    protected boolean                   compact;
    protected String                    contentType;
    protected String                    databaseName;

    
    public DefaultViewCollectionParameters() {
    }

    public boolean isCompact() {
        return compact;
    }
    public void setCompact(boolean compact) {
        this.compact = compact;
    }
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public String getDatabaseName() {
        return databaseName;
    }
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
