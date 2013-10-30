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

import com.ibm.commons.util.io.json.JsonJavaFactory;



/**
 * This JSON factory is used manipulate Java object with the extlib extensions.
 * <p>
 * The values are mapped by Java objects (String, Number, List<?>, Map<?,?>...). Note that
 * the collections can either be Map or JsonJavaObject.
 * </p>
 * 
 * @author priand
 */
public class JFactory extends JsonJavaFactory {

    /**
     * Singleton instance that uses JObject for collection.
     */
    public static final JFactory instance = new JFactory() {
        @Override
        public Object createObject(Object parent, String propertyName) {
            return new JObject();
        }
    };
}
