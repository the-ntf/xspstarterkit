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

import com.ibm.xsp.extlib.renderkit.html_extended.data.FormTableRenderer;
import com.ibm.xsp.extlib.resources.OneUIResources;


/**
 * One UI form table renderer.
 */
public class OneUIFormTableRenderer extends FormTableRenderer {

    @Override
    protected Object getProperty(int prop) {
        switch(prop) {
            case PROP_BLANKIMG:                 return OneUIResources.get().BLANK_GIF;
            
            case PROP_TABLESTYLECLASS:          return "lotusFormTable"; // $NON-NLS-1$
            
            case PROP_STYLECLASSERRORSUMMARY:   return "lotusFormErrorSummary"; // $NON-NLS-1$
            case PROP_ERRORSUMMARYMAINTEXT:     return com.ibm.xsp.extlib.oneui.ResourceHandler.getString("OneUIFormTableRenderer.Pleasecheckthefollowing"); // $NLS-OneUIFormTableRenderer.Pleasecheckthefollowing-1$
            case PROP_ERRORSUMMARYCLASS:        return "lotusFormRequired"; // $NON-NLS-1$
            case PROP_WARNSUMMARYMAINTEXT:      return getProperty(PROP_ERRORSUMMARYMAINTEXT);
            case PROP_WARNSUMMARYCLASS:         return null;
            case PROP_INFOSUMMARYMAINTEXT:      return getProperty(PROP_ERRORSUMMARYMAINTEXT);
            case PROP_INFOSUMMARYCLASS:         return null;
            
            case PROP_TAGFORMTITLE:             return "h2"; // $NON-NLS-1$
            case PROP_STYLECLASSHEADER:         return "lotusFormTitle"; // $NON-NLS-1$
            case PROP_STYLECLASSFORMDESC:       return "lotusMeta"; // $NON-NLS-1$
            case PROP_TAGFORMDESC:              return "div"; // $NON-NLS-1$
            
            case PROP_STYLECLASSFOOTER:         return "lotusFormFooter"; // $NON-NLS-1$

            case PROP_ERRORROWCLASS:            return "lotusFormError"; // $NON-NLS-1$

            case PROP_ERRORIMGSTYLE:            return null;
            case PROP_ERRORIMGCLASS:            return null;
            case PROP_ERRORIMGSRC:              return OneUIResources.get().ICON_ERROR;
            case PROP_ERRORIMGALT:              return com.ibm.xsp.extlib.oneui.ResourceHandler.getString("OneUIFormTableRenderer.Error"); // $NLS-OneUIFormTableRenderer.Error-1$
            case PROP_ERRORMSGALTTEXT:          return com.ibm.xsp.extlib.oneui.ResourceHandler.getString("OneUIFormTableRenderer.Error.1"); // $NLS-OneUIFormTableRenderer.Error.1-1$
            case PROP_ERRORMSGALTTEXTCLASS:     return "lotusAltText"; // $NON-NLS-1$
            case PROP_FATALMSGALTTEXT:          return com.ibm.xsp.extlib.oneui.ResourceHandler.getString("OneUIFormTableRenderer.Fatal_messagePrefix"); // $NLS-OneUIFormTableRenderer.Fatal_messagePrefix-1$

            case PROP_WARNIMGSTYLE:             return null;
            case PROP_WARNIMGCLASS:             return null;
            case PROP_WARNIMGSRC:               return OneUIResources.get().ICON_WARN;
            case PROP_WARNIMGALT:               return com.ibm.xsp.extlib.oneui.ResourceHandler.getString("OneUIFormTableRenderer.Warning"); // $NLS-OneUIFormTableRenderer.Warning-1$
            case PROP_WARNMSGALTTEXT:           return com.ibm.xsp.extlib.oneui.ResourceHandler.getString("OneUIFormTableRenderer.Warning.1"); // $NLS-OneUIFormTableRenderer.Warning.1-1$
            case PROP_WARNMSGALTTEXTCLASS:      return "lotusAltText"; // $NON-NLS-1$

            case PROP_INFOIMGSTYLE:             return null;
            case PROP_INFOIMGCLASS:             return null;
            case PROP_INFOIMGSRC:               return OneUIResources.get().ICON_INFO;
            case PROP_INFOIMGALT:               return com.ibm.xsp.extlib.oneui.ResourceHandler.getString("OneUIFormTableRenderer.Information.1"); // $NLS-OneUIFormTableRenderer.Information.1-1$
            case PROP_INFOMSGALTTEXT:           return com.ibm.xsp.extlib.oneui.ResourceHandler.getString("OneUIFormTableRenderer.Information.2"); // $NLS-OneUIFormTableRenderer.Information.2-1$
            case PROP_INFOMSGALTTEXTCLASS:      return "lotusAltText"; // $NON-NLS-1$

            case PROP_FIELDROWCLASS:            return "lotusFormFieldRow"; // $NON-NLS-1$
            case PROP_FIELDLABELWIDTH:          return "15%";
            case PROP_FIELDLABELCLASS:          return "lotusFormLabel"; // $NON-NLS-1$
            case PROP_FIELDLABELREQUIREDCLASS:  return "lotusFormRequired"; // $NON-NLS-1$
            case PROP_FIELDLABELREQUIREDTEXT:   return "*";

            case PROP_HELPIMGCLASS:             return null;
            case PROP_HELPIMGSRC:               return OneUIResources.get().ICON_HELP;
            case PROP_HELPIMGALT:               return com.ibm.xsp.extlib.oneui.ResourceHandler.getString("OneUIFormTableRenderer.Help"); // $NLS-OneUIFormTableRenderer.Help-1$
            case PROP_HELPMSGALTTEXT:           return com.ibm.xsp.extlib.oneui.ResourceHandler.getString("OneUIFormTableRenderer.Help.1"); // $NLS-OneUIFormTableRenderer.Help.1-1$
            case PROP_HELPMSGALTTEXTCLASS:      return "lotusAltText"; // $NON-NLS-1$
        }
        
        return super.getProperty(prop);
    }
}
