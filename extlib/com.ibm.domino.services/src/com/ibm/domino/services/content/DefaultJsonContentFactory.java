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

public class DefaultJsonContentFactory extends JsonContentFactory {
    
    private static JsonContentFactory s_factory = new DefaultJsonContentFactory();
    
    /**
     * Private constructor because this is a singleton.
     */
    private DefaultJsonContentFactory() {
    }

    public static JsonContentFactory get() {
        return s_factory;
    }
}
