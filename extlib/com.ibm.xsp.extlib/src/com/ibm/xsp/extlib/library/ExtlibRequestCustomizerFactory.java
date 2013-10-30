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






package com.ibm.xsp.extlib.library;

import javax.faces.context.FacesContext;

import com.ibm.xsp.context.RequestCustomizerFactory;
import com.ibm.xsp.context.RequestParameters;

/**
 * Extlib library contributor.
 * @author Philippe Riand
 */
public class ExtlibRequestCustomizerFactory extends RequestCustomizerFactory {

    @Override
    public void initializeParameters(FacesContext context, RequestParameters parameter) {
        parameter.setResourcesProvider(ExtlibRequestCustomizer.instance);
    }
}
