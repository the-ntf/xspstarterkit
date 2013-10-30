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





package com.ibm.xsp.extlib.renderkit.html_extended.oneuiv302.data;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.xsp.component.FacesDataIterator;
import com.ibm.xsp.extlib.component.data.AbstractPager;
import com.ibm.xsp.extlib.renderkit.html_extended.oneui.data.OneUIPagerDetailRenderer;

public class OneUIv302PagerDetailRenderer extends OneUIPagerDetailRenderer {
    
    @Override
    protected void writeMain(FacesContext context, ResponseWriter w, AbstractPager pager, FacesDataIterator dataIterator) throws IOException {
        writePagerContent(context, w, pager, dataIterator);
    }

}
