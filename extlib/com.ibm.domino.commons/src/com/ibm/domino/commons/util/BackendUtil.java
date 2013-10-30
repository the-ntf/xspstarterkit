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






package com.ibm.domino.commons.util;

import java.util.Iterator;
import java.util.Vector;

import lotus.domino.Base;
import lotus.domino.NotesException;

public class BackendUtil {

    /**
     * Safely recycle a backend object.
     * 
     * @param obj
     */
    public static void safeRecycle(Base obj) {
        
        if ( obj != null ) {
            try {
                obj.recycle();
            }
            catch (NotesException e) {
                // Ignore exceptions inside recycle
            }
        }
    }
    
    /**
     * Safely recycle a vector of backend objects.
     * 
     * @param list
     */
    public static void safeRecycle(Vector list) {
        if ( list != null ) {
            Iterator iterator = list.iterator();
            while ( iterator.hasNext() ) {
                Object obj = iterator.next();
                if ( obj instanceof Base ) {
                    safeRecycle((Base)obj);
                }
            }
        }
    }
}
