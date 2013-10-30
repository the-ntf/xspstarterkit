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






package com.ibm.domino.services.content;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Session;
import lotus.domino.View;

import com.ibm.domino.services.rest.das.document.RestDocumentService;
import com.ibm.domino.services.rest.das.view.RestViewService;

public abstract class JsonContentFactory {
    
    public JsonViewEntryCollectionContent createViewEntryCollectionContent(
            View view, RestViewService service) {
        return new JsonViewEntryCollectionContent(view, service);
    }

    public JsonDatabaseCollectionContent createDatabaseCollectionContent(
            Session session, String baseUri, String resourcePath) {
        return new JsonDatabaseCollectionContent(session, baseUri, resourcePath);
    }

    public JsonDocumentContent createDocumentContent(Document document, RestDocumentService service) {
        return new JsonDocumentContent(document, service);
    }

    public JsonDocumentCollectionContent createDocumentCollectionContent(Database database, String uri, String search, String since, int max) {
        return new JsonDocumentCollectionContent(database, uri, search, since, max);
    }

    public JsonViewCollectionContent createViewCollectionContent(
            Database database, String uri) {
        return new JsonViewCollectionContent(database, uri);
    }

    public JsonViewDesignContent createViewDesignContent(View view) {
        return new JsonViewDesignContent(view);
    }
}
