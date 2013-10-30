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







package com.ibm.domino.services.rest.das.document;

import java.util.List;

import com.ibm.domino.services.ServiceException;


/**
 * Domino Document Parameters Delegate..
 */
public class DocumentParametersDelegate implements DocumentParameters {
    
    private DocumentParameters delegate;

    protected DocumentParametersDelegate(DocumentParameters delegate) {
        this.delegate = delegate;
    }
    
    public DocumentParameters getDelegate() {
        return delegate;
    }

    public boolean isIgnoreRequestParams() {
        return delegate.isIgnoreRequestParams();
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
    
    public int getSystemItems() {
        return delegate.getSystemItems();
    }
    
    public int getGlobalValues() {
        return delegate.getGlobalValues();
    }
    
    public String getVar() {
        return delegate.getVar();
    }

    public boolean isDefaultItems() {
        return delegate.isDefaultItems();
    }
    
    public List<RestDocumentItem> getItems() {
        return delegate.getItems();
    }
    
    public String getDocumentUnid() {
        return delegate.getDocumentUnid();
    }
    
    public String getParentId() {
        return delegate.getParentId();
    }
        
    public boolean isComputeWithForm () {
        return delegate.isComputeWithForm();
    }
    
    public String getFormName () {
        return delegate.getFormName();
    }
    
    public boolean isMarkRead() {
        return delegate.isMarkRead();
    }
    
    public String getSince() {
        return delegate.getSince();
    }
    
    public String getSearch() {
        return delegate.getSearch();
    }
    
    public int getSearchMaxDocs() throws ServiceException {
        return delegate.getSearchMaxDocs();
    }       

    public boolean isStrongType() {
        return delegate.isStrongType();
    }

}
