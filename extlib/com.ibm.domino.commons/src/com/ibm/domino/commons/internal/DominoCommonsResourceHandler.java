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





/* Generated by Workplace Designer localization tool */
package com.ibm.domino.commons.internal;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.ibm.domino.commons.RequestContext;

/** 
 * Domino Commons Resource handler class
 * @ibm-not-published 
 */
public class DominoCommonsResourceHandler {

    // logging bundle is ok to save statically, using a static locale.
    private static ResourceBundle _loggingResourceBundle; 
    private static Map<Locale, ResourceBundle> _resourceBundle; 
    private static Map<Locale, ResourceBundle> _specialAudienceBundle;
    
    private static ResourceBundle getResourceBundle(String bundleSuffix, final Locale locale) {
        String clName = DominoCommonsResourceHandler.class.getName();
        final String bundlePackage = clName.substring( 0, clName.lastIndexOf('.') + 1 ) + bundleSuffix; //$NON-NLS-1$
        // ResourceBundle.getBundle is security-sensitive because it uses the ClassLoader
        // so use this plugin's priviledge level, instead of the application code's more restricted level.
        ResourceBundle bundle = AccessController.doPrivileged(new PrivilegedAction<ResourceBundle>() {
            public ResourceBundle run() {
                try {
                    return ResourceBundle.getBundle( bundlePackage, locale);
                }
                catch (MissingResourceException e) {
                    // does nothing - this method will return null and
                    // getString(String) will return the key
                    // it was called with
                }
                return null;
            }
        });
        return bundle;
    }
    public static String getLoggingString(String key) {
        if ( null == _loggingResourceBundle ) {
            Locale localeForLogging = Locale.getDefault(); // server locale, not browser locale
            _loggingResourceBundle = getResourceBundle("logging", localeForLogging); //$NON-NLS-1$
        }
        return getResourceBundleString(_loggingResourceBundle, key);
    }
    public static String getString(String key) {
        Locale locale = getUserLocale();
        ResourceBundle bundle = null == _resourceBundle? null : _resourceBundle.get(locale);
        if( null == bundle ){
            bundle = getResourceBundle("messages", locale); //$NON-NLS-1$
            if( null == _resourceBundle ){
                _resourceBundle = new HashMap<Locale, ResourceBundle>();
            }
            _resourceBundle.put(locale, bundle);
        }
        return getResourceBundleString(bundle, key);
    }
    public static String getSpecialAudienceString(String key) {
        Locale locale = getUserLocale();
        ResourceBundle bundle = null == _specialAudienceBundle? null : _specialAudienceBundle.get(locale);
        if( null == bundle ){
            bundle = getResourceBundle("specialAudience", locale); //$NON-NLS-1$
            if( null == _specialAudienceBundle ){
                _specialAudienceBundle = new HashMap<Locale, ResourceBundle>();
            }
            _specialAudienceBundle.put(locale, bundle);
        }
        return getResourceBundleString(bundle, key);
    }

    private static Locale getUserLocale(){
        // Try getting the locale from the request context (e.g. REST request)
        RequestContext rctx = RequestContext.getCurrentInstance();
        Locale locale = rctx.getUserLocale();
        
        if ( locale == null ) {
            locale = Locale.getDefault();
        }
        
        return locale;
    }
    
    private static String getResourceBundleString(ResourceBundle bundle, String key){
       if (bundle != null) {
            try {
                return bundle.getString(key);
            }
            catch (MissingResourceException e) {
                return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        else {
            return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
}