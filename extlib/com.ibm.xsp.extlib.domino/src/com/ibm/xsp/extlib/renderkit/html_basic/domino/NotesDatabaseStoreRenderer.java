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





package com.ibm.xsp.extlib.renderkit.html_basic.domino;

import java.io.IOException;

import javax.faces.context.FacesContext;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.Session;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.xsp.extlib.component.domino.UINotesDatabaseStoreComponent;
import com.ibm.xsp.model.domino.DominoUtils;
import com.ibm.xsp.renderkit.FacesRenderer;

public class NotesDatabaseStoreRenderer extends FacesRenderer {

    public String getDbUrlInName(FacesContext context, UINotesDatabaseStoreComponent component) throws IOException{
        String dbName = component.getDatabaseName();
        StringBuilder b = new StringBuilder();
        try {
            Database database = null;
            if (StringUtil.isEmpty(dbName)) {
                database = NotesContext.getCurrent().getCurrentDatabase();
            } else {
                Session session = NotesContext.getCurrent().getCurrentSession();
                database = DominoUtils.openDatabaseByName(session, dbName);
            }
            String url = database.getHttpURL();
            String[] paths = url.split("/");
            for (int i = 0; i < 3 && i < paths.length; i++) {
                b.append(paths[i] + "/");
            }
            b.append(database.getFilePath().replaceAll("\\\\", "/"));
        } catch (NotesException e) {
            IOException ioe = new IOException(e.getMessage());
            ioe.initCause(e);
            throw ioe;
        }
        return b.toString();
    }

    public String getDbUrl(FacesContext context, UINotesDatabaseStoreComponent component) throws IOException{
        String dbName = component.getDatabaseName();
        StringBuilder b = new StringBuilder();
        try {
            Database database = null;
            if (StringUtil.isEmpty(dbName)) {
                database = NotesContext.getCurrent().getCurrentDatabase();
            }else{
                Session session = NotesContext.getCurrent().getCurrentSession();
                database = DominoUtils.openDatabaseByName(session, dbName);             
            }
            String url = database.getHttpURL();
            int idx = url.indexOf("?OpenDatabase"); // $NON-NLS-1$

            b.append(idx == -1 ? url : url.substring(0, idx));
        } catch (NotesException e) {
            IOException ioe = new IOException(e.getMessage());
            ioe.initCause(e);
            throw ioe;
        }
        
        return b.toString();
    }

}
