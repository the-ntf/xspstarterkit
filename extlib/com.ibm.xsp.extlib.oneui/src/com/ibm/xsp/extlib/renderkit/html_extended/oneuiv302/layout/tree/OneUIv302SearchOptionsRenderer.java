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





package com.ibm.xsp.extlib.renderkit.html_extended.oneuiv302.layout.tree;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.xsp.extlib.renderkit.html_extended.outline.tree.HtmlComboBoxRenderer;

public class OneUIv302SearchOptionsRenderer extends HtmlComboBoxRenderer {

    private static final long serialVersionUID = 1L;
    
     @Override
        protected void postRenderTree(FacesContext context, ResponseWriter writer, TreeContextImpl tree) throws IOException {
            super.postRenderTree(context, writer, tree);
            //writer.endElement("div");
        }
            
        @Override
        public String getStyle() {
            return "border-width: 1px; border-style: solid;margin-left: 8px;margin-top: 1px;"; // $NON-NLS-1$
//        return "border-style: none;";
        }
        
        @Override
        public String getStyleClass() {
            return "lotusInactive"; // $NON-NLS-1$
        }
    

}
