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






package com.ibm.xsp.extlib.renderkit.html_extended.oneui.outline.tree;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.xsp.extlib.renderkit.html_extended.outline.tree.LinksListRenderer;
import com.ibm.xsp.extlib.resources.OneUIResources;
import com.ibm.xsp.extlib.tree.ITreeNode;
import com.ibm.xsp.renderkit.html_basic.HtmlRendererUtil;


public class OneUILinksListRenderer extends LinksListRenderer {
    
    private static final long serialVersionUID = 1L;

    public OneUILinksListRenderer() {
    }

    public OneUILinksListRenderer(UIComponent component) {
        super(component);
    }
    
    @Override
    protected void writePopupImage(FacesContext context, ResponseWriter writer, TreeContextImpl tree) throws IOException {
        // Render the popup image (down arrow)
        // Uniquely if it has multiple choices
        if(tree.getNode().getType()!=ITreeNode.NODE_LEAF) {
            writer.writeText(" ",null); // $NON-NLS-1$
            writer.startElement("img",null); // $NON-NLS-1$
            writer.writeAttribute("class","lotusArrow lotusDropDownSprite",null); // $NON-NLS-1$ $NON-NLS-2$
            writer.writeAttribute("src",HtmlRendererUtil.getImageURL(context,OneUIResources.get().BLANK_GIF),null); // $NON-NLS-1$
            writer.writeAttribute("aria-label",com.ibm.xsp.extlib.oneui.ResourceHandler.getString("OneUILinksListRenderer_AriaLabel_ShowMenu"),null);  // $NON-NLS-1$ // $NLS-OneUILinksListRenderer_AriaLabel_ShowMenu-2$ 
            writer.writeAttribute("alt",com.ibm.xsp.extlib.oneui.ResourceHandler.getString("OneUILinksListRenderer_Alt_ShowMenu"),null);  // $NON-NLS-1$ // $NLS-OneUILinksListRenderer_Alt_ShowMenu-2$
            writer.endElement("img"); // $NON-NLS-1$
            writer.startElement("span",null); // $NON-NLS-1$
            writer.writeAttribute("class","lotusAltText",null); // $NON-NLS-1$ $NON-NLS-2$
            // Unicode Character 'BLACK DOWN-POINTING TRIANGLE' 
            writer.writeText("\u25BC", null); //$NON-NLS-1$ 
            writer.endElement("span"); // $NON-NLS-1$
        }
    }    
}
