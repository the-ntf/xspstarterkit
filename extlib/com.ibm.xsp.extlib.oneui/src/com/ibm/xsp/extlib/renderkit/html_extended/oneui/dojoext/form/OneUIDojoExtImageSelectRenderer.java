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






package com.ibm.xsp.extlib.renderkit.html_extended.oneui.dojoext.form;

import com.ibm.xsp.extlib.renderkit.dojoext.form.DojoExtImageSelectRenderer;

public class OneUIDojoExtImageSelectRenderer extends DojoExtImageSelectRenderer {

    @Override
    protected Object getProperty(int prop) {
        switch(prop) {
            case PROP_LISTSTYLE:            return "display: inline;"; // $NON-NLS-1$
        }
        return super.getProperty(prop);
    }
}
