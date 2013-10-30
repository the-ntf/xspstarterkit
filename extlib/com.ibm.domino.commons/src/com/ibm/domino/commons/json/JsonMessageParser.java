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

import static com.ibm.domino.commons.json.JsonConstants.CONTENT_PROP;
import static com.ibm.domino.commons.json.JsonConstants.CC_PROP;
import static com.ibm.domino.commons.json.JsonConstants.TO_PROP;

import java.io.Reader;

import lotus.domino.Document;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonFactory;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.domino.commons.json.JsonMimeEntityAdapter.ParserContext;

public class JsonMessageParser {
    
    private Reader _reader = null;
    private ParserContext _context = null;

    /**
     * Inner object factory class.
     * 
     * <p>The JSON parser delegates to <code>createObject</code> when it encounters
     * an object in the JSON input.
     */
    private class JsonObjectFactory extends JsonJavaFactory {
        
        private Document _document;
        
        public JsonObjectFactory(Document document) {
            _document = document;
        }
        
        public Object createObject(Object parent, String propertyName) throws JsonException {
            if ( parent == null && propertyName == null ) {
                _context = new ParserContext(_document, "body"); //$NON-NLS-1$
                return new JsonMessageAdapter(_document, _context);
            }
            else if ( parent instanceof JsonMessageAdapter ) {
                if ( CONTENT_PROP.equals(propertyName) ) {
                    
                    // Flush the previous MIME entity to the document
                    
                    JsonMimeEntityAdapter entityAdapter = _context.getCurrentEntityAdapter();
                    if ( entityAdapter != null ) {
                        entityAdapter.flushJsonProperties(false);
                    }
                    
                    // Start parsing a new MIME entity
                    
                    entityAdapter =  new JsonMimeEntityAdapter(_context, new JsonJavaObject());
                    _context.setCurrentEntityAdapter(entityAdapter);
                    return entityAdapter;
                }
                else if ( TO_PROP.equals(propertyName) || CC_PROP.equals(propertyName) ) {
                    return new JsonPersonAdapter();
                }
            }

            return super.createObject(parent, propertyName);
        }
    }
    
    public JsonMessageParser(Reader reader) {
        _reader = reader;
    }
    
    public void fromJson(Document document) throws JsonException {
        
        JsonFactory factory = new JsonObjectFactory(document);
        JsonParser.fromJson(factory, _reader);
    }
    
}
