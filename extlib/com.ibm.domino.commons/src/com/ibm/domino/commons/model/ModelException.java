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

/**
 * Generic exception for the com.ibm.domino.commons.model package
 */
public class ModelException extends Exception {

    public static final int ERR_GENERAL = 0;
    public static final int ERR_CONFLICT = 1;
    public static final int ERR_INVALID_INPUT = 2;
    public static final int ERR_NOT_FOUND = 3;
    public static final int ERR_NOT_ALLOWED = 4;

    private int _code = ERR_GENERAL;
    
    public ModelException(String message) {
        super(message);
    }

    public ModelException(String message, int code) {
        super(message);
        _code = code;
    }

    public ModelException(String message, Throwable t) {
        super(message, t);
    }
    
    public int getCode() {
        return _code;
    }
}
