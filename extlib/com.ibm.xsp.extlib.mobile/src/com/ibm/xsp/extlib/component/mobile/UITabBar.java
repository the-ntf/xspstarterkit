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





package com.ibm.xsp.extlib.component.mobile;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.ibm.xsp.extlib.component.dojo.UIDojoWidgetBase;

public class UITabBar extends UIDojoWidgetBase {
    public static final String RENDERER_TYPE = "com.ibm.xsp.extlib.mobile.DojoTabBar"; //$NON-NLS-1$
    public static final String COMPONENT_FAMILY = "com.ibm.xsp.extlib.Mobile"; //$NON-NLS-1$
    public static final String COMPONENT_TYPE = "com.ibm.xsp.extlib.mobile.TabBar"; //$NON-NLS-1$
    
    private String barType;
    private Boolean inHeading;
    
    public UITabBar() {
        setRendererType(RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    public String getBarType ( ) {
        if (null != this.barType) {
            return this.barType;
        }
        ValueBinding _vb = getValueBinding("barType"); //$NON-NLS-1$
        if (_vb != null) {
            String val = (String) _vb.getValue(FacesContext.getCurrentInstance());
            if(val!=null) {
                return val;
            }
        } 
        return null;
    }
    public boolean isInHeading( ) {
        if (null != this.inHeading) {
            return this.inHeading;
        }
        ValueBinding _vb = getValueBinding("inHeading"); //$NON-NLS-1$
        if (_vb != null) {
            Boolean val = (Boolean) _vb.getValue(FacesContext.getCurrentInstance());
            if(val!=null) {
                return val;
            }
        } 
        return false;
    }
    public void setInHeading(boolean inHeading){
        this.inHeading = inHeading;
        
    }
    public void setBarType ( String barType ) {
        this.barType = barType;
    }

    public String getStyleKitFamily() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void restoreState(FacesContext _context, Object _state) {
        Object _values[] = (Object[]) _state;
        super.restoreState(_context, _values[0]);
        this.barType = (String)_values[1];
        this.inHeading = (Boolean)_values[2];
    }
    
    @Override
    public Object saveState(FacesContext _context) {
        Object _values[] = new Object[3];
        _values[0] = super.saveState(_context);
        _values[1] = barType;
        _values[2] = inHeading;
        return _values;
    }

}
