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






package com.ibm.xsp.extlib.renderkit.html_extended.oneui.tagcloud;

import com.ibm.xsp.extlib.renderkit.html_extended.cloud.AbstractTagCloudRenderer;

/**
 * OneUI Tag Cloud Renderer.
 * 
 * @author priand
 */
public class OneUITagCloudRenderer extends AbstractTagCloudRenderer {

    @Override
    protected Object getProperty(int prop) {
        switch(prop) {
            case PROP_INNERCLASS:   return "lotusTagCloud lotusChunk"; // $NON-NLS-1$
            case PROP_LISTTAG:      return "ul"; // $NON-NLS-1$
            case PROP_ENTRYTAG:     return "li"; // $NON-NLS-1$
            case PROP_TAGTITLE:     return super.getProperty(PROP_TAGTITLE_ENTRIES); 
            case PROP_SLIDERCLASS:  return "lotusChunk"; // $NON-NLS-1$
        }
        return super.getProperty(prop);
    }
}
