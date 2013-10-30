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

import com.ibm.xsp.extlib.renderkit.html_extended.oneui.layout.tree.OneUIApplicationLinksRenderer;
import com.ibm.xsp.extlib.renderkit.html_extended.outline.tree.HtmlListRenderer;
import com.ibm.xsp.extlib.util.ExtLibUtil;


public class OneUIv3ApplicationLinksRenderer extends OneUIApplicationLinksRenderer {
    
    private static final long serialVersionUID = 1L;

    public OneUIv3ApplicationLinksRenderer() {
    }
//    
//    @Override
//    protected void preRenderTree(FacesContext context, ResponseWriter writer, TreeContextImpl tree) throws IOException {
//        super.preRenderTree(context, writer, tree);
//    }
//
//    @Override
//    protected void postRenderTree(FacesContext context, ResponseWriter writer, TreeContextImpl tree) throws IOException {
//        super.postRenderTree(context, writer, tree);
//    }
//    
//    @Override
//    protected String getContainerStyleClass(TreeContextImpl node) {
//        return "lotusInlinelist lotusLinks"; // $NON-NLS-1$
//    }
//    
//    @Override
//    protected String getContainerStyle(TreeContextImpl node) {
//        return "float: left"; // $NON-NLS-1$
//    }
//    
//    @Override
//    protected String getItemStyleClass(TreeContextImpl tree, boolean enabled, boolean selected) {
//        String value = null;
//        if(tree.getNodeContext().isFirstNode()) {
//            value = selected ? "lotusFirst lotusSelected" : "lotusFirst"; // $NON-NLS-1$ $NON-NLS-2$
//        } else {
//            value = !enabled || selected ? "lotusSelected" : null; // $NON-NLS-1$
//        }
//        String s = super.getItemStyleClass(tree,enabled,selected);
//        return ExtLibUtil.concatStyleClasses(value, s);
//    }
//    
//    @Override
//    protected void renderChildren(FacesContext context, ResponseWriter writer, TreeContextImpl tree) throws IOException {
//        // Do not render the children - only one level...
//        if(tree.getDepth()==1) {
//            super.renderChildren(context, writer, tree);
//        }
//    }   
}
