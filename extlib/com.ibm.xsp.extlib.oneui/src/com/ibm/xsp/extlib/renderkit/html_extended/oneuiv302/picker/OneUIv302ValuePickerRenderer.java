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





package com.ibm.xsp.extlib.renderkit.html_extended.oneuiv302.picker;

import javax.faces.context.FacesContext;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.component.UIViewRootEx;
import com.ibm.xsp.extlib.component.picker.AbstractPicker;
import com.ibm.xsp.extlib.component.picker.data.IPickerData;
import com.ibm.xsp.extlib.renderkit.html_extended.oneui.picker.OneUIValuePickerRenderer;
import com.ibm.xsp.extlib.renderkit.html_extended.oneuiv302.OneUIv302Resources;
import com.ibm.xsp.extlib.resources.ExtLibResources;
import com.ibm.xsp.extlib.resources.OneUIResources;

public class OneUIv302ValuePickerRenderer extends OneUIValuePickerRenderer {
    
    @Override
    protected String getDefaultDojoType() {
        return "extlib.dijit.OneUIv302PickerList"; // $NON-NLS-1$
    }

    @Override
    protected String encodeDojoType(String dojoType) {
        // Transform the basic controls into OneUI ones
        if(StringUtil.equals(dojoType,"extlib.dijit.PickerCheckbox")) { // $NON-NLS-1$
            return "extlib.dijit.OneUIv302PickerCheckbox"; // $NON-NLS-1$
        }
        if(StringUtil.equals(dojoType,"extlib.dijit.PickerList")) { // $NON-NLS-1$
            return "extlib.dijit.OneUIv302PickerList"; // $NON-NLS-1$
        }
        if(StringUtil.equals(dojoType,"extlib.dijit.PickerListSearch")) { // $NON-NLS-1$
            return "extlib.dijit.OneUIv302PickerListSearch"; // $NON-NLS-1$
        }
        return super.encodeDojoType(dojoType);
    }

    @Override
    protected void encodeExtraResources(FacesContext context, AbstractPicker picker, IPickerData data, UIViewRootEx rootEx, String dojoType) {
        if(StringUtil.equals(dojoType, "extlib.dijit.OneUIv302PickerCheckbox")) { // $NON-NLS-1$
            ExtLibResources.addEncodeResource(rootEx, OneUIv302Resources.oneUIv302PickerCheckbox);
        }
        if(StringUtil.equals(dojoType, "extlib.dijit.OneUIv302PickerList")) { // $NON-NLS-1$
            ExtLibResources.addEncodeResource(rootEx, OneUIv302Resources.oneUIv302PickerList);
        }
        if(StringUtil.equals(dojoType, "extlib.dijit.OneUIv302PickerListSearch")) { // $NON-NLS-1$
            ExtLibResources.addEncodeResource(rootEx, OneUIv302Resources.oneUIv302PickerListSearch);
        }
        super.encodeExtraResources(context, picker, data, rootEx, dojoType);
    }

}
