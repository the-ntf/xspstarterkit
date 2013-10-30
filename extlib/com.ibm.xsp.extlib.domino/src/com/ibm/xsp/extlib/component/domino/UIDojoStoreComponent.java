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





package com.ibm.xsp.extlib.component.domino;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.ibm.commons.util.StringUtil;

public abstract class UIDojoStoreComponent extends UIComponentBase implements FacesExtlibJsIdWidget {

    private String jsId;
    //accessibility properties//
    private String storeTitle = null;
    
    /**
     * 
     */
    public UIDojoStoreComponent() {
        super();
    }
    
    public String getJsId() {
        if (jsId != null)
            return jsId;
        ValueBinding vb = getValueBinding("jsId"); // $NON-NLS-1$
        if (vb != null)
            return (String) vb.getValue(getFacesContext());
        else
            return null;
    }

    public void setJsId(String jsid) {
        this.jsId = jsid;
    }
    public String getDojoWidgetJsId(FacesContext context) {
        String jsId = getJsId();
        if(StringUtil.isNotEmpty(jsId)) {
            return jsId;
        }
        return ExtlibJsIdUtil.getClientIdAsJsId(this,context);
    }

    public String getStoreTitle() {
        if (storeTitle != null)
            return storeTitle;
        ValueBinding vb = getValueBinding("storeTitle"); // $NON-NLS-1$
        if (vb != null)
            return (String) vb.getValue(getFacesContext());
        else
            return null;
    }

    public void setStoreTitle(String storeTitle) {
        this.storeTitle = storeTitle;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = jsId;
        values[2] = storeTitle;
        return values;
    }
    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        this.jsId = (String) values[1];
        storeTitle = (String) values[2];
    }
}
