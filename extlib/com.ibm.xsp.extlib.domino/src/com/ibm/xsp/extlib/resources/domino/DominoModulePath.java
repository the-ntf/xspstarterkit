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






package com.ibm.xsp.extlib.resources.domino;

import com.ibm.xsp.resource.DojoModulePathResource;

/**
 * @author akosugi
 * 
 *        dwa dojo module path contributor
 */
public class DominoModulePath extends DojoModulePathResource {

    public DominoModulePath() {
        super("dwa", getDojoPath()); // $NON-NLS-1$
    }

    private static String getDojoPath(){
//          String dojoPath = context.getExternalContext().encodeResourceURL(
//                  "/.ibmxspres/.dwa");
//          String dojoPath = "/dojocal_2/dojo/dwa";
        String dojoPath = "/.ibmxspres/.dwa"; // $NON-NLS-1$
        return dojoPath;
    }

}
