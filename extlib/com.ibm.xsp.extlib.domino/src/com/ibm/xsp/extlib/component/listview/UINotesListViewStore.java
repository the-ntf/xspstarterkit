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






package com.ibm.xsp.extlib.component.listview;

import com.ibm.xsp.extlib.component.domino.UINotesViewStoreComponent;

/**
 * @author akosugi
 * 
 *        ui component handler for notes list view data store control
 */
public class UINotesListViewStore extends UINotesViewStoreComponent {

    public static final String COMPONENT_TYPE = "com.ibm.xsp.extlib.listview.NotesListViewStore"; // $NON-NLS-1$
    public static final String COMPONENT_FAMILY = "com.ibm.xsp.extlib.listview.ListViewStore"; // $NON-NLS-1$
    public static final String RENDERER_TYPE = "com.ibm.xsp.extlib.listview.NotesListViewStore"; //$NON-NLS-1$

    public UINotesListViewStore() {
        setRendererType(RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

}
