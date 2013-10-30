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






package com.ibm.xsp.extlib.renderkit.html_extended.oneui.dialog;

import javax.faces.context.FacesContext;

import com.ibm.xsp.dojo.FacesDojoComponent;
import com.ibm.xsp.extlib.renderkit.html_extended.dialog.DialogRenderer;
import com.ibm.xsp.extlib.resources.OneUIResources;
import com.ibm.xsp.resource.DojoModuleResource;


public class OneUIDialogRenderer extends DialogRenderer {

    @Override
    protected String getDefaultDojoType(FacesContext context, FacesDojoComponent component) {
        return "extlib.dijit.OneUIDialog"; // $NON-NLS-1$
    }
    
    @Override
    protected DojoModuleResource getDefaultDojoModule(FacesContext context, FacesDojoComponent component) {
        return OneUIResources.oneUIDialog;
    }
}
