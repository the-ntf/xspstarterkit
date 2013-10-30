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





package com.ibm.xsp.extlib.renderkit.html_extended.oneuiv302.outline.tree;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.xsp.extlib.renderkit.html_extended.oneui.outline.tree.OneUILinksListRenderer;

public class OneUIv302LinksListRenderer extends OneUILinksListRenderer {

    private static final long serialVersionUID = 1L;
    

    public OneUIv302LinksListRenderer() {
    }

    public OneUIv302LinksListRenderer(UIComponent component) {
        super(component);
    }
    
     @Override
    protected Object getProperty(int prop) {
        switch(prop) {
            case PROP_LINKSLIST_INLINELIST:   return "lotusInlinelist lotusActions"; // $NON-NLS-1$
            case PROP_LINKSLIST_FIRST:  return "lotusFirst"; // $NON-NLS-1$
        }
        return null;
    }
     
     @Override
        protected void preRenderList(FacesContext context, ResponseWriter writer, TreeContextImpl tree) throws IOException {
            startRenderContainer(context, writer, tree);
        }
     
     @Override
        protected void postRenderList(FacesContext context, ResponseWriter writer, TreeContextImpl tree) throws IOException {
            endRenderContainer(context, writer, tree);
        }

}
