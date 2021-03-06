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

import static com.ibm.domino.services.rest.RestServiceConstants.ATTRIB_ALIGNMENT;
import static com.ibm.domino.services.rest.RestServiceConstants.ATTRIB_CATEGORY;
import static com.ibm.domino.services.rest.RestServiceConstants.ATTRIB_COLUMNNUMBER;
import static com.ibm.domino.services.rest.RestServiceConstants.ATTRIB_FIELD;
import static com.ibm.domino.services.rest.RestServiceConstants.ATTRIB_HIDDEN;
import static com.ibm.domino.services.rest.RestServiceConstants.ATTRIB_NAME;
import static com.ibm.domino.services.rest.RestServiceConstants.ATTRIB_RESPONSE;
import static com.ibm.domino.services.rest.RestServiceConstants.ATTRIB_TITLE;
import static com.ibm.domino.services.rest.RestServiceConstants.ATTRIB_TWISTIE;
import static com.ibm.domino.services.rest.RestServiceConstants.ATTRIB_WIDTH;

import java.io.IOException;
import java.util.Vector;

import lotus.domino.NotesException;
import lotus.domino.View;
import lotus.domino.ViewColumn;

import com.ibm.commons.Platform;
import com.ibm.domino.services.ResponseCode;
import com.ibm.domino.services.ServiceException;
import com.ibm.domino.services.util.JsonWriter;

public class JsonViewDesignContent extends JsonContent {
    
    private View view;
    
    public JsonViewDesignContent(View view) {
        this.view = view;
    }
    
    public void writeViewDesign(JsonWriter jwriter) throws ServiceException {
        
        try {
            jwriter.startArray();
            Vector<?> columns = view.getColumns();
            for (int i = 0; i < columns.size(); i++) 
            {
                jwriter.startArrayItem();
                jwriter.startObject();
                try {
                    ViewColumn column = (ViewColumn) columns.elementAt(i);

                    writeProperty(jwriter, ATTRIB_COLUMNNUMBER, column.getPosition());
                    writeProperty(jwriter, ATTRIB_NAME, column.getItemName());
                    writeProperty(jwriter, ATTRIB_TITLE, column.getTitle());
                    writeProperty(jwriter, ATTRIB_WIDTH, column.getWidth());
                    writeProperty(jwriter, ATTRIB_ALIGNMENT, column.getAlignment());                    
                    writeProperty(jwriter, ATTRIB_HIDDEN, column.isHidden());
                    writeProperty(jwriter, ATTRIB_RESPONSE, column.isResponse());
                    writeProperty(jwriter, ATTRIB_TWISTIE, column.isShowTwistie());
                    writeProperty(jwriter, ATTRIB_FIELD, column.isField());                 
                    writeProperty(jwriter, ATTRIB_CATEGORY, column.isCategory());
                    
                } catch (NotesException e) {
                    Platform.getInstance().log(e);
                } finally {
                    jwriter.endArrayItem();
                    jwriter.endObject();
                }               
            }
        }catch (IOException e) {
            throw new ServiceException(e,"");
        }catch (NotesException e) {
            throw new ServiceException(e, ResponseCode.INTERNAL_ERROR); 
        }

        finally {
            try {
                jwriter.endArray();
                jwriter.flush();
            } catch (IOException e) {
                throw new ServiceException(e,"");
            }
        }
    }

}
