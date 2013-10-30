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






package com.ibm.domino.commons.model;

import java.util.List;

import lotus.domino.Database;

/**
 * Interface for managing access to mail and calendar features.
 */
public interface IDelegateProvider {

    public static final String DEFAULT_DELEGATE_NAME = "default"; // $NON-NLS-1$
    
    public Delegate get(Database database, String name) throws ModelException;
    
    public void set(Database database, Delegate delegate) throws ModelException;
    
    public void add(Database database, Delegate delegate) throws ModelException;
    
    public void delete(Database database, String name) throws ModelException;
    
    public List<Delegate> getList(Database database) throws ModelException;
    
    public DelegateAccess getEffectiveAccess(Database database) throws ModelException;
}
