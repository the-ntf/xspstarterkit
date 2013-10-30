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

import com.ibm.xsp.extlib.renderkit.html_extended.outline.tree.DojoDropDownButtonRenderer;
import com.ibm.xsp.extlib.resources.OneUIResources;


public class OneUIDropDownButtonRenderer extends DojoDropDownButtonRenderer {
    
    private static final long serialVersionUID = 1L;

    public OneUIDropDownButtonRenderer() {
    }
    
    @Override
    protected Object getProperty(int prop) {
        switch(prop) {
            case PROP_DROPDOWN_IMAGEURL_CLASS:     return OneUIResources.get().DROPDOWN_PNG;
        }
        return super.getProperty(prop);
    }
}
