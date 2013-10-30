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





package com.ibm.xsp.extlib.builder;

import com.ibm.xsp.page.FacesPageDriver;


/**
 * Dynamic component factory.
 * <p>
 * This interface should be implemented by the component which dynamically construct
 * XPages components, without relying on the compiled pages, but on its own mechanism
 * for constructing the hierarchy. 
 * Note: This is for very advanced use cases, like an XPages interpreter
 * </p>
 */
public interface DynamicComponentFactory {

    /**
     * Get the FacesPageDriver involved.
     * @return
     */
    public FacesPageDriver getPageDriver();
    
    /**
     * Get a string that identifies a source for code.
     * @return
     */
    public String getSourceComponentRef();
}
