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






package com.ibm.xsp.extlib.renderkit.html_extended.oneui.dojoext.form;

import com.ibm.xsp.extlib.renderkit.dojoext.form.DojoExtLinkSelectRenderer;

public class OneUIDojoExtLinkSelectRenderer extends DojoExtLinkSelectRenderer {

    @Override
    protected Object getProperty(int prop) {
        switch(prop) {
            case PROP_LISTSTYLE:            return "display: inline;"; // $NON-NLS-1$
            case PROP_LISTCLASS:            return "lotusInlinelist"; // $NON-NLS-1$
            case PROP_FIRSTITEMCLASS:       return "lotusFirst"; // $NON-NLS-1$
            //case PROP_LASTITEMSTYLE:      return "padding-right: 0";
            //LHEY97KEXJ - changing the styling of this control to remove reliance on colour
            //old version is commented out in the 2 lines below
            //case PROP_ENABLEDLINKSTYLE:     return "font-weight: bold;"; // $NON-NLS-1$
            //case PROP_DISABLEDLINKSTYLE:    return "color: rgb(128, 128, 128); font-weight: normal;"; // $NON-NLS-1$
            case PROP_ENABLEDLINKSTYLE:     return "font-weight: normal;"; // $NON-NLS-1$
            case PROP_DISABLEDLINKSTYLE:    return "font-weight: bold;"; // $NON-NLS-1$
        }
        return super.getProperty(prop);
    }
}
