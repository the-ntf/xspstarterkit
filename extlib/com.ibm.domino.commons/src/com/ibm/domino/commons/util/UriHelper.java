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

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Helper methods for working with java.net.URI objects.
 */
public class UriHelper {
    
    /**
     * Creates a new URI from a string.
     * 
     * <p>WARNING: If the input string is not a legal URI, this method
     * will throw an unchecked exception.
     * 
     * @param str
     * @param makeRelative
     * @return
     */
    public static URI create(String str, boolean makeRelative) {
        URI uri = URI.create(str);
        
        if ( uri.isAbsolute() && makeRelative ) {
            uri = copy(uri, true);
        }
        
        return uri;
    }
    
    /**
     * Make a relative copy of a URI.
     * 
     * <p>If makeRelative is false, this may return the original instance.
     * That should be OK because a URI is immutable.
     * 
     * @param original
     * @param makeRelative
     * @return
     */
    public static URI copy(URI original, boolean makeRelative) {
        URI uri = original;
        
        if ( uri.isAbsolute() && makeRelative ) {
            String rel = uri.getRawPath();
            if ( uri.getQuery() != null ) {
                rel += "?" + uri.getRawQuery();
            }
            uri = URI.create(rel);
        }
        
        return uri;
    }

    /**
     * Trims a URI starting at the last occurence of a substring.
     * 
     * @param original
     * @param match
     * @return
     */
    public static URI trimAtLast(URI original, String match) {
        URI uri = null;
        
        String str = original.toString();
        int index = str.lastIndexOf(match);
        if ( index == -1 ) {
            uri = original;
        }
        else {
            uri = URI.create(str.substring(0, index));
        }
        
        return uri;
    }
    
    /**
     * Appends a new path segment to a URI, returning a new instance.
     * 
     * <p>The new path segment is encoded before it is appended.
     * 
     * @param original
     * @param segment
     * @return
     */
    public static URI appendPathSegment(URI original, String segment) {
        URI uri;
        
        if ( original.getQuery() != null ) {
            throw new IllegalArgumentException(com.ibm.domino.commons.internal.DominoCommonsResourceHandler.getSpecialAudienceString("UriHelper.DoesnthandleURLparametersyet")); // $NLX-UriHelper.DoesnthandleURLparametersyet-1$
        }
        
        try {
            String path = original.toString();
            if (!path.endsWith("/"))
                path = path + "/";  
            String encodedSegment = encodePathSegment(segment);
            uri = URI.create(path + encodedSegment);
        }
        finally {
        } 
        
        return uri;
    }
    
    /**
     * Encode special characters in a path segment and replace each slash (/) with %2F.
     * 
     * @param segment
     * @return
     */
    public static String encodePathSegment(String segment) {
        String encoded = null;
        
        try {
            URI uri = new URI(null,null,segment,null);
            // We must encode / in segment by ourselves
            encoded = uri.getRawPath().replace("/", "%2F"); // $NON-NLS-1$
        } 
        catch (URISyntaxException e) {
            throw new IllegalArgumentException(com.ibm.domino.commons.internal.DominoCommonsResourceHandler.getSpecialAudienceString("UriHelper.Badpathsegment"), e); // $NLX-UriHelper.Badpathsegment-1$
        }
        
        return encoded;
    }
}
