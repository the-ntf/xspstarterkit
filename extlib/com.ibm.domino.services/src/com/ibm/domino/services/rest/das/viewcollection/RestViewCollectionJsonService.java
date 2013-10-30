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

import static com.ibm.domino.services.rest.RestParameterConstants.*;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lotus.domino.Database;
import lotus.domino.NotesException;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.services.HttpServiceConstants;
import com.ibm.domino.services.ResponseCode;
import com.ibm.domino.services.ServiceException;
import com.ibm.domino.services.content.DefaultJsonContentFactory;
import com.ibm.domino.services.content.JsonContentFactory;
import com.ibm.domino.services.content.JsonViewCollectionContent;
import com.ibm.domino.services.util.JsonWriter;

public class RestViewCollectionJsonService extends RestViewCollectionService {
    
    private JsonContentFactory factory = DefaultJsonContentFactory.get();

    public RestViewCollectionJsonService(HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, ViewCollectionParameters parameters) {
        super(httpRequest, httpResponse, parameters);
    }

    /**
     * Constructs a <code>RestViewCollectionJsonService</code> object.
     * 
     * <p>Use this constructor if you want the service to use a subclass
     * of <code>JsonViewCollectionContent</code>.  You must implement
     * a factory that creates the desired subclass of 
     * <code>JsonViewCollectionContent</code>. 
     * 
     * @param httpRequest   The HTTP request.
     * @param httpResponse  The HTTP response.
     * @param parameters    View collection parameters (perhaps parsed from a URL).
     * @param factory       The factory the service should use to create
     *                      an instance of <code>JsonViewEntryCollectionContent</code>.
     */
    public RestViewCollectionJsonService(HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, ViewCollectionParameters parameters,
            JsonContentFactory factory) {
        super(httpRequest, httpResponse, parameters);
        if ( factory != null ) {
            this.factory = factory;
        }
    }
    
    @Override
    protected ViewCollectionParameters wrapViewCollectionParameters(ViewCollectionParameters parameters) {
        return new RequestViewParameter(parameters);
    }   

    protected class RequestViewParameter extends ViewCollectionParametersDelegate {
        protected RequestViewParameter(ViewCollectionParameters delegate) {
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
            throw new ServiceException(null, ResponseCode.METHOD_NOT_ALLOWED, com.ibm.domino.services.ResourceHandler.getSpecialAudienceString("RestViewCollectionJsonService.Method0isnotallowedwithDatabaseRe"), method); // $NLX-RestViewCollectionJsonService.Method0isnotallowedwithDatabaseRe-1$
        }
    }


    private void renderServiceGet() throws ServiceException {
        try {
            ViewCollectionParameters parameters = getParameters();
            String contentType = ""; // $NON-NLS-1$
            if(StringUtil.isEmpty(contentType)) {
                contentType = HttpServiceConstants.CONTENTTYPE_APPLICATION_JSON;
            }
            getHttpResponse().setContentType(contentType);
            getHttpResponse().setCharacterEncoding(HttpServiceConstants.ENCODING_UTF8);
            
            Writer writer = new OutputStreamWriter(getOutputStream(),HttpServiceConstants.ENCODING_UTF8);
            boolean compact = parameters.isCompact();
            JsonWriter jwriter = new JsonWriter(writer,compact);
            
            try {
                Database db = getDatabase(parameters);
                JsonViewCollectionContent content = factory.createViewCollectionContent(db, this.getHttpRequest().getRequestURI());
                content.writeViewCollection(jwriter);
            } catch (NotesException e) {
                throw new ServiceException(e,""); // $NON-NLS-1$
            }   
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException(e,""); // $NON-NLS-1$
        }

    }
    
}
