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

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lotus.domino.Document;

import com.ibm.domino.services.rest.RestServiceEngine;
import com.ibm.domino.services.rest.das.view.RestViewXmlLegacyService;
import com.ibm.xsp.model.domino.DominoUtils;


/**
 * Content coming from a view and rendered as Domino legacy XML.
 * 
 * @author Philippe Riand
 */
public class DominoViewXmlLegacyService extends DominoViewService {

    public DominoViewXmlLegacyService() {
    }
    
    @Override
    public boolean writePageMarkup(FacesContext context, UIBaseRestService parent, ResponseWriter writer) throws IOException {
        // No page markup is rendered
        return false; 
    }
    
    ///////////////////////////////////////////////////////////////////////
    @Override
    public RestServiceEngine createEngine(FacesContext context, UIBaseRestService parent, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        Parameters params = new Parameters(context, parent, httpRequest);
        return new Engine(httpRequest,httpResponse,params);
    }
    
    private class Engine extends RestViewXmlLegacyService {
        Engine(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Parameters params) {
            super(httpRequest,httpResponse,params);
            setDefaultSession(DominoUtils.getCurrentSession());
            setDefaultDatabase(DominoUtils.getCurrentDatabase());
        }
        @Override
        public boolean queryNewDocument() {
            return DominoViewXmlLegacyService.this.queryNewDocument();
        }
        @Override
        public boolean queryOpenDocument(String id) {
            return DominoViewXmlLegacyService.this.queryOpenDocument(id);
        }
        @Override
        public boolean querySaveDocument(Document doc) {
            return DominoViewXmlLegacyService.this.querySaveDocument(doc);
        }
        @Override
        public boolean queryDeleteDocument(String id) {
            return DominoViewXmlLegacyService.this.queryDeleteDocument(id);
        }
        @Override
        public void postNewDocument(Document doc) {
            DominoViewXmlLegacyService.this.postNewDocument(doc);
        }
        @Override
        public void postOpenDocument(Document doc)  {
            DominoViewXmlLegacyService.this.postOpenDocument(doc);
        }
        @Override
        public void postSaveDocument(Document doc)  {
            DominoViewXmlLegacyService.this.postSaveDocument(doc);
        }   
        @Override
        public void postDeleteDocument(String id) {
            DominoViewXmlLegacyService.this.postDeleteDocument(id);
        }
    }
}
