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






package com.ibm.xsp.extlib.library;

import java.io.IOException;
import java.util.List;

import javax.faces.context.FacesContext;

import com.ibm.xsp.context.RequestParameters;
import com.ibm.xsp.resource.Resource;

/**
 * Extlib library request customizer.
 * @author Philippe Riand
 */
public class ExtlibRequestCustomizer implements RequestParameters.ResourceProvider {

    public static final ExtlibRequestCustomizer instance = new ExtlibRequestCustomizer();
    
    private static List<Resource> resources;
    
    public List<Resource> getResources(FacesContext context) throws IOException {
        
        if(resources==null) {
//          resources = new ArrayList<Resource>();
//          ScriptResource dojoModulePath = new ScriptResource();
//          dojoModulePath.setClientSide(true);
//          String dojoPath = context.getExternalContext().encodeResourceURL(ExtlibResourceProvider.DOJO_PATH);
//          StringBuilder b = new StringBuilder();
//          b.append("dojo.registerModulePath('extlib','"+JavaScriptUtil.toJavaScriptString(dojoPath)+"');\n");         
            
//          b.append("dojo.registerModulePath('com.ibm.mm','/mum/js/com/ibm/mm');\n");
//          b.append("dojo.registerModulePath('com.ibm.mashups','/mum/js/com/ibm/mashups');");
            
//          b.append("dojo.registerModulePath(\"com.ibm.mm.enabler\",\"/mum/js/com/ibm/mm/enabler\");\n");
//          b.append("dojo.registerModulePath(\"com.ibm.mashups.enabler\",\"/mum/js/com/ibm/mashups/enabler\");\n");    
//          b.append("dojo.registerModulePath(\"com.ibm.mm.data\",\"/mum/js/com/ibm/mm/data\");\n");
            
//          b.append("dojo.registerModulePath('com.ibm.mm','"+JavaScriptUtil.toJavaScriptString(dojoPath)+"/com/ibm/mm');\n");
//          b.append("dojo.registerModulePath('com.ibm.mashups','"+JavaScriptUtil.toJavaScriptString(dojoPath)+"/com/ibm/mashups');");

//          dojoModulePath.setContents(b.toString());
//          resources.add(dojoModulePath);
        }
        return resources;
    }
}
