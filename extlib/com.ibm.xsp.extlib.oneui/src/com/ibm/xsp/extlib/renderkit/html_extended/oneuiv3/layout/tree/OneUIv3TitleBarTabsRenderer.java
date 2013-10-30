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






package com.ibm.xsp.extlib.renderkit.html_extended.oneuiv3.layout.tree;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.xsp.extlib.renderkit.html_extended.outline.tree.HtmlListRenderer;
import com.ibm.xsp.extlib.util.ExtLibUtil;


public class OneUIv3TitleBarTabsRenderer extends HtmlListRenderer {
    
    private static final long serialVersionUID = 1L;

    public OneUIv3TitleBarTabsRenderer() {
    }
    
    @Override
    protected void renderEntryItemContent(FacesContext context, ResponseWriter writer, TreeContextImpl tree, boolean enabled, boolean selected) throws IOException {
        writer.startElement("div", null); // $NON-NLS-1$
        super.renderEntryItemContent(context, writer, tree, enabled, selected);
        writer.endElement("div"); // $NON-NLS-1$
    }

    @Override
    protected String getContainerStyleClass(TreeContextImpl node) {
        return "lotusTabs"; // $NON-NLS-1$
    }
    
    @Override
    protected String getItemStyleClass(TreeContextImpl tree, boolean enabled, boolean selected) {
        String value = null;
        if(selected) {
            value = "lotusSelected"; // $NON-NLS-1$
        }
        String s = super.getItemStyleClass(tree,enabled,selected);
        return ExtLibUtil.concatStyleClasses(value, s);
    }
    
    @Override
    protected void renderChildren(FacesContext context, ResponseWriter writer, TreeContextImpl tree) throws IOException {
        // Do not render the children - only one level...
        if(tree.getDepth()==1) {
            super.renderChildren(context, writer, tree);
        }
    }   
}
