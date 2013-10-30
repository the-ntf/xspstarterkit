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






package com.ibm.xsp.extlib.renderkit.html_extended.oneuiv3;

import com.ibm.xsp.extlib.resources.OneUIResources;


/**
 * Shared OneUI Dojo resources.
 * 
 * @author priand
 *
 */
public class OneUIv3Resources extends OneUIResources {
    
    public static final OneUIv3Resources instance = new OneUIv3Resources();

    public OneUIv3Resources() {
        this.BLANK_GIF      = "/.ibmxspres/.oneuiv3/oneui/css/images/blank.gif"; // $NON-NLS-1$
        this.DROPDOWN_PNG   = "/.ibmxspres/.oneuiv3/oneuicompat/btnDropDown2.png"; // $NON-NLS-1$
        this.ICON_ERROR     = "/.ibmxspres/.oneuiv3/oneuicompat/iconError16.png"; // $NON-NLS-1$
        this.ICON_WARN      = "/.ibmxspres/.oneuiv3/oneuicompat/iconWarning16.png"; // $NON-NLS-1$
        this.ICON_INFO      = "/.ibmxspres/.oneuiv3/oneuicompat/iconInfo16.png"; // $NON-NLS-1$
        this.ICON_HELP      = "/.ibmxspres/.oneuiv3/oneuicompat/iconHelp16.png"; // $NON-NLS-1$
    }
}
