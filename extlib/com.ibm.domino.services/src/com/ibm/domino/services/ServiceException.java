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






package com.ibm.domino.services;

import com.ibm.commons.util.AbstractException;

public class ServiceException extends AbstractException {
    
    private static final long serialVersionUID = 1L;
    private final ResponseCode rc;
    
    public ServiceException(Throwable t) {
        super(t);
        rc = ResponseCode.INTERNAL_ERROR;
    }

    public ServiceException(Throwable t, ResponseCode rc) {
        super(t);
        this.rc = rc;
    }
    
    public ServiceException(Throwable t, ResponseCode rc, String msg, Object... params) {
        super(t, msg, params);
        this.rc = rc;
    }
    
    public ServiceException(Throwable t, String msg, Object... params) {
        super(t, msg, params);
        rc = ResponseCode.BAD_REQUEST;
    }

    public ResponseCode getResponseCode() {
        return rc;
    }

//  /**
//   * Get the message from the exception. If the wrapped exception is a
//   * NotesException then this function returns the text of the notes
//   * exception.
//   */
//  public String getMessage() {
//      final Throwable cause = this.getCause();
//      if (cause != null) {
//          if (cause instanceof NotesException) {
//              return ((NotesException) cause).text;
//          } else {
//              return cause.getMessage();
//          }
//      } else {
//          return super.getMessage();
//      }
//  }

}
