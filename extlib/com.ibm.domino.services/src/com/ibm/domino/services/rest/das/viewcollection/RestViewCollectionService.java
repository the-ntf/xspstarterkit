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






package com.ibm.domino.services.rest.das.viewcollection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.domino.services.ServiceException;
import com.ibm.domino.services.rest.das.RestDominoService;

public abstract class RestViewCollectionService extends RestDominoService {

    private ViewCollectionParameters parameters;
    
    public RestViewCollectionService(HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, ViewCollectionParameters parameters) {
        super(httpRequest, httpResponse);
        this.parameters = wrapViewCollectionParameters(parameters);
    }

    protected ViewCollectionParameters wrapViewCollectionParameters(ViewCollectionParameters parameters) {
        return parameters;
    }

    @Override
    public abstract void renderService() throws ServiceException;

    @Override
    public ViewCollectionParameters getParameters() {
        return parameters;
    }

}

