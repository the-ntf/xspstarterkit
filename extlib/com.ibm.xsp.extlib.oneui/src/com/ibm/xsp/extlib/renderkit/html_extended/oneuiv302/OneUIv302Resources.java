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






package com.ibm.xsp.extlib.renderkit.html_extended.oneuiv302;

import com.ibm.xsp.extlib.resources.OneUIResources;
import com.ibm.xsp.resource.DojoModuleResource;


/**
 * Shared OneUI 3.0.2 Dojo resources.
 * 
 * @author priand
 *
 */
public class OneUIv302Resources extends OneUIResources {
    
    public static final OneUIv302Resources instance = new OneUIv302Resources();
    public static final DojoModuleResource oneUIv302Dialog = new DojoModuleResource("extlib.dijit.OneUIv302Dialog"); // $NON-NLS-1$
    public static final DojoModuleResource oneUIv302PickerCheckbox = new DojoModuleResource("extlib.dijit.OneUIv302PickerCheckbox"); // $NON-NLS-1$
    public static final DojoModuleResource oneUIv302PickerList = new DojoModuleResource("extlib.dijit.OneUIv302PickerList"); // $NON-NLS-1$
    public static final DojoModuleResource oneUIv302PickerListSearch = new DojoModuleResource("extlib.dijit.OneUIv302PickerListSearch"); // $NON-NLS-1$
    public static final DojoModuleResource oneUIv302PickerName = new DojoModuleResource("extlib.dijit.OneUIv302PickerName"); // $NON-NLS-1$
    public static final DojoModuleResource extlibMenu = new DojoModuleResource("extlib.dijit.OneUIv302Menu");   // $NON-NLS-1$
    public static final DojoModuleResource extlibMenuItem = new DojoModuleResource("extlib.dijit.OneUIv302MenuItem");   // $NON-NLS-1$

    public OneUIv302Resources() {
        this.BLANK_GIF      = "/.ibmxspres/.oneuiv302/oneui/css/images/blank.gif"; // $NON-NLS-1$
        this.DROPDOWN_PNG   = "/.ibmxspres/.oneuiv302/oneuicompat/btnDropDown2.png"; // $NON-NLS-1$
        this.ICON_ERROR     = "/.ibmxspres/.oneuiv302/oneuicompat/iconError16.png"; // $NON-NLS-1$
        this.ICON_WARN      = "/.ibmxspres/.oneuiv302/oneuicompat/iconWarning16.png"; // $NON-NLS-1$
        this.ICON_INFO      = "/.ibmxspres/.oneuiv302/oneuicompat/iconInfo16.png"; // $NON-NLS-1$
        this.ICON_HELP      = "/.ibmxspres/.oneuiv302/oneuicompat/iconHelp16.png"; // $NON-NLS-1$
    }
}
