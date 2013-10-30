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







package com.ibm.domino.services.rest.das.viewcollection;



/**
 * Domino View Collection Parameters Delegate..
 */
public class ViewCollectionParametersDelegate implements ViewCollectionParameters {
    
    private ViewCollectionParameters delegate;

    protected ViewCollectionParametersDelegate(ViewCollectionParameters delegate) {
        this.delegate = delegate;
    }
    
    public ViewCollectionParameters getDelegate() {
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
