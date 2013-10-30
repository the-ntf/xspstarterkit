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

import lotus.domino.NotesException;
import lotus.domino.Session;

/**
 * Factory for Out of Office provider.
 * 
 * <p>In the future, this factory may eventually produce other provider interfaces.
 */
public class ProviderFactory {
    
    private static IOooStatusProvider s_oooProvider = null;
    private static IDelegateProvider s_delegateProvider = null;
    private static IFreeRoomsProvider s_freeRoomsProvider = null;
    private static ISiteProvider s_siteProvider = new SiteProvider();
    private static IImaSettingsProvider s_imaSettingsProvider = null;
    
    public static IOooStatusProvider getOooStatusProvider() {
        if ( s_oooProvider == null ) {
            try {
                // TODO: See if there is a better way to do this.  It would be nice not
                // to hardcode the name of the class to load.  I tried to use a static
                // block inside the fragment, but it never got called.
                
                Class clazz = Class.forName("com.ibm.domino.commons.model.OooStatusProvider"); // $NON-NLS-1$
                s_oooProvider = (IOooStatusProvider)clazz.newInstance();
            } 
            catch (Throwable e) {
                // Do nothing
            }
        }
        return s_oooProvider;
    }
    
    public static IImaSettingsProvider getImaSettingsProvider() {
        if ( s_imaSettingsProvider == null ) {
            try {
                Class clazz = Class.forName("com.ibm.domino.commons.model.ImaSettingsProvider"); // $NON-NLS-1$
                s_imaSettingsProvider = (IImaSettingsProvider)clazz.newInstance();
            } 
            catch (Throwable e) {
                // Do nothing
            }
        }
        return s_imaSettingsProvider;
    }
    
    public static IDelegateProvider getDelegateProvider(Session session) {
        if ( s_delegateProvider == null ) {
            boolean useOldProvider = false;
            
            try {
                String var = session.getEnvironmentString("MailServiceDelegateWithACL", true); // $NON-NLS-1$
                if ( "1".equals(var) ) {
                    useOldProvider = true;
                }
            }
            catch (NotesException e) {
                // Ignore
            }
            
            if ( useOldProvider ) {
                s_delegateProvider = new DelegateProvider();
            }
            else {
                s_delegateProvider = new Delegate901Provider();
            }
        }
        
        return s_delegateProvider;
    }

    public static IFreeRoomsProvider getFreeRoomsProvider(Session session) {
        if ( s_freeRoomsProvider == null ) {
            boolean useOldProvider = false;
            
            try {
                String var = session.getEnvironmentString("FbUseOldRoomsAPI", true); // $NON-NLS-1$
                if ( "1".equals(var) ) {
                    useOldProvider = true;
                }
            }
            catch (NotesException e) {
                // Ignore
            }
            
            if ( useOldProvider ) {
                s_freeRoomsProvider = new FreeRoomsProvider();
            }
            else {
                s_freeRoomsProvider = new FreeRooms901Provider();
            }
        }
        
        return s_freeRoomsProvider;
    }
    
    public static ISiteProvider getSiteProvider() {
        return s_siteProvider;
    }
}
