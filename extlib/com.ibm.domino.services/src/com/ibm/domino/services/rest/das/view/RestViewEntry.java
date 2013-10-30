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
public interface RestViewEntry {

    // Access to System column
    public abstract String getUniversalId() throws ServiceException;
    public abstract String getNoteId() throws ServiceException;
    public abstract String getPosition() throws ServiceException;
    public abstract boolean getRead() throws ServiceException;
    public abstract int getSiblings() throws ServiceException;
    public abstract int getDescendants() throws ServiceException;
    public abstract int getChildren() throws ServiceException;
    public abstract int getIndent() throws ServiceException;
    
    // Column values
    public abstract int getColumnCount() throws ServiceException;
    public abstract String getColumnName(int index) throws ServiceException;
    public abstract Object getColumnValue(int index) throws ServiceException;
    public abstract Object getColumnValue(String name) throws ServiceException;

    // Other attributes
    public abstract boolean isDocument() throws ServiceException;
}
