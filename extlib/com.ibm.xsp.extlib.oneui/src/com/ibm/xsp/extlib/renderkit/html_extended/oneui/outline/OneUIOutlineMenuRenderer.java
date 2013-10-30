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






package com.ibm.xsp.extlib.renderkit.html_extended.oneui.outline;

import javax.faces.context.FacesContext;

import com.ibm.xsp.extlib.component.outline.AbstractOutline;
import com.ibm.xsp.extlib.component.outline.UIOutlineNavigator;
import com.ibm.xsp.extlib.renderkit.html_extended.oneui.outline.tree.OneUIMenuRenderer;
import com.ibm.xsp.extlib.renderkit.html_extended.outline.AbstractOutlineRenderer;
import com.ibm.xsp.extlib.tree.ITreeRenderer;

public class OneUIOutlineMenuRenderer extends AbstractOutlineRenderer {

    @Override
    protected ITreeRenderer findTreeRenderer(FacesContext context, AbstractOutline outline) {
        OneUIMenuRenderer r = createMenuRenderer(context, outline);
        if(outline instanceof UIOutlineNavigator) {
            UIOutlineNavigator nav = (UIOutlineNavigator)outline;
            r.setExpandable(nav.isExpandable());
            r.setExpandEffect(nav.getExpandEffect());
            //r.setKeepState(nav.isKeepState());
            r.setExpandLevel(nav.getExpandLevel());
        }
        return r;
    }
    
    protected OneUIMenuRenderer createMenuRenderer(FacesContext context, AbstractOutline outline) {
        return new OneUIMenuRenderer(outline);
    }
}
