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






package com.ibm.domino.services.rpc;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonFactory;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.domino.services.HttpServiceEngine;
import com.ibm.domino.services.ServiceException;
import com.ibm.jscript.std.ObjectObject;

import static com.ibm.domino.services.rest.RestServiceConstants.*;
import static com.ibm.domino.services.HttpServiceConstants.*;

/**
 * RPC Service Engine.
 */
public abstract class RpcServiceEngine extends HttpServiceEngine {
    
    public RpcServiceEngine(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(httpRequest,httpResponse);
    }
    
    @Override
    public void renderService() throws ServiceException {
        JsonFactory factory = getJsonFactory();

        // Parse the body 
        try {
            InputStream is = getHttpRequest().getInputStream();  
            Object o = (ObjectObject)JsonParser.fromJson(factory, new InputStreamReader(is,ENCODING_UTF8));
            
            int id = (int)factory.getNumber(factory.getProperty(o, JSON_FACTORY_PROPERTY_ID));
            String methodName = factory.getString((factory.getProperty(o, JSON_FACTORY_PROPERTY_METHOD)));
            Object params = factory.getProperty(o, JSON_FACTORY_PROPERTY_PARAMS);

            Object res = factory.createObject(null, null);
            factory.setProperty(res, JSON_FACTORY_PROPERTY_ID, factory.createNumber(id));
    
            RpcMethod method = findMethod(methodName);
    
            if(method!=null) {
                Object result = method.invoke(getHttpRequest(), id, params);
                factory.setProperty(res, JSON_FACTORY_PROPERTY_RESULT, result);
            } else {
                factory.setProperty(res, JSON_FACTORY_PROPERTY_ERROR, factory.createString(StringUtil.format(com.ibm.domino.services.ResourceHandler.getSpecialAudienceString("RpcServiceEngine.Unknownmethod0"), methodName))); // $NLX-RpcServiceEngine.Unknownmethod0-1$
            }
            
            getHttpResponse().setContentType(CONTENTTYPE_TEXT_JSON);
            getHttpResponse().setCharacterEncoding(ENCODING_UTF8);
            
            OutputStream os = getOutputStream();
            Writer w = new OutputStreamWriter(os,ENCODING_UTF8);
            JsonGenerator.toJson(factory, w, res, false); // last is "compact"
            w.flush();
            
        } catch(Exception ex) {
            throw new ServiceException(ex,""); // $NON-NLS-1$
        }
    }
    
    protected abstract JsonFactory getJsonFactory() throws ServiceException;
    protected abstract RpcMethod findMethod(String methodName) throws ServiceException; 
}
