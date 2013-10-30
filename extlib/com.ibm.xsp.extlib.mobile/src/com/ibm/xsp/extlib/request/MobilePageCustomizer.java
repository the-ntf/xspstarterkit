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






package com.ibm.xsp.extlib.request;

import javax.faces.context.FacesContext;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.context.RequestParameters;

/**
 * Basic Mobile request customizer.
 * 
 * @author Philippe Riand
 * @author tony.mcguckin@ie.ibm.com
 */
public class MobilePageCustomizer implements RequestParameters.RunningContextProvider {

    public static final boolean USE_MOBILE_THEME = false;
    
    private FacesContext context;
    
    public MobilePageCustomizer(FacesContext context, RequestParameters parameters) {
        this.context = context;
    }
    
    public FacesContext getFacesContext() {
        return context;
    }

    public boolean isRunningContext(String context) {
        if(StringUtil.equals(context, MobileConstants.MOBILE_CONTEXT)) {
            return true;
        }
        return false;
    }
     
}
          
