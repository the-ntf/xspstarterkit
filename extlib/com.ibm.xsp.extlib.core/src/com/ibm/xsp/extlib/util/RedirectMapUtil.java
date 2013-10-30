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






package com.ibm.xsp.extlib.util;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

/**
 * Management of a transient map that carries value when a redirect is emitted. 
 * <p>
 * Note that this is not yet a complete implementation of the flash scope, as
 * provided by JSF 2.0. This is a simple mechanism used to transmit values between
 * 2 requests even though a redirect request is emitted. 
 * </p>
 */
public class RedirectMapUtil {

    public static final String REDIRECTMAP_KEY_PUSH = "xsp.extlib.rdmapp"; // $NON-NLS-1$
    public static final String REDIRECTMAP_KEY_GET  = "xsp.extlib.rdmapg"; // $NON-NLS-1$
    
    /**
     * Add a value to the redirect map.
     * This should be done in either the InvokeApplication or Render phase.
     */
    @SuppressWarnings("unchecked") //$NON-NLS-1$
    public static void push(FacesContext context, String key, Object value) {
        Map<String, Map<?, ?>> sessionMap = context.getExternalContext().getSessionMap();
        Map<String, Object> redirectMap = (Map<String, Object>)sessionMap.get(REDIRECTMAP_KEY_PUSH);
        if(redirectMap==null) {
            redirectMap = new HashMap<String,Object>();
            sessionMap.put(REDIRECTMAP_KEY_PUSH,redirectMap);
        }
        redirectMap.put(key,value);
    }
    
    /**
     * Remove a value to the redirect map.
     * This should be done in either the InvokeApplication or Render phase.
     */
    @SuppressWarnings("unchecked") //$NON-NLS-1$
    public static void remove(FacesContext context, String key) {
        Map<String, Map<?, ?>> sessionMap = context.getExternalContext().getSessionMap();
        Map<?, ?> redirectMap = (Map<?, ?>)sessionMap.get(REDIRECTMAP_KEY_PUSH);
        if(redirectMap!=null) {
            redirectMap.remove(REDIRECTMAP_KEY_PUSH);
        }
    }
    
    /**
     * Get a value just pushed from the redirect map.
     */
    @SuppressWarnings("unchecked") //$NON-NLS-1$
    public static Object getPushed(FacesContext context, String key) {
        Map<String, Map<?, ?>> sessionMap = context.getExternalContext().getSessionMap();
        Map<?, ?> redirectMap = (Map<?, ?>)sessionMap.get(REDIRECTMAP_KEY_PUSH);
        if(redirectMap!=null) {
            return redirectMap.get(key);
        }
        return null;
    }
    
    /**
     * Get a value from the redirect map.
     */
    public static Object get(FacesContext context, String key) {
        Map<?, ?> requestMap = context.getExternalContext().getRequestMap();
        Map<?, ?> redirectMap = (Map<?, ?>)requestMap.get(REDIRECTMAP_KEY_GET);
        if(redirectMap!=null) {
            return redirectMap.get(key);
        }
        return null;
    }
}
