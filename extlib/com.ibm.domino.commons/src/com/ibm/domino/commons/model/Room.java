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

public class Room {
    
    private String _displayName;
    private String _distinguishedName;
    private String _domain;
    private String _emailAddress;
    private int _capacity;

    public Room(String displayName, String distinguishedName, String domain,
                    String emailAddress, int capacity) {
        _displayName = displayName;
        _distinguishedName = distinguishedName;
        _domain = domain;
        _emailAddress = emailAddress;
        _capacity = capacity;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return _displayName;
    }

    /**
     * @return the distinguishedName
     */
    public String getDistinguishedName() {
        return _distinguishedName;
    }
    
    public String getDomain() {
        return _domain;
    }

    /**
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return _emailAddress;
    }

    /**
     * @return the capacity
     */
    public int getCapacity() {
        return _capacity;
    }

}
