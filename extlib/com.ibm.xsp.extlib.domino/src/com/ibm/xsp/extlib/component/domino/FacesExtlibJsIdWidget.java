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
* Date: 19 Sep 2011
* FacesExtlibJsIdWidget.java
*/

package com.ibm.xsp.extlib.component.domino;

import javax.faces.context.FacesContext;

/**
 *
 * @author Maire Kehoe (mkehoe@ie.ibm.com)
 */
public interface FacesExtlibJsIdWidget {
    
    public String getDojoWidgetJsId(FacesContext context);
}
