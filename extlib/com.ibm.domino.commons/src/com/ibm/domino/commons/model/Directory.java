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






package com.ibm.domino.commons.model;

/**
 * Immutable class representing a directory on a server.
 */
public class Directory {
    
    private String _serverName;
    private String _filePath;
    private String _displayName;
    
    /**
     * Constructs a directory object.
     * 
     * @param serverName The canonical server name.
     * @param filePath The file path including (the file name).
     * @param displayName The display name or title.
     */
    public Directory(String serverName, String filePath, String displayName) {
        _serverName = serverName;
        _filePath = filePath;
        _displayName = displayName;
    }

    /**
     * @return the serverName
     */
    public String getServerName() {
        return _serverName;
    }

    /**
     * @return the filePath
     */
    public String getFilePath() {
        return _filePath;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return _displayName;
    }

}
