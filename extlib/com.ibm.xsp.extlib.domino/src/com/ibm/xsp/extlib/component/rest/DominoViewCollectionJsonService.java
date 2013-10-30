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
import com.ibm.domino.services.rest.das.viewcollection.RestViewCollectionJsonService;
import com.ibm.domino.services.rest.das.viewcollection.ViewCollectionParameters;
import com.ibm.xsp.model.domino.DominoUtils;


/**
 * Content coming from a collection of views and rendered as Domino JSON.
 * 
 * @author Stephen Auriemma
 */
public class DominoViewCollectionJsonService extends DominoService {

    public DominoViewCollectionJsonService() {
    }
    
    private class Engine extends RestViewCollectionJsonService {
        Engine(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Parameters params) {
            super(httpRequest,httpResponse,params);
            setDefaultSession(DominoUtils.getCurrentSession());
            setDefaultDatabase(DominoUtils.getCurrentDatabase());
        }
    }
    
    protected class Parameters implements ViewCollectionParameters {
        Parameters(FacesContext context, UIBaseRestService parent, HttpServletRequest httpRequest) {
        }
        public String getDatabaseName() {
            return DominoViewCollectionJsonService.this.getDatabaseName();
        }
        public String getContentType() {
            return DominoViewCollectionJsonService.this.getContentType();
        }
        public boolean isCompact() {
            return DominoViewCollectionJsonService.this.isCompact();
        }
    }

    public RestServiceEngine createEngine(FacesContext context, UIBaseRestService parent, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        Parameters params = new Parameters(context, parent, httpRequest);
        return new Engine(httpRequest,httpResponse,params);
    }
}
