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






package com.ibm.xsp.extlib.util.json;

import java.util.Date;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.xml.util.XMIConverter;

/**
 * JSON Object extension for easier manipulation in Java.
 * 
 * @author priand
 *
 */
public class JObject extends JsonJavaObject {

    private static final long serialVersionUID = 1L;

    public Date getDate(String property) {
        String s = getString(property);
        if (s != null) {
            Date dt = XMIConverter.parseDate(s);
            return dt;
        }
        return null;
    }

    public void putDate(String property, Date value) {
        if (value != null) {
            String dt = XMIConverter.composeDate(value.getTime());
            put(property, dt);
        }
    }
}
