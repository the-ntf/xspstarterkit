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

import com.ibm.xsp.extlib.renderkit.html_extended.outline.tree.HtmlListRenderer;
import com.ibm.xsp.extlib.tree.ITreeNode;
import com.ibm.xsp.extlib.tree.complex.UserTreeNode;
import com.ibm.xsp.extlib.util.ExtLibUtil;


public class OneUIv3UtilityLinksRenderer extends HtmlListRenderer {
    
    private static final long serialVersionUID = 1L;

    public OneUIv3UtilityLinksRenderer() {
    }

    @Override
    protected String getContainerStyleClass(TreeContextImpl node) {
        return "lotusInlinelist lotusUtility"; // $NON-NLS-1$
    }
    
    @Override
    public boolean isNodeEnabled(ITreeNode node) {
        // The user node should not be enabled by default... 
        return !(node instanceof UserTreeNode);
    }
    
    @Override
    protected String getItemStyleClass(TreeContextImpl tree, boolean enabled, boolean selected) {
        String value = null;
        if(tree.getNodeContext().isFirstNode()) {
            value = "lotusFirst"; // $NON-NLS-1$
        }
        if(selected || !enabled) {
            value = ExtLibUtil.concatStyleClasses(value,"lotusSelected"); // $NON-NLS-1$
            //value = selected ? "lotusFirst lotusSelected" : "lotusFirst";
        }
        if(tree.getNode() instanceof UserTreeNode) {
            value = ExtLibUtil.concatStyleClasses(value,"lotusUser"); // $NON-NLS-1$
        }
        value = ExtLibUtil.concatStyleClasses(value,super.getItemStyleClass(tree,enabled,selected));
        return value;
    }   
}
