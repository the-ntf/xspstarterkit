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





package com.ibm.xsp.extlib.renderkit.html_extended.oneuiv302.outline;

import javax.faces.context.FacesContext;

import com.ibm.xsp.extlib.component.outline.AbstractOutline;
import com.ibm.xsp.extlib.renderkit.html_extended.oneui.outline.OneUISortLinksRenderer;
import com.ibm.xsp.extlib.tree.ITreeRenderer;

public class OneUIv302SortLinksRenderer extends OneUISortLinksRenderer {
    
    @Override
    protected ITreeRenderer findTreeRenderer(FacesContext context, AbstractOutline outline) {
        return new com.ibm.xsp.extlib.renderkit.html_extended.oneuiv302.outline.tree.OneUIv302SortLinksRenderer(outline);
    }

}
