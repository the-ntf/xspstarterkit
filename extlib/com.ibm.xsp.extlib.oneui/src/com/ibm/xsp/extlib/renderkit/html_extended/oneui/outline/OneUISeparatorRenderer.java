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






package com.ibm.xsp.extlib.renderkit.html_extended.oneui.outline;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.xsp.extlib.component.outline.UISeparator;
import com.ibm.xsp.extlib.renderkit.html_extended.outline.SeparatorRenderer;


public class OneUISeparatorRenderer extends SeparatorRenderer {

    @Override
    protected void writeSeparator(FacesContext context, ResponseWriter w, UISeparator c) throws IOException {
        w.startElement("span", c); // $NON-NLS-1$
        w.writeAttribute("class", "lotusDivider", null); // $NON-NLS-1$ $NON-NLS-2$
        w.writeAttribute("style", "margin-left: 3px; margin-right: 3px;", null); // $NON-NLS-1$ $NON-NLS-2$
        //w.writeAttribute("style", "border-color:#CCCCCC; border-left-style:solid; border-left-width:1px; margin: 0 5px 0 5px;", null);
        w.writeAttribute("role", "separator", null); // $NON-NLS-1$ $NON-NLS-2$
        w.write('|');
        w.endElement("span"); // $NON-NLS-1$
    }

}
