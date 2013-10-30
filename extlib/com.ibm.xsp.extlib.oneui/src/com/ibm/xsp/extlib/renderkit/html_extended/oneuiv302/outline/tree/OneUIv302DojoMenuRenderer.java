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





package com.ibm.xsp.extlib.renderkit.html_extended.oneuiv302.outline.tree;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.xsp.component.UIViewRootEx;
import com.ibm.xsp.extlib.renderkit.html_extended.oneuiv302.OneUIv302Resources;
import com.ibm.xsp.extlib.renderkit.html_extended.outline.tree.DojoMenuRenderer;
import com.ibm.xsp.extlib.resources.ExtLibResources;

public class OneUIv302DojoMenuRenderer extends DojoMenuRenderer {
    
    private static final long serialVersionUID = 1L;

    @Override
    protected String getMenuType() {
        return "extlib.dijit.OneUIv302Menu"; // $NON-NLS-1$
    }
    
    @Override
    protected String getMenuItemType() {
        return "extlib.dijit.OneUIv302MenuItem"; // $NON-NLS-1$
    }
    
     @Override
        protected void preRenderTree(FacesContext context, ResponseWriter writer, TreeContextImpl tree) throws IOException {
         UIViewRootEx rootEx = (UIViewRootEx) context.getViewRoot();
         rootEx.setDojoTheme(true);
         ExtLibResources.addEncodeResource(rootEx, OneUIv302Resources.extlibMenuItem);
         super.preRenderTree(context, writer, tree);
     }
}

