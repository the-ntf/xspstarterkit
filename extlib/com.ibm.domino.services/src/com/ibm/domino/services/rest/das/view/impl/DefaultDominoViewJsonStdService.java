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






package com.ibm.domino.services.rest.das.view.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.Session;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.services.rest.das.DominoParameters;
import com.ibm.domino.services.rest.das.view.RestViewJsonLegacyService;
import com.ibm.domino.services.rest.das.view.ViewParameters;
import com.ibm.xsp.model.domino.DominoUtils;


/**
 * Domino View Service.
 */
public class DefaultDominoViewJsonStdService extends RestViewJsonLegacyService {
    
    private Session session;
    private Database defaultDatabase;
    
    public DefaultDominoViewJsonStdService(HttpServletRequest httpRequest, HttpServletResponse httpResponse, ViewParameters parameters) {
        super(httpRequest, httpResponse,parameters);
    }

    @Override
    public void recycle() {
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Database getDefaultDatabase() {
        return defaultDatabase;
    }

    @Override
    public void setDefaultDatabase(Database defaultDatabase) {
        this.defaultDatabase = defaultDatabase;
    }

    @Override
    protected void loadDatabase(DominoParameters parameters) throws NotesException {
        String databaseName = parameters.getDatabaseName();
        // In case the database is null, use the default one
        if(StringUtil.isEmpty(databaseName)) {
            Database db = getDefaultDatabase();
            if(db==null) {
                throw new NotesException(0,com.ibm.domino.services.ResourceHandler.getSpecialAudienceString("DefaultDominoViewJsonStdService.Thereisntadefaultdatabaseassigned")); // $NLX-DefaultDominoViewJsonStdService.Thereisntadefaultdatabaseassigned-1$
            }
            this.database = db;
            this.shouldRecycleDatabase = false;
            return;
        }
    
        // Try to open the database
        Session session = getSession();
        this.database = DominoUtils.openDatabaseByName(session,databaseName);
        this.shouldRecycleDatabase = true;
    }
}
