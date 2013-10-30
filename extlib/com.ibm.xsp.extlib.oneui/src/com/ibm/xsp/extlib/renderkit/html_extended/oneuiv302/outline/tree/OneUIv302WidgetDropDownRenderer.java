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

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.renderkit.html_extended.oneui.outline.tree.OneUIWidgetDropDownRenderer;
import com.ibm.xsp.extlib.resources.OneUIResources;
import com.ibm.xsp.renderkit.html_basic.HtmlRendererUtil;
import com.ibm.xsp.util.JSUtil;

/**
 * @author kevin
 *
 */
public class OneUIv302WidgetDropDownRenderer extends OneUIWidgetDropDownRenderer {

    private static final long serialVersionUID = 1L;

    @Override
    protected void renderPopupButton(FacesContext context, ResponseWriter writer, TreeContextImpl tree, boolean enabled, boolean selected) throws IOException {
        writer.startElement("a",null);
        // A popup button requires an id
        String clientId = tree.getClientId(context,"ab",1); // $NON-NLS-1$
        if(StringUtil.isNotEmpty(clientId)) {
            writer.writeAttribute("id", clientId, null); // $NON-NLS-1$
        }
        writer.writeAttribute("class","lotusIcon lotusActionIcon",null); // $NON-NLS-1$ $NON-NLS-2$
        writer.writeAttribute("href", "javascript:;", null); // $NON-NLS-1$ $NON-NLS-2$
        writer.writeAttribute("title",com.ibm.xsp.extlib.oneui.ResourceHandler.getString("OneUIWidgetDropDownRenderer.clickforactions"),null); // $NON-NLS-1$ $NLS-OneUIWidgetDropDownRenderer.clickforactions-2$
        writer.writeAttribute("aria-haspopup","true",null); // $NON-NLS-1$ $NON-NLS-2$
        writer.startElement("img",null); // $NON-NLS-1$
        writer.writeAttribute("src", HtmlRendererUtil.getImageURL(context,OneUIResources.get().BLANK_GIF), null); // $NON-NLS-1$
        writer.writeAttribute("alt","",null); // $NON-NLS-1$
        writer.writeAttribute("aria-owns",clientId,null); // $NON-NLS-1$ $NON-NLS-2$
        writer.endElement("img"); // $NON-NLS-1$
        writer.startElement("span",null); // $NON-NLS-1$
        writer.writeAttribute("class","lotusAltText",null); // $NON-NLS-1$ $NON-NLS-2$
        writer.writeText(com.ibm.xsp.extlib.oneui.ResourceHandler.getString("OneUIWidgetDropDownRenderer.Actions"),null); // $NLS-OneUIWidgetDropDownRenderer.Actions-1$
        writer.endElement("span"); // $NON-NLS-1$
        writer.endElement("a");
        JSUtil.writeln(writer);
    }
}
