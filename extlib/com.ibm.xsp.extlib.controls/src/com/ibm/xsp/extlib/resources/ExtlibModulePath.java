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






package com.ibm.xsp.extlib.resources;

import com.ibm.xsp.resource.DojoModulePathResource;

/**
 *
 * @author Maire Kehoe (mkehoe@ie.ibm.com)
 * 23 Feb 2010
 */
public class ExtlibModulePath extends DojoModulePathResource {

    public ExtlibModulePath() {
        super("extlib", "/.ibmxspres/.extlib"); // $NON-NLS-1$ $NON-NLS-2$
    }
}
