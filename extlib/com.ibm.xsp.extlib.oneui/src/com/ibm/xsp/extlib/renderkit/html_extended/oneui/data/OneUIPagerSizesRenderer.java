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






package com.ibm.xsp.extlib.renderkit.html_extended.oneui.data;

import com.ibm.xsp.extlib.renderkit.html_extended.data.PagerSizesRenderer;


public class OneUIPagerSizesRenderer extends PagerSizesRenderer {

    @Override
    protected Object getProperty(int prop) {
        switch(prop) {
            case PROP_LISTCLASS:        return "lotusInlinelist"; // $NON-NLS-1$
            case PROP_FIRSTCLASS:       return "lotusFirst"; // $NON-NLS-1$
            case PROP_LASTCLASS:        return "lotusLast"; // $NON-NLS-1$
        }
        return super.getProperty(prop);
    }
}
