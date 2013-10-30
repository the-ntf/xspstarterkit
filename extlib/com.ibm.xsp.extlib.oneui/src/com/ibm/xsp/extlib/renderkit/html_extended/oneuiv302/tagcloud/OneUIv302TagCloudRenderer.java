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





package com.ibm.xsp.extlib.renderkit.html_extended.oneuiv302.tagcloud;

import javax.faces.context.FacesContext;

import com.ibm.xsp.extlib.component.tagcloud.ITagCloudEntry;
import com.ibm.xsp.extlib.component.tagcloud.UITagCloud;
import com.ibm.xsp.extlib.renderkit.html_extended.oneui.tagcloud.OneUITagCloudRenderer;

public class OneUIv302TagCloudRenderer extends OneUITagCloudRenderer {
    
    @Override
    protected String getLinkStyleClass(FacesContext context, UITagCloud tagCloud, ITagCloudEntry entry) {
        int weight = entry.getWeight();
        String styleClass = "lotusF"+weight; // $NON-NLS-1$
        return styleClass;
    }

}
