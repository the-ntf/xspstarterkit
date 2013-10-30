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






package com.ibm.domino.services.rest.das.document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.domino.services.ServiceException;
import com.ibm.domino.services.rest.das.RestDominoService;


/**
 * Domino Document Service.
 */
public abstract class RestDocumentService extends RestDominoService {
    
    private DocumentParameters parameters;

    // Work members 
    protected Document defaultDocument;
    protected boolean shouldRecycleDocument;
    protected Document document;
        
    protected RestDocumentService(HttpServletRequest httpRequest, HttpServletResponse httpResponse, DocumentParameters parameters) {
        super(httpRequest, httpResponse);
        this.parameters = wrapDocumentParameters(parameters);
    }

    protected DocumentParameters wrapDocumentParameters(DocumentParameters parameters) {
        return parameters;
    }
    
    @Override
    public DocumentParameters getParameters() {
        return parameters;
    }
    
    @Override
    public void recycle() {
        if(document!=null && shouldRecycleDocument) {
            try {
                document.recycle();
                document = null;
            } catch(NotesException ex) {
                Platform.getInstance().log(ex);
            }
        }
        super.recycle();
    }
    
    @Override
    public abstract void renderService() throws ServiceException;

    // Access to the backend classes
    public Document getDocument() throws NotesException {
        if(document==null) {
            loadDocument();
        }
        return document;
    }
    
    public void setDefaultDocument(Document defaultDocument) {
        this.defaultDocument = defaultDocument;     
    }
    
    protected String getDocumentUnid() throws NotesException {
        DocumentParameters parameters = getParameters();        
        String unid = parameters.getDocumentUnid(); 
        if(StringUtil.isEmpty(unid)) {      
            String pathInfo = getHttpRequest().getPathInfo();
            if(StringUtil.isNotEmpty(pathInfo)) {
                unid = pathInfo.substring(pathInfo.lastIndexOf("/")+1); // $NON-NLS-1$
            }       
        }       
        if(StringUtil.isEmpty(unid)) {
            if(defaultDocument==null) {
                throw new IllegalStateException(com.ibm.domino.services.ResourceHandler.getSpecialAudienceString("RestDocumentService.Nodefaultdocumentassignedtotheser")); // $NLX-RestDocumentService.Nodefaultdocumentassignedtotheser-1$
            }
            unid = defaultDocument.getUniversalID();            
        }       
        if(StringUtil.isEmpty(unid)) {
            throw new IllegalStateException(com.ibm.domino.services.ResourceHandler.getSpecialAudienceString("RestDocumentService.NodocumentUNIDassignedtotheservic")); // $NLX-RestDocumentService.NodocumentUNIDassignedtotheservic-1$
        }
        return unid;
    }
    
    protected void loadDocument() throws NotesException {
        DocumentParameters parameters = getParameters();
        Database db = getDatabase(parameters);
        String unid = parameters.getDocumentUnid(); 
        if(StringUtil.isEmpty(unid)) {      
            String pathInfo = getHttpRequest().getPathInfo();
            if(StringUtil.isNotEmpty(pathInfo)) {
                pathInfo = pathInfo.startsWith("/")?pathInfo.substring(1):pathInfo; // $NON-NLS-1$
                if (pathInfo.indexOf("/") > 0) { // $NON-NLS-1$
                    unid = pathInfo.substring(pathInfo.lastIndexOf("/")+1); // $NON-NLS-1$
                }
            }       
        }       
        if(StringUtil.isEmpty(unid)) {
            // Verfiy we want get all document feature in XPages then remove this.  
//          if(defaultDocument==null) {
//              throw new IllegalStateException("No default document assigned to the service");
//          }
            this.document = defaultDocument;            
            this.shouldRecycleDocument = false;
            return;
        }
        this.document = db.getDocumentByUNID(unid);
        if(document==null) {
            throw new NotesException(0,StringUtil.format(com.ibm.domino.services.ResourceHandler.getSpecialAudienceString("RestDocumentService.Unknowndocument0indatabase1"), unid, db.getFileName())); // $NLX-RestDocumentService.Unknowndocument0indatabase1-1$
        }
        this.shouldRecycleDocument = true;
    }
    
    /**
     * Called when a new document is being created in the database.
     * 
     * @return true to continue
     */
    public boolean queryNewDocument() {
        return true;
    }
    
    /**
     * Called when a document is being opened for update purposes.
     * 
     * @return true to continue
     */
    public boolean queryOpenDocument(String id) {
        return true;
    }

    /**
     * Called right before the document is being saved (after computeWithForm).
     * 
     * @return true to continue
     */
    public boolean querySaveDocument(Document doc) {
        return true;
    }
    
    /**
     * Called right before a document is about to be deleted.
     * 
     * @return true to continue
     */
    public boolean queryDeleteDocument(String id) {
        return true;
    }

    /**
     * Called after a new document is created in memory.
     */
    public void postNewDocument(Document doc) {
        
    }

    /**
     * Called after a document is opened in memory.
     */
    public void postOpenDocument(Document doc)  {
        
    }
    
    /**
     * Called after a document had been saved.
     */
    public void postSaveDocument(Document doc)  {
        
    }   
    
    /**
     * Called after a document had been deleted.
     */
    public void postDeleteDocument(String id) {
        
    }
    
    
}
