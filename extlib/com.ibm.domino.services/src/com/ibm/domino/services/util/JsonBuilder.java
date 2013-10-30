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






package com.ibm.domino.services.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import lotus.domino.DateTime;
import lotus.domino.NotesException;

import com.ibm.commons.util.AbstractIOException;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.domino.services.rest.RestServiceConstants;



/**
 * Specialized JSON writer within a StringBuilder.
 * 
 * @author Philippe Riand
 */
public class JsonBuilder extends JsonGenerator.StringBuilderGenerator {
    
    private int objectLevels = 0;
    private boolean first[] = new boolean[32]; // max 32 for now...
    
    public JsonBuilder(StringBuilder b, boolean compact) {
        super(null,b,compact);
    }

    public void startObject() throws IOException {
        nl();
        indent();
        out('{');
        first[++objectLevels]=true;
        incIndent();
    }
    public void endObject() throws IOException {
        nl();
        decIndent();
        indent();
        out('}');
        first[--objectLevels]=false;
    }
    public void startArray() throws IOException {
        nl();
        indent();
        out('[');
        first[++objectLevels]=true;
        incIndent();
    }
    public void endArray() throws IOException {
        nl();
        decIndent();
        indent();
        out(']');
        first[--objectLevels]=false;
    }
    public void startArrayItem() throws IOException {
        if(!first[objectLevels]) {
            out(',');
        }
    }
    public void endArrayItem() throws IOException {
        first[objectLevels]=false;
    }
    public void startProperty(String propertyName) throws IOException {
        if(!first[objectLevels]) {
            out(',');
        } else {
            first[objectLevels]=false;
        }
        nl();
        incIndent();
        indent();
        outPropertyName(propertyName);
        out(':');
    }
    public void endProperty() throws IOException {
        decIndent();
    }

    // Should be moved to the core libs
    @Override
    public void outNumberLiteral(double d) throws IOException {
        long l = (long)d;
        if((double)l==d) {
            String s = Long.toString(l);
            out(s);
        } else {
            String s = Double.toString(d);
            out(s);
        }
    }
    
    // Should be moved to the core libs
    public void outDateLiteral(Date value) throws IOException {
        String s = dateToString(value);
        outStringLiteral(s);
    }

    public void outDateLiteral(DateTime value) throws IOException {
        String s = dateToString(value);
        out(s);
    }

    public void outDominoValue(Object value) throws IOException {
        try {
            if(value==null) {
                outNull();
                return;
            }
            if(value instanceof String) {
                outStringLiteral((String)value);
                return;
            }
            if(value instanceof Number) {
                outNumberLiteral(((Number)value).doubleValue());
                return;
            }
            if(value instanceof Boolean) {
                outBooleanLiteral(((Boolean)value).booleanValue());
                return;
            }
            if(value instanceof Date) {
                outStringLiteral(dateToString((Date)value));
                return;
            }
            if(value instanceof DateTime) {
                try {
                    outStringLiteral(dateToString(((DateTime)value).toJavaDate()));
                    return;
                } catch(NotesException ex) {
                    throw new AbstractIOException(ex,"");
                }
            }           
            if(value instanceof Vector) {
                startArray();
                Vector v = (Vector)value;
                int count = v.size();
                for(int i=0; i<count; i++) {
                    startArrayItem();
                    outDominoValue(v.get(i));
                    endArrayItem();
                }
                endArray();
                return;
            }
            // Should not happen...
            outStringLiteral("???");
        } catch(JsonException ex) {
            throw new AbstractIOException(ex,"");
        }
    }   

    
    //TODO: What the TZ should be??
    private static SimpleDateFormat ISO8601 = new SimpleDateFormat(RestServiceConstants.TIME_FORMAT_B); //$NON-NLS-1$
    
    
    public String dateToString(Date value) throws IOException {
        return ISO8601.format((Date)value);
    }
    
    public String dateToString(DateTime value) throws IOException {
        try {
            return ISO8601.format(((DateTime)value).toJavaDate());
        } catch(NotesException ex) {
            throw new AbstractIOException(ex,"");
        }
    }
    
    public Date toJavaDate(DateTime value) throws IOException {
        try {
            return value.toJavaDate();
        } catch(NotesException ex) {
            throw new AbstractIOException(ex,"");
        }
    }
}   
