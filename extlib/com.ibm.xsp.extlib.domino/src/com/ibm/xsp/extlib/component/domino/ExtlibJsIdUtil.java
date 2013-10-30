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





/*
* Author: Maire Kehoe (mkehoe@ie.ibm.com)
* Date: 19 Sep 2011
* ExtlibJsIdUtil.java
*/

package com.ibm.xsp.extlib.component.domino;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.util.FacesUtil;

/**
 *
 * @author Maire Kehoe (mkehoe@ie.ibm.com)
 */
public class ExtlibJsIdUtil {

    public static String findDojoWidgetId(FacesContext context, UIComponent from, String componentId) {
        if(StringUtil.isNotEmpty(componentId)) {
            UIComponent sc = FacesUtil.getComponentFor(from, componentId);
            if( null == sc ){
                return null;
            }
            if(!(sc instanceof FacesExtlibJsIdWidget)) {
                Object jsId = sc.getAttributes().get("jsId"); //$NON-NLS-1$
                if( jsId instanceof String ){
                    return (String)jsId;
                }
            }
            return ((FacesExtlibJsIdWidget)sc).getDojoWidgetJsId(context);
        }
        return null;
    }

    /**
     * @param context
     * @return
     */
    public static String getClientIdAsJsId(UIComponent component, FacesContext context) {
        String jsId = component.getClientId(context);
        return jsId.replaceAll("[:.,-]", "_");
    }

}
