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






package com.ibm.xsp.extlib.renderkit.html_extended.mobile;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.xsp.ajax.AjaxUtil;
import com.ibm.xsp.dojo.FacesDojoComponent;
import com.ibm.xsp.extlib.component.dojo.UIDojoWidgetBase;
import com.ibm.xsp.extlib.component.mobile.UITabBar;
import com.ibm.xsp.extlib.renderkit.dojo.DojoRendererUtil;
import com.ibm.xsp.extlib.renderkit.dojo.DojoWidgetBaseRenderer;
import com.ibm.xsp.extlib.resources.ExtLibResources;
import com.ibm.xsp.resource.DojoModuleResource;

/**
 * 
 */
public class TabBarRenderer extends DojoWidgetBaseRenderer { 
    
    protected static final int PROP_FACETNAME = 100;
    
    @Override
    protected Object getProperty(int prop) {
        switch(prop) {
            case PROP_FACETNAME: return "actionFacet"; // $NON-NLS-1$
        }
        return super.getProperty(prop);
    }
    
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        // Get the response renderer
        ResponseWriter writer = context.getResponseWriter();

        // Do not render if it is not needed
        if (AjaxUtil.isAjaxNullResponseWriter(writer)) {
            return;
        }

        // And write the value
        if (component instanceof UIDojoWidgetBase) {
            writeTag(context, (UIDojoWidgetBase) component, writer);
        }
        
    }
    
    @Override
    protected DojoModuleResource getDefaultDojoModule(FacesContext context, FacesDojoComponent component) {
        return ExtLibResources.extlibMobile;
    }

    @Override
    protected String getDefaultDojoType(FacesContext context, FacesDojoComponent component) {
        return "extlib.dijit.mobile.TabBar"; // $NON-NLS-1$
    }
    
    @Override
    protected String getTagName() {
        return "ul"; // $NON-NLS-1$
    }

    @Override
    protected void initDojoAttributes(FacesContext context, FacesDojoComponent dojoComponent, Map<String,String> attrs) throws IOException {
        super.initDojoAttributes(context, dojoComponent, attrs);
        if(dojoComponent instanceof UITabBar) {
            UITabBar c = (UITabBar)dojoComponent;
            DojoRendererUtil.addDojoHtmlAttributes(attrs,"barType", c.getBarType()); // $NON-NLS-1$
            DojoRendererUtil.addDojoHtmlAttributes(attrs,"inHeading", c.isInHeading(), /*defaultValue*/false); // $NON-NLS-1$
        }
    }    
}
