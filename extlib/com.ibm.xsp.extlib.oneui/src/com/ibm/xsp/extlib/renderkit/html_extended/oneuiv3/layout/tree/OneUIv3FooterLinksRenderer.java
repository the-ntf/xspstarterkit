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






package com.ibm.xsp.extlib.renderkit.html_extended.oneuiv3.layout.tree;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.xsp.extlib.renderkit.html_extended.outline.tree.HtmlListRenderer;
import com.ibm.xsp.extlib.tree.ITreeNode;


public class OneUIv3FooterLinksRenderer extends HtmlListRenderer {
    
    private static final long serialVersionUID = 1L;

    public OneUIv3FooterLinksRenderer() {
    }

    @Override
    public void renderChildren(FacesContext context, ResponseWriter writer, TreeContextImpl tree) throws IOException {
        // Get the tree node list for the outline
        if(tree.getNodeContext().hasChildren()) {
            writer.startElement("table",null); // $NON-NLS-1$
            writer.writeAttribute("cellspacing","0",null); // $NON-NLS-1$
            //writer.writeAttribute("role","presentation",null); // $NON-NLS-1$ $NON-NLS-2$
            writer.startElement("tbody",null); // $NON-NLS-1$

            // Write the titles
            // Shouldn't it be in thead???
            writer.startElement("tr",null); // $NON-NLS-1$
            int i=0;
            for(ITreeNode.NodeIterator it = tree.getNodeContext().iterateChildren(0,Integer.MAX_VALUE); it.hasNext(); i++ ) {
                ITreeNode node = it.next();
                tree.push(node, i, !it.hasNext());
                writer.startElement("th",null); // $NON-NLS-1$
                if(!it.hasNext()) {
                    writer.writeAttribute("class","lotusLast",null); // $NON-NLS-1$ $NON-NLS-2$
                }
                writer.writeText(tree.getNode().getLabel(),null);
                writer.endElement("th"); // $NON-NLS-1$
                tree.pop();
            }
            writer.endElement("tr"); // $NON-NLS-1$

            // Write the links
            writer.startElement("tr",null); // $NON-NLS-1$
            i=0;
            for(ITreeNode.NodeIterator it = tree.getNodeContext().iterateChildren(0,Integer.MAX_VALUE); it.hasNext(); i++ ) {
                ITreeNode node = it.next();
                tree.push(node, i, !it.hasNext());
                writer.startElement("td",null); // $NON-NLS-1$
                if(!it.hasNext()) {
                    writer.writeAttribute("class","lotusLast",null); // $NON-NLS-1$ $NON-NLS-2$
                }
                if(tree.getNodeContext().hasChildren()) {
                    preRenderList(context, writer, tree);
                    renderList(context, writer, tree);
                    postRenderList(context, writer, tree);
                }
                writer.endElement("td"); // $NON-NLS-1$
                tree.pop();
            }
            writer.endElement("tr"); // $NON-NLS-1$

            writer.endElement("tbody"); // $NON-NLS-1$
            writer.endElement("table"); // $NON-NLS-1$
        }
    }
}
