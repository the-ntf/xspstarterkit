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






package com.ibm.xsp.extlib.renderkit.html_extended.oneui.dialog;

import com.ibm.xsp.extlib.renderkit.html_extended.dialog.DialogButtonBarRenderer;


/**
 * OneUI rendering for a dialog button bar.
 */
public class OneUIDialogButtonBarRenderer extends DialogButtonBarRenderer {

    @Override
    protected Object getProperty(int prop) {
        switch(prop) {
            //case PROP_PANELSTYLE:             return "margin-top: 10px";
            case PROP_PANELSTYLECLASS:          return "lotusDialogFooter"; // $NON-NLS-1$
        }
        return super.getProperty(prop);
    }
    
}
