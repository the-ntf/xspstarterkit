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






package com.ibm.domino.commons.json;

import static com.ibm.domino.commons.json.JsonConstants.JSON_DELEGATE_ACCESS;

import java.io.Reader;

import lotus.domino.Document;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonFactory;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.domino.commons.model.Delegate;

/**
 * Parses the JSON representation of a delegate
 */
public class JsonDelegateParser {
    
    Reader _reader;
    JsonDelegateAdapter _adapter;

    private class JsonObjectFactory extends JsonJavaFactory {
        public Object createObject(Object parent, String propertyName) throws JsonException {
            if ( parent == null && propertyName == null ) {
                _adapter =  new JsonDelegateAdapter();
                return _adapter;
            }
            else if ( parent instanceof JsonDelegateAdapter ) {
                if ( JSON_DELEGATE_ACCESS.equals(propertyName) ) {
                    return new JsonDelegateAccessAdapter();
                }
            }

            return super.createObject(parent, propertyName);
        }
    }

    public JsonDelegateParser(Reader reader) {
        _reader = reader;
    }
    
    public Delegate fromJson() throws JsonException {
        Delegate delegate = null;
        
        JsonFactory factory = new JsonObjectFactory();
        JsonParser.fromJson(factory, _reader);
        
        if ( _adapter != null ) {
            delegate = _adapter.compose();
        }
        
        return delegate;
    }
}
