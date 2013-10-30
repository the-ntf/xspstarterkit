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

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.domino.services.rest.das.RestDocumentNavigator;

public interface IRestViewJsonEventListener {

    public boolean queryCreateDocument(RestViewNavigator viewNav, RestDocumentNavigator docNav, JsonJavaObject items);

    public void postCreateDocument(RestViewNavigator viewNav, RestDocumentNavigator docNav, JsonJavaObject items);

    public boolean queryUpdateDocument(RestViewNavigator viewNav, RestDocumentNavigator docNav, String id,
            JsonJavaObject items);

    public void postUpdateDocument(RestViewNavigator viewNav, RestDocumentNavigator docNav, String id,
            JsonJavaObject items);

    public boolean queryDeleteDocument(RestViewNavigator viewNav, RestDocumentNavigator docNav, String id,
            JsonJavaObject items);

    public void postDeleteDocument(RestViewNavigator viewNav, RestDocumentNavigator docNav, String id,
            JsonJavaObject items);

}
