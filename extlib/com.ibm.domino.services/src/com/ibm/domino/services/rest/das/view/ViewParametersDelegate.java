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

import java.util.List;

import com.ibm.domino.services.ServiceException;

/**
 * Domino View Parameters Delegate..
 */
public class ViewParametersDelegate implements ViewParameters {
    
    private ViewParameters delegate;

    protected ViewParametersDelegate(ViewParameters delegate) {
        this.delegate = delegate;
    }
    
    public ViewParameters getDelegate() {
        return delegate;
    }

    public boolean isIgnoreRequestParams() {
        return delegate.isIgnoreRequestParams();
    }

    public int getStart() {
        return delegate.getStart();
    }

    public int getCount() {
        return delegate.getCount();
    }

    public String getCategoryFilter() {
        return delegate.getCategoryFilter();
    }

    public List<RestViewColumn> getColumns() {
        return delegate.getColumns();
    }

    public String getContentType() {
        return delegate.getContentType();
    }

    public String getDatabaseName() {
        return delegate.getDatabaseName();
    }

    public int getExpandLevel() {
        return delegate.getExpandLevel();
    }

    public int getGlobalValues() {
        return delegate.getGlobalValues();
    }

    public Object getKeys() {
        return delegate.getKeys();
    }

    public String getParentId() {
        return delegate.getParentId();
    }

    public String getSearch() {
        return delegate.getSearch();
    }

    public int getSearchMaxDocs() throws ServiceException {
        return delegate.getSearchMaxDocs();
    }

    public String getSortColumn() {
        return delegate.getSortColumn();
    }

    public String getSortOrder() {
        return delegate.getSortOrder();
    }

    public Object getStartKeys() {
        return delegate.getStartKeys();
    }

    public int getSystemColumns() {
        return delegate.getSystemColumns();
    }

    public String getVar() {
        return delegate.getVar();
    }

    public String getViewName() {
        return delegate.getViewName();
    }

    public boolean isCompact() {
        return delegate.isCompact();
    }

    public boolean isDefaultColumns() {
        return delegate.isDefaultColumns();
    }

    public boolean isKeysExactMatch() {
        return delegate.isKeysExactMatch();
    }
        
    public boolean isComputeWithForm () {
        return delegate.isComputeWithForm();
    }
    
    public String getFormName () {
        return delegate.getFormName();
    }

}
