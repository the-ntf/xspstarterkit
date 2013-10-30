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
import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.context.RequestParameters;

/**
 * Android request customizer.
 * 
 * @author Philippe Riand
 * @author tony.mcguckin@ie.ibm.com
 */
public class AndroidPageCustomizer extends MobilePageCustomizer {

    public AndroidPageCustomizer(FacesContext context, RequestParameters parameters) {
        super(context, parameters);

        // WARN: we cannot read the property from FacesContext as goes to
        // infinite
        // recursion, because FacesContext also relies on the request parameters
        // -> we directly read the property from the application object, which
        // can be then set at either the application or server level.
        ApplicationEx app = ApplicationEx.getInstance(context);
        String s = app.getApplicationProperty(MobileConstants.XSP_THEME_MOBILE_ANDROID, null);

        if (StringUtil.isNotEmpty(s)) {
            parameters.setProperty(MobileConstants.XSP_THEME_WEB, s);
        }
        else {
            s = app.getApplicationProperty(MobileConstants.XSP_THEME_MOBILE, AndroidConstants.ANDROID_THEME_NAME);
            parameters.setProperty(MobileConstants.XSP_THEME_WEB, s);
        }
    }

    @Override
    public boolean isRunningContext(String context) {
        if (StringUtil.equals(context, AndroidConstants.ANDROID_CONTEXT)) {
            return true;
        }
        return super.isRunningContext(context);
    }
}
