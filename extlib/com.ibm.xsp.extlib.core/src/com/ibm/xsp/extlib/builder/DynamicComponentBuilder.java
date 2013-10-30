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


/**
 * Dynamic builder identification.
 * <p>
 * This inteface should be implemented by a component builder which can return
 * a source reference for the component.
 * Note: This is for very advanced use cases, like an XPages interpreter
 * </p>
 */
public interface DynamicComponentBuilder {

    public String getSourceComponentRef();
}
