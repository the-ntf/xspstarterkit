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






package com.ibm.xsp.extlib.component.rest;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.domino.services.rest.RestServiceEngine;
import com.ibm.domino.services.rest.das.databasecollection.DatabaseCollectionParameters;
import com.ibm.domino.services.rest.das.databasecollection.RestDatabaseCollectionJsonService;
import com.ibm.xsp.model.domino.DominoUtils;


/**
 * Content coming from a collection of views and rendered as Domino JSON.
 * 
 * @author Stephen Auriemma
 */
public class DominoDatabaseCollectionJsonService extends DominoService {

    public DominoDatabaseCollectionJsonService() {
    }
    
    private class Engine extends RestDatabaseCollectionJsonService {
        Engine(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Parameters params) {
            super(httpRequest,httpResponse,params);
            setDefaultSession(DominoUtils.getCurrentSession());
        }
    }
    
    protected class Parameters implements DatabaseCollectionParameters {
        Parameters(FacesContext context, UIBaseRestService parent, HttpServletRequest httpRequest) {
        }
        public String getDatabaseName() {
            return DominoDatabaseCollectionJsonService.this.getDatabaseName();
        }
        public String getContentType() {
            return DominoDatabaseCollectionJsonService.this.getContentType();
        }
        public boolean isCompact() {
            return DominoDatabaseCollectionJsonService.this.isCompact();
        }
    }

    public RestServiceEngine createEngine(FacesContext context, UIBaseRestService parent, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        Parameters params = new Parameters(context, parent, httpRequest);
        return new Engine(httpRequest,httpResponse,params);
    }
}
