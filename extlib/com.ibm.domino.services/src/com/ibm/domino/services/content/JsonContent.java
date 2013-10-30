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






package com.ibm.domino.services.content;

import java.io.IOException;

import com.ibm.domino.services.util.JsonWriter;

public class JsonContent {

    /**
     * Writes a JSON string property.
     * 
     * @param jwriter
     * @param propName
     * @param propValue
     * @throws IOException
     */ 
    protected void writeProperty(JsonWriter jwriter, String propName, String propValue) throws IOException {
        jwriter.startProperty(propName);
        jwriter.outStringLiteral(propValue);
        jwriter.endProperty();
    }
    
    /**
     * Writes a JSON integer property.
     * 
     * @param jwriter
     * @param propName
     * @param propValue
     * @throws IOException
     */ 
    protected void writeProperty(JsonWriter jwriter, String propName, int propValue) throws IOException {
        jwriter.startProperty(propName);
        jwriter.outIntLiteral(propValue);
        jwriter.endProperty();
    }
    
    /**
     * Writes a JSON boolean property.
     * 
     * @param jwriter
     * @param propName
     * @param propValue
     * @throws IOException
     */ 
    protected void writeProperty(JsonWriter jwriter, String propName, boolean propValue) throws IOException {
        jwriter.startProperty(propName);
        jwriter.outBooleanLiteral(propValue);
        jwriter.endProperty();
    }

    /**
     * Writes a JSON property from a Domino object (DateTime, Vector, etc).
     * 
     * @param jwriter
     * @param propName
     * @param propValue
     * @throws IOException
     */
    protected void writeDominoProperty(JsonWriter jwriter, String propName, Object propValue) throws IOException {
        if ( propValue != null ) {
            jwriter.startProperty(propName);
            jwriter.outDominoValue(propValue);
            jwriter.endProperty();
        }
    }
}
