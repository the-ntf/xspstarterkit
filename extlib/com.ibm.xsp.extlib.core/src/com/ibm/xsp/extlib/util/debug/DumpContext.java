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






package com.ibm.xsp.extlib.util.debug;

import java.util.HashMap;
import java.util.Map;


/**
 * Context being used when dumping properties.
 */
public class DumpContext {

    private Map<String,Object> properties = new HashMap<String, Object>();
    
    public DumpContext() {
    }
    
    public Object getProperty(String name) {
        return properties.get(name);
    }

    public void putProperty(String name, Object value) {
        properties.put(name,value);
    }

    public Map<String,Object> getProperties() {
        return properties;
    }
    
    public boolean shouldUseBeanProperties(Object o) {
        return false;
    }
}
