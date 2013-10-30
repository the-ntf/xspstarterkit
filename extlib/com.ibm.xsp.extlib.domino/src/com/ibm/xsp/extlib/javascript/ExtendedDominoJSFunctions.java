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





package com.ibm.xsp.extlib.javascript;

import com.ibm.designer.runtime.extensions.JavaScriptProvider;
import com.ibm.jscript.JSContext;


/**
 * Javascript extensions.
 */
@SuppressWarnings("restriction") // $NON-NLS-1$
public class ExtendedDominoJSFunctions implements JavaScriptProvider {

    public void registerWrappers(JSContext jsContext) {
        jsContext.getRegistry().registerGlobalPrototype("@FunctionsEx", new NotesFunctionsEx(jsContext)); // $NON-NLS-1$
    }
}
