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





package com.ibm.xsp.extlib.renderkit.html_extended.oneuiv302.dialog;

import javax.faces.context.FacesContext;

import com.ibm.xsp.dojo.FacesDojoComponent;
import com.ibm.xsp.extlib.renderkit.html_extended.oneui.dialog.OneUIDialogRenderer;
import com.ibm.xsp.extlib.renderkit.html_extended.oneuiv302.OneUIv302Resources;
import com.ibm.xsp.resource.DojoModuleResource;

public class OneUIv302DialogRenderer extends OneUIDialogRenderer {
    
     @Override
        protected String getDefaultDojoType(FacesContext context, FacesDojoComponent component) {
            return "extlib.dijit.OneUIv302Dialog"; // $NON-NLS-1$
        }
        
        @Override
        protected DojoModuleResource getDefaultDojoModule(FacesContext context, FacesDojoComponent component) {
            return OneUIv302Resources.oneUIv302Dialog;
        }

}
