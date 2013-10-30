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







package com.ibm.domino.services.rest.das.databasecollection;



/**
 * Domino Database Collection Parameters Delegate..
 */
public class DatabaseCollectionParametersDelegate implements DatabaseCollectionParameters {
    
    private DatabaseCollectionParameters delegate;

    protected DatabaseCollectionParametersDelegate(DatabaseCollectionParameters delegate) {
        this.delegate = delegate;
    }
    
    public DatabaseCollectionParameters getDelegate() {
        return delegate;
    }

    public String getContentType() {
        return delegate.getContentType();
    }

    public String getDatabaseName() {
        return delegate.getDatabaseName();
    }
    
    public boolean isCompact() {
        return delegate.isCompact();
    }


}
