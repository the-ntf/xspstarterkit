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

import static com.ibm.domino.commons.json.JsonConstants.ISO8601_UTC;
import static com.ibm.domino.commons.json.JsonConstants.JSON_OOO_ENABLED;
import static com.ibm.domino.commons.json.JsonConstants.JSON_OOO_END;
import static com.ibm.domino.commons.json.JsonConstants.JSON_OOO_MESSAGE;
import static com.ibm.domino.commons.json.JsonConstants.JSON_OOO_SEND_EXTERNAL;
import static com.ibm.domino.commons.json.JsonConstants.JSON_OOO_START;
import static com.ibm.domino.commons.json.JsonConstants.JSON_OOO_SUBJECT;

import java.io.IOException;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonGenerator.Generator;
import com.ibm.commons.util.io.json.JsonGenerator.StringBuilderGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.domino.commons.model.OooStatus;

/**
 * Generates Out of Office status in JSON format.
 */
public class JsonOooStatusGenerator {

    private Generator _generator = null;
    
    public JsonOooStatusGenerator(StringBuilder sb) {
        _generator = new StringBuilderGenerator(JsonJavaFactory.instanceEx, sb, false);
    }
    
    public void toJson(OooStatus oooStatus) throws JsonException, IOException {
        
        JsonJavaObject obj = new JsonJavaObject();
        if ( oooStatus.isEnabled() ) {
            obj.putJsonProperty(JSON_OOO_ENABLED, true);
            
            if ( !StringUtil.isEmpty(oooStatus.getSubject()) ) {
                obj.putJsonProperty(JSON_OOO_SUBJECT, oooStatus.getSubject());
            }

            if ( !StringUtil.isEmpty(oooStatus.getMessage()) ) {
                obj.putJsonProperty(JSON_OOO_MESSAGE, oooStatus.getMessage());
            }
            
            String start = ISO8601_UTC.format(oooStatus.getStart());
            obj.putJsonProperty(JSON_OOO_START, start);
            
            String end = ISO8601_UTC.format(oooStatus.getEnd());
            obj.putJsonProperty(JSON_OOO_END, end);
            
            obj.putJsonProperty(JSON_OOO_SEND_EXTERNAL, oooStatus.isSendExternal());
            
            // SPR# XZHU98BBZM: Don't write the appendReturnToSubject property
            // because the OooStatusProvider doesn't read the correct value yet. 
            
            //obj.putJsonProperty(JSON_OOO_APPEND_RETURN, oooStatus.isAppendReturnToSubject());
        }
        else {
            obj.putJsonProperty(JSON_OOO_ENABLED, false);
        }
        
        _generator.toJson(obj);
    }

}
