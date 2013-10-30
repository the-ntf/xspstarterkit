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





package com.ibm.domino.commons;

import java.util.Locale;

/**
 * Generalized request context.
 * 
 * <p>This class holds variables specific to this thread.  It is similar to a FacesContext
 * but is used when a FacesContext isn't available -- for example, in the context of
 * a REST request.
 */
public class RequestContext {
    
    private static ThreadLocal<RequestContext> t_context = new ThreadLocal<RequestContext>();
    
    private Locale _userLocale = null;
    
    private RequestContext() {
    }
    
    public static RequestContext getCurrentInstance() {
        RequestContext ctx = t_context.get();
        
        if ( ctx == null ) {
            ctx = new RequestContext();
            t_context.set(ctx);
        }
        
        return ctx;
    }
    
    public Locale getUserLocale() {
        return _userLocale;
    }
    
    public void setUserLocale(Locale locale) {
        _userLocale = locale;
    }

}
