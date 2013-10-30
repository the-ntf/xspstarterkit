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

import static com.ibm.domino.services.rest.RestParameterConstants.*;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.services.HttpServiceConstants;
import com.ibm.domino.services.ResponseCode;
import com.ibm.domino.services.ServiceException;
import com.ibm.domino.services.content.DefaultJsonContentFactory;
import com.ibm.domino.services.content.JsonContentFactory;
import com.ibm.domino.services.content.JsonDatabaseCollectionContent;
import com.ibm.domino.services.util.JsonWriter;

public class RestDatabaseCollectionJsonService extends RestDatabaseCollectionService {

    // TODO:  Should not use hard coded path here
    
    private static final String RESOURCE_PATH = "/api/data/collections"; //$NON-NLS-1$
    
    private JsonContentFactory factory = DefaultJsonContentFactory.get();

    public RestDatabaseCollectionJsonService(HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, DatabaseCollectionParameters parameters) {
        super(httpRequest, httpResponse, parameters);
    }

    /**
     * Constructs a <code>RestDatabaseCollectionJsonService</code> object.
     * 
     * <p>Use this constructor if you want the service to use a subclass
     * of <code>JsonDatabaseCollectionContent</code>.  You must implement
     * a factory that creates the desired subclass of 
     * <code>JsonDatabaseCollectionContent</code>. 
     * 
     * @param httpRequest   The HTTP request.
     * @param httpResponse  The HTTP response.
     * @param parameters    Database collection parameters (perhaps parsed from a URL).
     * @param factory       The factory the service should use to create
     *                      an instance of <code>JsonDatabaseCollectionContent</code>.
     */
    public RestDatabaseCollectionJsonService(HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, DatabaseCollectionParameters parameters,
            JsonContentFactory factory) {
        super(httpRequest, httpResponse, parameters);
        if ( factory != null ) {
            this.factory = factory;
        }
    }

    @Override
    protected DatabaseCollectionParameters wrapDatabaseCollectionParameters(DatabaseCollectionParameters parameters) {
        return new RequestDatabaseParameter(parameters);
    }   

    protected class RequestDatabaseParameter extends DatabaseCollectionParametersDelegate {
        protected RequestDatabaseParameter(DatabaseCollectionParameters delegate) {
            super(delegate);
        }
        @Override
        public boolean isCompact() {
            String param = getHttpRequest().getParameter(PARAM_COMPACT); 
            if (StringUtil.isNotEmpty(param)) {
                return param.contentEquals(PARAM_VALUE_TRUE);
            }
            return super.isCompact();
        }
    }
    
    @Override
    public void renderService() throws ServiceException {
        
        String method = getHttpRequest().getMethod();
        if (HttpServiceConstants.HTTP_GET.equalsIgnoreCase(method)) {
            renderServiceGet();
        } else {
            throw new ServiceException(null, ResponseCode.METHOD_NOT_ALLOWED, com.ibm.domino.services.ResourceHandler.getSpecialAudienceString("RestDatabaseCollectionJsonService.Method0isnotallowedwithDatabaseRe"), method); // $NLX-RestDatabaseCollectionJsonService.Method0isnotallowedwithDatabaseRe-1$
        }
    }

    private void renderServiceGet() throws ServiceException {
        try {
            DatabaseCollectionParameters parameters = getParameters();
            String contentType = "";
            if(StringUtil.isEmpty(contentType)) {
                contentType = HttpServiceConstants.CONTENTTYPE_APPLICATION_JSON;
            }
            getHttpResponse().setContentType(contentType);
            getHttpResponse().setCharacterEncoding(HttpServiceConstants.ENCODING_UTF8);
            
            Writer writer = new OutputStreamWriter(getOutputStream(),HttpServiceConstants.ENCODING_UTF8);
            boolean compact = parameters.isCompact();
            JsonWriter jwriter = new JsonWriter(writer,compact);
            
            JsonDatabaseCollectionContent content = factory.createDatabaseCollectionContent(this.defaultSession, "", RESOURCE_PATH); // $NON-NLS-1$
            content.writeDatabaseCollection(jwriter);
    
        } catch (UnsupportedEncodingException e) {
                throw new ServiceException(e,"");
        }
    }
    
}
