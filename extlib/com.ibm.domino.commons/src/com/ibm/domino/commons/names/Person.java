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






package com.ibm.domino.commons.names;

/**
 * Person object
 */
public class Person {
    
    private String _displayName;
    private String _distinguishedName;
    private String _emailAddress;
    
    public Person() {
    }
    
    public Person(String displayName, String distinguishedName, String emailAddress) {
        _displayName = displayName;
        _distinguishedName = distinguishedName;
        _emailAddress = emailAddress;
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

    /**
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return _emailAddress;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        _displayName = displayName;
    }

    /**
     * @param distinguishedName the distinguishedName to set
     */
    public void setDistinguishedName(String distinguishedName) {
        _distinguishedName = distinguishedName;
    }

    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        _emailAddress = emailAddress;
    }

}
