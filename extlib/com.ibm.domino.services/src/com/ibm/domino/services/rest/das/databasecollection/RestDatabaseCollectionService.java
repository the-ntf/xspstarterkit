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






package com.ibm.domino.services.rest.das.databasecollection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.domino.services.ServiceException;
import com.ibm.domino.services.rest.das.RestDominoService;

public abstract class RestDatabaseCollectionService extends RestDominoService {

    private DatabaseCollectionParameters parameters;
    
    protected RestDatabaseCollectionService(HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, DatabaseCollectionParameters parameters) {
        super(httpRequest, httpResponse);
        this.parameters = wrapDatabaseCollectionParameters(parameters);
    }

    protected DatabaseCollectionParameters wrapDatabaseCollectionParameters(DatabaseCollectionParameters parameters) {
        return parameters;
    }

    @Override
    public abstract void renderService() throws ServiceException;
    
    @Override
    public DatabaseCollectionParameters getParameters() {
        return parameters;
    }
}
