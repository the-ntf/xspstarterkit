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






package com.ibm.xsp.extlib.renderkit.html_extended.oneui.layout.tree;

import com.ibm.xsp.extlib.util.ExtLibUtil;


public class OneUIApplicationLinksRenderer extends AbstractLinkPopupRenderer {
    
    private static final long serialVersionUID = 1L;

    public OneUIApplicationLinksRenderer() {
    }
    
    @Override
    protected String getContainerStyleClass(TreeContextImpl node) {
        return "lotusInlinelist lotusLinks"; // $NON-NLS-1$
    }
    
    @Override
    protected String getContainerStyle(TreeContextImpl node) {
        return "float: left"; // $NON-NLS-1$
    }
    
    @Override
    protected String getItemStyleClass(TreeContextImpl tree, boolean enabled, boolean selected) {
        String value = null;
        if(tree.getNodeContext().isFirstNode()) {
            value = selected ? "lotusFirst lotusSelected" : "lotusFirst"; // $NON-NLS-1$ $NON-NLS-2$
        } else {
            value = !enabled || selected ? "lotusSelected" : null; // $NON-NLS-1$
        }
        String s = super.getItemStyleClass(tree,enabled,selected);
        return ExtLibUtil.concatStyleClasses(value, s);
    }
}
