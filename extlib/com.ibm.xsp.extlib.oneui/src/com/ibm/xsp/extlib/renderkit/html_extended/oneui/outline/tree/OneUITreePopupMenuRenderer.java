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






package com.ibm.xsp.extlib.renderkit.html_extended.oneui.outline.tree;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.renderkit.html_extended.outline.tree.DojoMenuRenderer;
import com.ibm.xsp.extlib.renderkit.html_extended.outline.tree.HtmlDivSpanRenderer;
import com.ibm.xsp.extlib.tree.ITreeNode;


/**
 * Abtract class to render a button displaying a pop-up menu.
 * @author priand
 */
public abstract class OneUITreePopupMenuRenderer extends HtmlDivSpanRenderer {
    
    private static final long serialVersionUID = 1L;

    public OneUITreePopupMenuRenderer() {
    }
    
    @Override
    protected void renderEntryItemContent(FacesContext context, ResponseWriter writer, TreeContextImpl tree, boolean enabled, boolean selected) throws IOException {
        boolean leaf = tree.getNode().getType()==ITreeNode.NODE_LEAF;
        if(leaf) {
            super.renderEntryItemContent(context, writer, tree, enabled, selected);
        } else {
            renderPopupButton(context, writer, tree, enabled, selected);
        }
    }
    
    protected void renderPopupButton(FacesContext context, ResponseWriter writer, TreeContextImpl tree, boolean enabled, boolean selected) throws IOException {
        // TODO - generic button?
    }
    
    @Override
    protected void renderChildren(FacesContext context, ResponseWriter writer, TreeContextImpl tree) throws IOException {
        int depth = tree.getDepth();
        if(depth==1) {
            super.renderChildren(context, writer, tree);
        } else {
            if(tree.getNode().getType()!=ITreeNode.NODE_LEAF) {
                DojoMenuRenderer r = new DojoMenuRenderer();
                String clientId = tree.getClientId(context,"ab",1); // $NON-NLS-1$

                String mid = clientId+"_mn"; // $NON-NLS-1$
                r.setMenuId(mid);

                if(StringUtil.isNotEmpty(clientId)) {
                    r.setConnectId(clientId);
                }

                r.setConnectEvent("onclick"); // $NON-NLS-1$
                r.render(context, writer, tree);
            }
        }
    }
}
