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

import java.io.IOException;
import java.io.Writer;

import lotus.domino.Document;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonGenerator.Generator;
import com.ibm.commons.util.io.json.JsonGenerator.StringBuilderGenerator;
import com.ibm.commons.util.io.json.JsonGenerator.WriterGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;

public class JsonMessageGenerator {
    
    private Generator _generator = null;
    
    public JsonMessageGenerator(StringBuilder sb) {
        _generator = new StringBuilderGenerator(JsonJavaFactory.instanceEx, sb, false);
    }

    public JsonMessageGenerator(Writer writer) {
        _generator = new WriterGenerator(JsonJavaFactory.instanceEx, writer, false);
    }
    
    public void toJson(Document document, String url) throws JsonException, IOException {
        JsonMessageAdapter messageAdapter = new JsonMessageAdapter(document, url);
        _generator.toJson(messageAdapter);
    }
}
