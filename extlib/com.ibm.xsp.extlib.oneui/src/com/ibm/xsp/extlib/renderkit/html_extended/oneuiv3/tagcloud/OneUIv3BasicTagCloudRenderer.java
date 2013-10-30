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






package com.ibm.xsp.extlib.renderkit.html_extended.oneuiv3.tagcloud;

import com.ibm.xsp.extlib.renderkit.html_extended.oneui.tagcloud.OneUITagCloudRenderer;


/**
 * OneUI V3 Tag Cloud Renderer.
 *
 * @author priand
 */
public class OneUIv3BasicTagCloudRenderer extends OneUITagCloudRenderer {

    @Override
    protected Object getProperty(int prop) {
        switch(prop) {
            case PROP_OUTERCLASS:   return "lotusSectionBody"; // $NON-NLS-1$
            case PROP_SLIDERCLASS:  return "lotusChunk"; // $NON-NLS-1$
        }
        return super.getProperty(prop);
    }
}
