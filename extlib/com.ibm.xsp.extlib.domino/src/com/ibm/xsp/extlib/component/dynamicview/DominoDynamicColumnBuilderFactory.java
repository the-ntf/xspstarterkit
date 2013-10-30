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






package com.ibm.xsp.extlib.component.dynamicview;

import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;

import com.ibm.xsp.model.domino.DominoViewDataModel;


/**
 * Dynamic XPage view panel adapter factory.
 * <p>
 * This factory is used to create a column builder for the dynamic view panel. 
 * </p>
 * @author priand
 */
public class DominoDynamicColumnBuilderFactory extends DynamicColumnBuilderFactory {

    @Override
    public DynamicColumnBuilder createColumnBuilder(FacesContext context, UIDynamicViewPanel viewPanel, DataModel dataModel) {
        if(dataModel instanceof DominoViewDataModel) {
            return new DominoDynamicColumnBuilder(context,viewPanel);
        }
        return null;
    }
}
