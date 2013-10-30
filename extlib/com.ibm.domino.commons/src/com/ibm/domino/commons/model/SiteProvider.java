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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewNavigator;

import com.ibm.domino.commons.util.BackendUtil;

public class SiteProvider implements ISiteProvider {

    /* (non-Javadoc)
     * @see com.ibm.domino.commons.model.ISiteProvider#getDirectories(lotus.domino.Session)
     */
    public List<Directory> getDirectories(Session session) throws ModelException {
        
        List<Directory> directories = new ArrayList<Directory>();
        Vector databases = null;
        
        try {
            databases = session.getAddressBooks();
            Iterator iterator = databases.iterator();
            while (iterator.hasNext()) {
                Database database = (Database)iterator.next();
                String server = database.getServer();
                String filename = database.getFilePath();
                
                if ( !database.isOpen() ) {
                    database.open();
                }
                String title = database.getTitle();
                
                Directory directory = new Directory(server, filename, title);
                directories.add(directory);
            }
        } 
        catch (NotesException e) {
            throw new ModelException(com.ibm.domino.commons.internal.DominoCommonsResourceHandler.getSpecialAudienceString("SiteProvider.Errorgettingaddressbooks"), e); // $NLX-SiteProvider.Errorgettingaddressbooks-1$
        }
        finally {
            BackendUtil.safeRecycle(databases);
        }
        
        return directories;
    }

    /* (non-Javadoc)
     * @see com.ibm.domino.commons.model.ISiteProvider#getSites(lotus.domino.Session, java.lang.String)
     */
    public List<String> getSites(Session session, String directory) throws ModelException {
        List<String> sites = new ArrayList<String>();
        Database database = null;
        
        try {
            if ( directory == null ) {
                throw new ModelException(com.ibm.domino.commons.internal.DominoCommonsResourceHandler.getSpecialAudienceString("SiteProvider.Directorynamenotspecified"), ModelException.ERR_INVALID_INPUT); // $NLX-SiteProvider.Directorynamenotspecified-1$
            }
            
            // Parse the server and file name
            
            String server = null;
            String filename = null;
            String tokens[] = directory.split("!!");
            if ( tokens == null || tokens.length < 1 ) {
                throw new ModelException(com.ibm.domino.commons.internal.DominoCommonsResourceHandler.getSpecialAudienceString("SiteProvider.Unexpectedformatfordirectoryident"), ModelException.ERR_NOT_FOUND); // $NLX-SiteProvider.Unexpectedformatfordirectoryident-1$
            }
            else if ( tokens.length > 1 ) {
                server = tokens[0];
                filename = tokens[1];
            }
            else {
                filename = tokens[0];
            }
            
            // Open the directory database
            
            database = session.getDatabase(server, filename, false);
            if ( database == null ) {
                throw new ModelException(MessageFormat.format(com.ibm.domino.commons.internal.DominoCommonsResourceHandler.getSpecialAudienceString("SiteProvider.Cannotopendatabase0on1"), filename, server), ModelException.ERR_NOT_FOUND); // $NLX-SiteProvider.Cannotopendatabase0on1-1$
            }
            
            // Open the rooms view
            
            View view = database.getView("($Rooms)"); // $NON-NLS-1$
            if ( view == null ) {
                throw new ModelException(MessageFormat.format(com.ibm.domino.commons.internal.DominoCommonsResourceHandler.getSpecialAudienceString("SiteProvider.CannotopenRoomsviewin0on1"), filename, server)); // $NLX-SiteProvider.CannotopenRoomsviewin0on1-1$
            }
            
            view.setAutoUpdate(false);
            if ( !view.isCategorized() ) {
                throw new ModelException(com.ibm.domino.commons.internal.DominoCommonsResourceHandler.getSpecialAudienceString("SiteProvider.UnexpectedviewformatRoomsviewisno")); // $NLX-SiteProvider.UnexpectedviewformatRoomsviewisno-1$
            }
            
            // Find all the sites in the ($Rooms) view
            // TODO: Investigate whether this can be optimized (perhaps with 
            // cache controls).
            
            ViewNavigator nav = view.createViewNav();
            nav.setMaxLevel(0);
            ViewEntry next = null;
            ViewEntry entry = nav.getFirst();
            while ( entry != null ) {

                // Extract the site from the first column
                
                Vector values = entry.getColumnValues();
                if ( values != null && values.size() > 0 ) {
                    if ( values.get(0) instanceof String ) {
                        sites.add((String)values.get(0));
                    }
                }
                
                // Get the next entry
                
                next = nav.getNextCategory();
                entry.recycle();
                entry = next;
            }

        } 
        catch (NotesException e) {
            throw new ModelException(com.ibm.domino.commons.internal.DominoCommonsResourceHandler.getSpecialAudienceString("SiteProvider.Erroropeningdirectorydatabase"), e); // $NLX-SiteProvider.Erroropeningdirectorydatabase-1$
        }
        finally {
            BackendUtil.safeRecycle(database);
        }

        return sites;
    }

}
