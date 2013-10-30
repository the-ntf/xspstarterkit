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

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_CONFLICT;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_GONE;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED;
import static javax.servlet.http.HttpServletResponse.SC_MOVED_PERMANENTLY;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_NOT_IMPLEMENTED;
import static javax.servlet.http.HttpServletResponse.SC_NOT_MODIFIED;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public enum ResponseCode {
    
    UNINITIALIZED(SC_INTERNAL_SERVER_ERROR, com.ibm.domino.services.ResourceHandler.getSpecialAudienceString("ResponseCode.InternalError")), // $NLX-ResponseCode.InternalError-1$
    BAD_REQUEST(SC_BAD_REQUEST, com.ibm.domino.services.ResourceHandler.getSpecialAudienceString("ResponseCode.BadRequest")), // $NLX-ResponseCode.BadRequest-1$
    CONFLICT(SC_CONFLICT, com.ibm.domino.services.ResourceHandler.getSpecialAudienceString("ResponseCode.Conflict")), // $NLX-ResponseCode.Conflict-1$
    DEPRECATED_URI(SC_MOVED_PERMANENTLY, com.ibm.domino.services.ResourceHandler.getSpecialAudienceString("ResponseCode.DeprecatedURI")), // $NLX-ResponseCode.DeprecatedURI-1$
    METHOD_NOT_ALLOWED(SC_METHOD_NOT_ALLOWED, com.ibm.domino.services.ResourceHandler.getSpecialAudienceString("ResponseCode.Methodnotallowed")), // $NLX-ResponseCode.Methodnotallowed-1$
    INTERNAL_ERROR(SC_INTERNAL_SERVER_ERROR, com.ibm.domino.services.ResourceHandler.getSpecialAudienceString("ResponseCode.InternalError.1")), // $NLX-ResponseCode.InternalError.1-1$
    NOT_IMPLEMENTED(SC_NOT_IMPLEMENTED, com.ibm.domino.services.ResourceHandler.getSpecialAudienceString("ResponseCode.Notimplemented")), // $NLX-ResponseCode.Notimplemented-1$
    RSRC_GONE(SC_GONE, com.ibm.domino.services.ResourceHandler.getSpecialAudienceString("ResponseCode.Gone")), // $NLX-ResponseCode.Gone-1$
    RSRC_NOT_FOUND(SC_NOT_FOUND, com.ibm.domino.services.ResourceHandler.getSpecialAudienceString("ResponseCode.Notfound")), // $NLX-ResponseCode.Notfound-1$
    RSRC_UNCHANGED(SC_NOT_MODIFIED, com.ibm.domino.services.ResourceHandler.getSpecialAudienceString("ResponseCode.Notmodified")), // $NLX-ResponseCode.Notmodified-1$
    FORBIDDEN(SC_FORBIDDEN, com.ibm.domino.services.ResourceHandler.getSpecialAudienceString("ResponseCode.Forbidden")), // $NLX-ResponseCode.Forbidden-1$
    UNAUTHORIZED(SC_UNAUTHORIZED, com.ibm.domino.services.ResourceHandler.getSpecialAudienceString("ResponseCode.NotAuthorized")), // $NLX-ResponseCode.NotAuthorized-1$
    RSRC_CREATED(SC_CREATED, com.ibm.domino.services.ResourceHandler.getSpecialAudienceString("ResponseCode.Created")), // $NLX-ResponseCode.Created-1$
    OK(SC_OK, com.ibm.domino.services.ResourceHandler.getSpecialAudienceString("ResponseCode.OK")); // $NLX-ResponseCode.OK-1$
    
    public final int httpStatusCode;
    public final String httpStatusText;
    
    ResponseCode(final int httpStatusCode, final String httpStatusText) {
        this.httpStatusCode = httpStatusCode;
        this.httpStatusText = httpStatusText;
    }
}
