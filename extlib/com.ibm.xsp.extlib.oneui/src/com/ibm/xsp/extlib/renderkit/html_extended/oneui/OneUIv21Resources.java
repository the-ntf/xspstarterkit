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






package com.ibm.xsp.extlib.renderkit.html_extended.oneui;

import com.ibm.xsp.extlib.resources.OneUIResources;


/**
 * Shared OneUI V2.1 Dojo resources.
 * 
 * @author priand
 *
 */
public class OneUIv21Resources extends OneUIResources {
    
    public static final OneUIv21Resources instance = new OneUIv21Resources();
    
    public OneUIv21Resources() {
        this.BLANK_GIF      = "/.ibmxspres/domino/oneuiv2.1/images/blank.gif"; // $NON-NLS-1$
        
        // The following images no longer exist in OneUIv2.1 -> we default to OneUIv2
//        this.DROPDOWN_PNG   = "/.ibmxspres/domino/oneuiv2/images/btnDropDown2.png"; // $NON-NLS-1$
//        this.ICON_ERROR     = "/.ibmxspres/domino/oneuiv2/images/iconError16.png"; // $NON-NLS-1$
//        this.ICON_WARN      = "/.ibmxspres/domino/oneuiv2/images/iconWarning16.png"; // $NON-NLS-1$
//        this.ICON_INFO      = "/.ibmxspres/domino/oneuiv2/images/iconInfo16.png"; // $NON-NLS-1$
//        this.ICON_HELP      = "/.ibmxspres/domino/oneuiv2/images/iconHelp16.png"; // $NON-NLS-1$
    }
}
