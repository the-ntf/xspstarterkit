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

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;


/**
 * @author akosugi
 * 
 *        base class for ui components which uses url property
 */
public abstract class UIURLDataStoreComponent extends UIDojoStoreComponent {

    private String url;

    public String getUrl() {
        if (url != null)
            return url;
        ValueBinding _vb = getValueBinding("url"); // $NON-NLS-1$
        if (_vb != null)
            return (String) _vb.getValue(getFacesContext());
        else
            return null;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    @Override
    public Object saveState(FacesContext context) {
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = url;
        return values;
    }
    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        this.url = (String) values[1];
    }

}
