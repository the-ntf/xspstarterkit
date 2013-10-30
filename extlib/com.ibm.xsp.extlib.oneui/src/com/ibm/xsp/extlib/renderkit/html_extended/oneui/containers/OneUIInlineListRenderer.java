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






package com.ibm.xsp.extlib.renderkit.html_extended.oneui.containers;

import com.ibm.xsp.extlib.renderkit.html_extended.containers.ListRenderer;



/**
 * OneUI Inline List renderer.
 */
public class OneUIInlineListRenderer extends ListRenderer {
    
    public OneUIInlineListRenderer() {
    }

    @Override
    protected Object getProperty(int prop) {
        switch(prop) {
            case PROP_LISTSTYLECLASS:           return "lotusInlinelist"; // $NON-NLS-1$
            case PROP_FIRSTITEMSTYLECLASS:      return "lotusFirst"; // $NON-NLS-1$
            //case PROP_LASTITEMSTYLE:          return "padding-right: 0";
        }
        return super.getProperty(prop);
    }
}
