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

import com.ibm.xsp.dojo.FacesDojoComponent;
import com.ibm.xsp.extlib.component.mobile.UIDMRoundRectList;
import com.ibm.xsp.extlib.renderkit.dojo.DojoRendererUtil;
import com.ibm.xsp.extlib.renderkit.dojo.DojoWidgetRenderer;
import com.ibm.xsp.extlib.resources.ExtLibResources;
import com.ibm.xsp.resource.DojoModuleResource;

/**
 * 
 */
public class DMRoundRectListRenderer extends DojoWidgetRenderer {   
    
    
    @Override
    protected void startTag(FacesContext context, ResponseWriter writer, UIComponent component) throws IOException {
        super.startTag(context, writer, component);        
       // writer.writeAttribute("class", "mblFullRectList", null); //$NON-NLS-2$ $NON-NLS-1$
        
    }
    
    @Override
    protected DojoModuleResource getDefaultDojoModule(FacesContext context, FacesDojoComponent component) {
        return ExtLibResources.extlibMobile;
    }

    @Override
    protected String getDefaultDojoType(FacesContext context, FacesDojoComponent component) {
        return "dojox.mobile.RoundRectList"; // $NON-NLS-1$
    }
    
    @Override
    protected String getTagName() {
        return "ul"; // $NON-NLS-1$
    }

    @Override
    protected void initDojoAttributes(FacesContext context, FacesDojoComponent dojoComponent, Map<String,String> attrs) throws IOException {
        super.initDojoAttributes(context, dojoComponent, attrs);
        if(dojoComponent instanceof com.ibm.xsp.extlib.component.mobile.UIDMRoundRectList) {
            UIDMRoundRectList c = (UIDMRoundRectList)dojoComponent;
            DojoRendererUtil.addDojoHtmlAttributes(attrs,"transition",c.getTransition()); // $NON-NLS-1$
            DojoRendererUtil.addDojoHtmlAttributes(attrs,"iconBase",c.getIconBase()); // $NON-NLS-1$
            DojoRendererUtil.addDojoHtmlAttributes(attrs,"iconPos",c.getIconPos()); // $NON-NLS-1$
        }
    }    
}
