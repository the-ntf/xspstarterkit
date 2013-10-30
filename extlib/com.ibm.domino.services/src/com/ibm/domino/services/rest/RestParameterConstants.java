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






package com.ibm.domino.services.rest;

public interface RestParameterConstants {

    // Global Parameters Constants  
    public static final String PARAM_UNID = "unid"; //$NON-NLS-1$
    public static final String PARAM_DATA = "data"; //$NON-NLS-1$
    public static final String PARAM_COLLECTIONS = "collections";   //$NON-NLS-1$
    public static final String PARAM_DOCUMENTS = "documents";   //$NON-NLS-1$
    public static final String PARAM_NAME = "name"; //$NON-NLS-1$
    public static final String PARAM_COMPUTEWITHFORM = "computewithform";   //$NON-NLS-1$
    public static final String PARAM_FORM = "form"; //$NON-NLS-1$
    public static final String PARAM_COMPACT = "compact";   //$NON-NLS-1$
    public static final String PARAM_DESIGN = "design"; //$NON-NLS-1$
    public static final String PARAM_STREAMING = "streaming";   //$NON-NLS-1$
    public static final String PARAM_SEPERATOR = "/";   //$NON-NLS-1$
    public static final String UNID_RESOURCE_PATH = "{" + PARAM_UNID + "}";
    
    // Default Constants    
    public static final int DEFAULT_VIEW_COUNT = 10;
    
    // View Parameters Constants    
    public static final String PARAM_VIEW_START = "start";  //$NON-NLS-1$
    public static final String PARAM_VIEW_COUNT = "count";  //$NON-NLS-1$
    public static final String PARAM_VIEW_STARTINDEX = "si";    //$NON-NLS-1$
    public static final String PARAM_VIEW_PAGESIZE = "ps";  //$NON-NLS-1$
    public static final String PARAM_VIEW_PAGEINDEX = "page";   //$NON-NLS-1$
    public static final String PARAM_VIEW_SEARCH = "search";    //$NON-NLS-1$
    public static final String PARAM_VIEW_SEARCHMAXDOCS = "searchmaxdocs";  //$NON-NLS-1$
    public static final String PARAM_VIEW_SORTCOLUMN = "sortcolumn";    //$NON-NLS-1$
    public static final String PARAM_VIEW_SORTORDER = "sortorder";  //$NON-NLS-1$
    public static final String PARAM_VIEW_STARTKEYS = "startkeys"; //$NON-NLS-1$
    public static final String PARAM_VIEW_SYSTEMCOLUMNS = "systemcolumns"; //$NON-NLS-1$
    public static final String PARAM_VIEW_KEYS = "keys";    //$NON-NLS-1$
    public static final String PARAM_VIEW_KEYSEXACTMATCH = "keysexactmatch";    //$NON-NLS-1$
    public static final String PARAM_VIEW_EXPANDLEVEL = "expandlevel";  //$NON-NLS-1$
    public static final String PARAM_VIEW_CATEGORY = "category";    //$NON-NLS-1$
    public static final String PARAM_VIEW_PARENTID = "parentid";    //$NON-NLS-1$
    public static final String PARAM_VIEW_COMPUTEWITHFORM = PARAM_COMPUTEWITHFORM;
    public static final String PARAM_VIEW_FORM = PARAM_FORM;
    public static final String PARAM_VIEW_ENTRYCOUNT = "entrycount";    //$NON-NLS-1$
    public static final String PARAM_VIEW_NAME = "viewname";    //$NON-NLS-1$

    // Document Parameters Constants
    public static final String PARAM_DOC_ATTACHFORM = "attachform"; //$NON-NLS-1$
    public static final String PARAM_DOC_COMPUTEWITHFORM = PARAM_COMPUTEWITHFORM;
    public static final String PARAM_DOC_FORM = PARAM_FORM;
    public static final String PARAM_DOC_HIDDEN = "hidden"; //$NON-NLS-1$
    public static final String PARAM_DOC_MARKREAD = "markread"; //$NON-NLS-1$
    public static final String PARAM_DOC_RICHTEXT = "richtext"; //$NON-NLS-1$   
    public static final String PARAM_DOC_PARENTID = PARAM_VIEW_PARENTID;
    public static final String PARAM_DOC_DOCUMENTID = "documentid"; //$NON-NLS-1$
    public static final String PARAM_DOC_SEARCH = "search"; //$NON-NLS-1$
    public static final String PARAM_DOC_SEARCHMAXDOCS = PARAM_VIEW_SEARCHMAXDOCS;
    public static final String PARAM_DOC_SEND = "send"; //$NON-NLS-1$
    public static final String PARAM_DOC_SINCE = "since";   //$NON-NLS-1$
    public static final String PARAM_DOC_STRONGTYPE = "strongtype"; //$NON-NLS-1$   
    public static final String PARAM_DOC_MULTIPART = "multipart";   //$NON-NLS-1$   
    
    // View Parameters Value
    public static final String PARAM_VALUE_TRUE = "true";   //$NON-NLS-1$
    public static final String PARAM_VALUE_FALSE = "false"; //$NON-NLS-1$
}
