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
import com.ibm.xsp.extlib.renderkit.html_extended.oneui.outline.tree.OneUIDropDownButtonRenderer;
import com.ibm.xsp.extlib.util.ExtLibRenderUtil;
import com.ibm.xsp.renderkit.html_basic.HtmlRendererUtil;
import com.ibm.xsp.util.JSUtil;

public class OneUIv302DropDownButtonRenderer extends OneUIDropDownButtonRenderer {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final int PROP_BUTTON_CONTAINER_CLASS       = 5;
    
     @Override
        protected Object getProperty(int prop) {
            switch(prop) {
                case PROP_BUTTON_CONTAINER_CLASS:     return "lotusBtnContainer"; // $NON-NLS-1$
            }
            return super.getProperty(prop);
        }
     
     @Override
        protected void preRenderList(FacesContext context, ResponseWriter writer, TreeContextImpl tree) throws IOException {
            setContainerStyleClass((String)getProperty(PROP_BUTTON_CONTAINER_CLASS));
            super.preRenderList(context, writer, tree);

        }

    @Override
    protected String getItemTag() {
        return ""; // $NON-NLS-1$
    }
    @Override
    protected void renderPopupButton(FacesContext context, ResponseWriter writer, TreeContextImpl tree, boolean enabled, boolean selected) throws IOException {
            String clientId = tree.getClientId(context,"ab",1); // $NON-NLS-1$
            
            writer.startElement("button",null); // $NON-NLS-1$
            writer.writeAttribute("class", (String)getProperty(PROP_DROPDOWN_BUTTON_CLASS), null); // $NON-NLS-1$
            writer.writeAttribute("aria-owns", clientId+MENUID_SUFFIX, null); // $NON-NLS-1$
            writer.writeAttribute("aria-haspopup", "true", null); // $NON-NLS-1$ $NON-NLS-2$
            writer.writeAttribute("role", "button", null); // $NON-NLS-1$ $NON-NLS-2$
             
            //A popup button requires an id
           writer.writeAttribute("id", clientId, null); // $NON-NLS-1$
            
            String image = tree.getNode().getImage();
            boolean hasImage = StringUtil.isNotEmpty(image);
            if(hasImage) {
                writer.startElement("img",null); // $NON-NLS-1$
                if(StringUtil.isNotEmpty(image)) {
                    image = HtmlRendererUtil.getImageURL(context, image);
                    writer.writeAttribute("src",image,null); // $NON-NLS-1$
                    String imageAlt = tree.getNode().getImageAlt();
                    if ( ExtLibRenderUtil.isAltPresent(imageAlt) ) {
                        writer.writeAttribute("alt",imageAlt,null); // $NON-NLS-1$
                    }
                    String imageHeight = tree.getNode().getImageHeight();
                    if (StringUtil.isNotEmpty(imageHeight)) {
                        writer.writeAttribute("height",imageHeight,null); // $NON-NLS-1$
                    }
                    String imageWidth = tree.getNode().getImageWidth();
                    if (StringUtil.isNotEmpty(imageWidth)) {
                        writer.writeAttribute("width",imageWidth,null); // $NON-NLS-1$
                    }
                }
                writer.endElement("img"); // $NON-NLS-1$
            }
            
            // Render the text
            String label = tree.getNode().getLabel();
            if(StringUtil.isNotEmpty(label)) {
                writer.writeText(label, "label"); // $NON-NLS-1$
            }
            writer.writeText(" ",null); // $NON-NLS-1$
            
            // Render the popup image (down arrow)
            writePopupImage(context, writer, tree);

            //writer.endElement("a");
            writer.endElement("button"); // $NON-NLS-1$
            JSUtil.writeln(writer);
    }
}
