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





package com.ibm.xsp.extlib.component.listview;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.ibm.xsp.extlib.component.domino.UIDojoWidgetComponent;

/**
 * 
 *
 * @deprecated this is unused
 */
public class UIReadViewEntriesService extends UIDojoWidgetComponent {
    public static final String COMPONENT_TYPE = "com.ibm.xsp.extlib.ReadViewEntriesService"; // $NON-NLS-1$
    public static final String COMPONENT_FAMILY = "com.ibm.xsp.extlib.listview.ListViewStore"; // $NON-NLS-1$
    public static final String RENDERER_TYPE = "com.ibm.xsp.extlib.dwa.ReadViewEntriesService"; //$NON-NLS-1$

    private String databaseName;
    private String viewName;

    public UIReadViewEntriesService() {
        setRendererType(RENDERER_TYPE);
        throw new RuntimeException("Unsupported class."); //$NON-NLS-1$
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getDatabaseName() {
        if (databaseName != null) {
            return databaseName;
        }        
        ValueBinding vb = getValueBinding("databaseName"); //$NON-NLS-1$
        if (vb != null) {
            return (String)vb.getValue(getFacesContext());
        }
        return null;
    }
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getViewName() {
        if (viewName != null) {
            return viewName;
        }
        
        ValueBinding vb = getValueBinding("viewName"); //$NON-NLS-1$
        if (vb != null) {
            return (String)vb.getValue(getFacesContext());
        }

        return null;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
    @Override
    public Object saveState(FacesContext context) {
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = databaseName;
        values[2] = viewName;
        return values;
    }
    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        this.databaseName = (String) values[1];
        this.viewName = (String) values[2];
    }
}
