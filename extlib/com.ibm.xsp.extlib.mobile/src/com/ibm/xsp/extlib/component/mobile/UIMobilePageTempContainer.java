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





/*
* Author: Maire Kehoe (mkehoe@ie.ibm.com)
* Date: 11 Jul 2013
* UIMobilePageTempContainer.java
*/
package com.ibm.xsp.extlib.component.mobile;

import javax.faces.component.UIComponentBase;

/**
 * While the {@link UIMobilePageContent} is dynamically loading the xpage source xe:appPage content,
 * an instance of this control is used as a container for the temporary loaded xe:appPage control.
 * This instance is only added to the control tree for the duration of the dynamic loading
 * of the control tree subset under the xe:appPage control. It is removed afterwards.
 * @author Maire Kehoe (mkehoe@ie.ibm.com)
 * @ibm-not-published
 */
public class UIMobilePageTempContainer extends UIComponentBase {
    public static final String COMPONENT_FAMILY = "com.ibm.xsp.extlib.Mobile"; //$NON-NLS-1$
    
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
}
