/*
 * © Copyright IBM Corp. 2009,2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.dots.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Vector;

import lotus.domino.Base;
import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.DbDirectory;
import lotus.domino.Document;
import lotus.domino.DxlImporter;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.Stream;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;

import com.ibm.dots.Activator;

/**
 * @author dtaieb
 *
 */
public class NotesUtils {

	private static final String TEMP_DB = "~pref.ncf"; // $NON-NLS-1$

	/**
	 * 
	 */
	private NotesUtils() {
	}

	/**
	 * @param base
	 */
	public static void recycle(Base base) {
		if ( base != null ){
			try {
				if ( base instanceof Stream ){
					((Stream)base).close();
				}
				base.recycle();
			} catch (NotesException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param v
	 */
	public static void recycle(Vector<?> v ) {
		if ( v != null ){
			for ( Object o : v ){
				if ( o instanceof Base ){
					recycle( (Base)o );
				}
			}
		}
	}

	/**
	 * @param doc
	 * @return
	 * @throws NotesException
	 */
	public static long getLastModified(Document doc ) throws NotesException {
		DateTime dt = null;
		try{
			dt = doc.getLastModified();
			return dt.toJavaDate().getTime();
		}finally{
			recycle( dt );
		}
	}

	/**
	 * @param is
	 */
	public static void close(InputStream is ) {
		if ( is != null ){
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return
	 * @throws IOException 
	 */
	private static InputStream getDxlInputStream(String pluginId, String dxlPath ) throws IOException {
		URL url = Platform.getBundle( pluginId ).getEntry( dxlPath );
		return url.openStream();
	}

	/**
	 * @param session
	 * @return
	 * @throws NotesException 
	 */
	private static Database getTempDatabase(Session session) throws NotesException {
		IPath path = Activator.getDefault().getStateLocation();
		File f = path.append( TEMP_DB ).toFile();
		Database db = session.getDatabase( "", f.getAbsolutePath(), true );
		if ( db == null || !db.isOpen() ){
			NotesUtils.recycle( db );
			db = null;
			DbDirectory dbDir = session.getDbDirectory( null );
			try{
				db = dbDir.createDatabase( f.getAbsolutePath(), true );
			}finally{
				NotesUtils.recycle( dbDir );
			}
		}
		return db;
	}

	public static interface IDXLProcessor{

		void processImportedDoc(Document fromDoc) throws NotesException;

	}

	/**
	 * @param pluginId
	 * @param dxlPath
	 * @return
	 * @throws NotesException 
	 * @throws IOException 
	 */
	public static void processFromDXL(Session session, IDXLProcessor processor, String pluginId, String dxlPath) throws NotesException, IOException {
		DxlImporter dxlImporter = null;
		Stream stream = null;
		InputStream dxlStream = null;
		Database tmpDb = null;
		Document fromDoc = null; 
		try{
			dxlImporter = session.createDxlImporter();
			dxlImporter.setReplicaRequiredForReplaceOrUpdate( false );
			dxlImporter.setDesignImportOption( DxlImporter.DXLIMPORTOPTION_REPLACE_ELSE_CREATE );
			stream = session.createStream();
			stream.setContents( dxlStream = getDxlInputStream( pluginId, dxlPath ) );
			tmpDb = getTempDatabase( session );
			dxlImporter.importDxl( stream, tmpDb);
			if ( dxlImporter.getImportedNoteCount() == 0 ){
				throw new IllegalStateException(MessageFormat.format( "Unable to import dxl {0}", dxlPath ) ); // $NON-NLS-1$
			}
			String noteId = dxlImporter.getFirstImportedNoteID();
			fromDoc = tmpDb.getDocumentByID( noteId );
			processor.processImportedDoc( fromDoc );
			//Delete the fromDoc from the database
			fromDoc.remove( true );
		}finally{
			NotesUtils.close( dxlStream );
			NotesUtils.recycle( stream );
			NotesUtils.recycle( fromDoc );
			NotesUtils.recycle( tmpDb );
			NotesUtils.recycle( dxlImporter );
		}
	}


}
