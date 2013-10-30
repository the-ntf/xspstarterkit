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






package com.ibm.domino.services.rest.das.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.domino.services.ServiceException;
import com.ibm.domino.services.rest.das.RestDocumentNavigator;

public class RestViewJsonServiceExt extends RestViewJsonService {
    protected List<IRestViewJsonEventListener> _listeners;

    public RestViewJsonServiceExt(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            ViewParameters parameters) {
        super(httpRequest, httpResponse, parameters);
    }

    public void addEventListener(IRestViewJsonEventListener listener) {
        if (_listeners == null) {
            _listeners = new ArrayList<IRestViewJsonEventListener>();
        }
        _listeners.add(listener);
    }

    public void removeEventListener(IRestViewJsonEventListener listener) {
        if (_listeners != null && _listeners.contains(listener)) {
            _listeners.remove(listener);
        }
    }

    @Override
    protected void createDocument(RestViewNavigator viewNav, RestDocumentNavigator docNav, JsonJavaObject items)
            throws ServiceException, JsonException, IOException {
        for (IRestViewJsonEventListener l : _listeners) {
            boolean more = l.queryCreateDocument(viewNav, docNav, items);
            if (!more) { // anything but true means cancel the create...
                return;
            }
        }
        super.createDocument(viewNav, docNav, items);
        for (IRestViewJsonEventListener l : _listeners) {
            l.postCreateDocument(viewNav, docNav, items);
        }
    }

    @Override
    protected void updateDocument(RestViewNavigator viewNav, RestDocumentNavigator docNav, String id,
            JsonJavaObject items) throws ServiceException, JsonException, IOException {
        for (IRestViewJsonEventListener l : _listeners) {
            boolean more = l.queryUpdateDocument(viewNav, docNav, id, items);
            if (!more) { // anything but true means cancel the create...
                return;
            }
        }
        super.createDocument(viewNav, docNav, items);
        for (IRestViewJsonEventListener l : _listeners) {
            l.postUpdateDocument(viewNav, docNav, id, items);
        }
    }

    @Override
    protected void deleteDocument(RestViewNavigator viewNav, RestDocumentNavigator docNav, String id,
            JsonJavaObject items) throws ServiceException, JsonException, IOException {
        for (IRestViewJsonEventListener l : _listeners) {
            boolean more = l.queryDeleteDocument(viewNav, docNav, id, items);
            if (!more) { // anything but true means cancel the create...
                return;
            }
        }
        super.createDocument(viewNav, docNav, items);
        for (IRestViewJsonEventListener l : _listeners) {
            l.postDeleteDocument(viewNav, docNav, id, items);
        }
    }

}
