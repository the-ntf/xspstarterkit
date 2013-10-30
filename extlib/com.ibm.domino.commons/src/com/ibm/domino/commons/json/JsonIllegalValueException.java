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






package com.ibm.domino.commons.json;

/**
 * Unchecked exception thrown when parsing a JSON value.
 */
public class JsonIllegalValueException extends RuntimeException {
    
    public JsonIllegalValueException(Throwable t) {
        super(t);
    }
    
    public JsonIllegalValueException(String message) {
        super(message);
    }

}
