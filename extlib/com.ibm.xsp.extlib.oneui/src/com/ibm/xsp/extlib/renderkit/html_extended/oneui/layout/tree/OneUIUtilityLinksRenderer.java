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

import com.ibm.xsp.extlib.tree.ITreeNode;
import com.ibm.xsp.extlib.tree.complex.UserTreeNode;


public class OneUIUtilityLinksRenderer extends AbstractLinkPopupRenderer {

    private static final long serialVersionUID = 1L;

    public OneUIUtilityLinksRenderer() {
    }
    
    @Override
    protected Object getProperty(int prop) {
        switch(prop) {
            case PROP_MENUPREFIX:           return "ul"; //$NON-NLS-1$
        }
        return super.getProperty(prop);
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
}
