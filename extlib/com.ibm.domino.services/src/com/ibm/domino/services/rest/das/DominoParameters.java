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






package com.ibm.domino.services.rest.das;

import com.ibm.domino.services.rest.RestServiceParameters;


/**
 * Domino Based Service parameters.
 */
public interface DominoParameters extends RestServiceParameters {

    // Access to the view
    public String getDatabaseName();
}
