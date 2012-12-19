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
package com.ibm.dots.internal.preferences;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import lotus.domino.Base;
import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.NotesThread;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewNavigator;

import org.eclipse.core.internal.preferences.EclipsePreferences;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.osgi.service.prefs.BackingStoreException;

import com.ibm.dots.Activator;
import com.ibm.dots.internal.OSGiProfileProxy;
import com.ibm.dots.preferences.AbstractConfigurationInitializer;
import com.ibm.dots.preferences.IDominoPreferences;
import com.ibm.dots.utils.NotesUtils;
import com.ibm.dots.utils.Platform;


/**
 * @author dtaieb
 *
 */
@SuppressWarnings("restriction") // $NON-NLS-1$
public class DominoOSGiPreferences extends EclipsePreferences implements IDominoPreferences, IPrefConstants  {

	private static class Backend{

		DominoOSGiPreferences owner;
		Session session;
		Database store;
		View view;

		/**
		 * New connection to the backend store
		 * @throws NotesException 
		 */
		public Backend( DominoOSGiPreferences owner) throws BackingStoreException {
			try{
				NotesThread.sinitThread();
				session = NotesFactory.createSession();
				String database = OSGiProfileProxy.getOSGiConfDb();
				store = session.getDatabase( "", database );
				view = store.getView( CONFIGURATION_VIEW_NAME );
				if ( view == null && owner.bCreateView ){
					if ( owner.ci != null ){
						owner.ci.println( MessageFormat.format( "Database is not initiatlized. Creating new view {0}", CONFIGURATION_VIEW_NAME ) ); // $NON-NLS-1$
					}
					//Create the view and try again
					NotesUtils.processFromDXL(session, 
							new NotesUtils.IDXLProcessor() {                                
						public void processImportedDoc(Document fromDoc) throws NotesException {
							Document viewDoc = null;
							try{
								viewDoc = fromDoc.copyToDatabase( store );
							}finally{
								NotesUtils.recycle( viewDoc );
							}
						}
					}, 
					Activator.PLUGIN_ID, CONFIGURATION_VIEW_DXL 
					);
					//Try again
					view = store.getView( CONFIGURATION_VIEW_NAME );
				}
				if ( view == null ){
					throw new IllegalStateException( MessageFormat.format( "Unable to find view {0}", CONFIGURATION_VIEW_NAME ) ); // $NON-NLS-1$
				}
				this.owner = owner;
			}catch( Throwable t){
				release();
				if ( owner.ci != null ){
					owner.ci.printStackTrace( t );
				}
				throw new BackingStoreException( "Error connecting to the NSF backend store", t ); // $NON-NLS-1$
			}
		}

		/**
		 * 
		 */
		public void release() {
			NotesUtils.recycle( store );
			NotesUtils.recycle( view );
			NotesThread.stermThread();
			owner = null;
		}

		/**
		 * @return
		 * @throws NotesException
		 */
		public String getServerName() throws NotesException {
			if ( session == null ){
				throw new IllegalStateException();
			}
			return session.getEffectiveUserName();
		}
	}

	protected static final ThreadLocal<Backend> tlsBackend = new ThreadLocal<Backend>();
	private boolean bIsLoaded;
	private String documentId;  //Backend store Document id for this node
	private long lastModified;  //Backend store document last modified date

	private HashMap<String, Object> objectsProperties = new HashMap<String, Object>();
	private HashMap<String, String> modifiedProperties = new HashMap<String, String>();

	private AbstractConfigurationInitializer configInitializer;

	private CommandInterpreter ci;
	private boolean bCreateView;

	/**
	 * @param nodeParent
	 * @param nodeName
	 */
	private DominoOSGiPreferences(EclipsePreferences nodeParent, String nodeName) {
		super( nodeParent, nodeName );
	}

	/**
	 * 
	 */
	public DominoOSGiPreferences() {
	}

	@Override
	protected EclipsePreferences internalCreate(EclipsePreferences nodeParent, String nodeName, Object context) {
		DominoOSGiPreferences newPref = new DominoOSGiPreferences( nodeParent, nodeName );
		if ( context instanceof AbstractConfigurationInitializer ){
			newPref.setConfigurationInitializer( (AbstractConfigurationInitializer) context );
		}
		return newPref;
	}

	/**
	 * @param configInitializer
	 */
	private void setConfigurationInitializer( AbstractConfigurationInitializer configInitializer ) {
		this.configInitializer = configInitializer;
	}

	@Override
	public void flush() throws BackingStoreException {
		super.flush();
	}

	@Override
	protected IEclipsePreferences getLoadLevel() {
		return this;
	}

	@Override
	protected boolean isAlreadyLoaded(IEclipsePreferences node) {
		if ( node instanceof DominoOSGiPreferences ){
			return ((DominoOSGiPreferences)node).bIsLoaded;
		}
		return false;
	}

	@Override
	protected void loaded() {
		bIsLoaded = true;
	}

	@Override
	protected void load() throws BackingStoreException {
		try{
			Document store = getDocument( null );
			if ( store == null ){
				if ( isRoot() ){
					throw new BackingStoreException( "Unable to locate root preference document for this profile: " + Platform.getProfileName() ); // $NON-NLS-1$
				}
				return;
			}
			Vector<?> items = store.getItems();
			try{
				for ( Object o : items ){
					Item item = (Item)o;
					boolean bIsTextValue = true;
					String key = fromStoreKey( item.getName(), FIELD_PREF_PREFIX );
					if ( key == null ){
						key = fromStoreKey( item.getName(), FIELD_PREF_PREFIX_OBJECT );
						bIsTextValue = false;
					}
					if ( key != null ){
						if ( bIsTextValue ){
							internalPut( key, item.getText() );
						}else{
							Vector<?> values = item.getValues();
							try{
								if ( values.size() > 0 ){
									if ( values.size() == 1 ){
										internalPutObject( key, values.get( 0 ) );
									}else {
										internalPutObject( key, getSafeList( values ) );
									}
								}
							}finally{
								NotesUtils.recycle( values );
							}
						}
					}
				}
			}finally{
				NotesUtils.recycle( items );
			}
		}catch( NotesException ex ){
			throw new BackingStoreException("Error while loading from the Notes Database", ex ); // $NON-NLS-1$
		}finally{
			releaseTLS();
		}
	}

	/**
	 * @param values
	 * @return
	 * @throws NotesException 
	 */
	private List<Object> getSafeList(Vector<?> values) throws NotesException {
		ArrayList< Object > retList = new ArrayList<Object>();
		for ( Object value : values ){
			if ( value instanceof Base ){
				if ( value instanceof DateTime ){
					retList.add( ((DateTime)value).toJavaDate() );
				}else{
					retList.add( value.toString() );
				}
			}else{
				retList.add( value );
			}
		}
		return retList;
	}

	@Override
	protected String internalPut(String key, String newValue) {
		//Remove it from list of Object properties before adding it
		objectsProperties.remove( key );

		//Put this key in the list of modified properties
		modifiedProperties.put( key, newValue );
		return super.internalPut(key, newValue);
	}

	/**
	 * @return
	 */
	private boolean isRoot() {
		return !(parent() instanceof DominoOSGiPreferences);
	}

	@Override
	protected void save() throws BackingStoreException {
		try{
			Document store = getDocument( null );
			if ( store == null ){
				throw new BackingStoreException( 
						MessageFormat.format( "Unable to open backend document for configuration {0}", absolutePath() ) // $NON-NLS-1$
				);
			}
			if ( lastModified != 0 && lastModified < NotesUtils.getLastModified( store ) ){
				//Verify that there is no conflict
				if ( hasConflicts( store ) ){
					throw new BackingStoreException( 
							MessageFormat.format( "The document for preference {0} has been modified resulting in conflict", toString() )  // $NON-NLS-1$
					);
				}
				lastModified = NotesUtils.getLastModified( store );
			}
			String[] keys = keys();
			for ( String key : keys ){
				String storeKey = toStoreKey( key );
				appendItemValue( store, storeKey, properties.get( key ) );
			}

			for ( Entry<String, Object> entry : objectsProperties.entrySet() ){
				String storeKey = toStoreKey( entry.getKey() );
				if ( store.hasItem( storeKey )){
					store.removeItem( storeKey );
				}
				Object value = entry.getValue();
				appendItemValue( store, storeKey, value );
			}

			// recursively save the children
			for ( IEclipsePreferences childNode : getChildren(true) ){
				childNode.flush();
			}
			modifiedProperties.clear();

			//Remove the DisplayPreferences if it exist so that it can be recalculated from the form
			if ( store.hasItem( FIELD_DISPLAY_PREFERENCES )){
				store.removeItem( FIELD_DISPLAY_PREFERENCES );
			}
			store.sign();
			store.save( true );
		}catch( NotesException ex ){
			throw new BackingStoreException("Error while saving the Notes Database", ex ); // $NON-NLS-1$
		}finally{
			releaseTLS();
		}
	}

	/**
	 * @param store
	 * @return
	 * @throws NotesException 
	 */
	private boolean hasConflicts(Document store) throws NotesException {
		Vector<?> items = store.getItems();
		try{
			for ( Object o : items ){
				Item item = (Item)o;
				String key = fromStoreKey( item.getName(), FIELD_PREF_PREFIX );
				if ( key != null && modifiedProperties.containsKey( key ) ){
					if ( !item.getText().equals( modifiedProperties.get( key ) ) ){
						return false;
					}
				}
			}
		}finally{
			NotesUtils.recycle( items );
		}
		return true;
	}

	/**
	 * @param name
	 * @param prefix
	 * @return
	 */
	private String fromStoreKey(String name, String prefix) {
		if ( name == null ){
			return null;
		}
		if ( name.startsWith( prefix ) ){
			return name.substring( prefix.length() );
		}
		return null;
	}

	/**
	 * @param key
	 * @return
	 */
	private String toStoreKey(String key) {
		return FIELD_PREF_PREFIX + key;
	}

	/**
	 * 
	 */
	private void releaseTLS() {
		Backend be = tlsBackend.get();
		if ( be != null && be.owner == this ){
			be.release();
			tlsBackend.remove();            
		}       
	}

	/**
	 * @return
	 */
	private Backend getBackend(){
		Backend be = tlsBackend.get();
		if ( be == null ){
			throw new IllegalStateException();
		}
		return be;
	}

	/**
	 * @param profileName
	 * @return
	 * @throws BackingStoreException
	 * @throws NotesException
	 */
	private Document getDocument(String profileName) throws BackingStoreException, NotesException {
		return getDocument( profileName, false );
	}

	/**
	 * @param profileName
	 * @return
	 * @throws BackingStoreException
	 * @throws NotesException
	 */
	private Document getDocument(String profileName, boolean bIsNewProfile) throws BackingStoreException, NotesException {
		Backend be = tlsBackend.get();
		if ( be == null ){
			tlsBackend.set( be = new Backend( this ) );
		}

		if ( bIsNewProfile || documentId == null ){
			//To find the document id for this node, we must walk the segments starting from the root
			String path = absolutePath();
			String[] segments = path.split("/");
			ViewNavigator vn = null;
			ViewEntry ve = null;
			Document createdDoc = null; //Used only when bCreate is set to true
			try{
				vn = be.view.createViewNav();

				for ( String segment : segments ){
					if ( segment.length() > 0 ){
						if ( createdDoc != null ){
							Document tmpDoc = createNewPrefDoc( createdDoc, segment, profileName );
							NotesUtils.recycle( createdDoc );
							createdDoc = tmpDoc;
						}else{
							ViewEntry tmpve = navigate( profileName, segment, vn, ve );
							if ( tmpve == null ){
								Document tmpDoc = (ve == null ? null : ve.getDocument());
								try{
									if ( tmpDoc == null  && !isRoot() ){
										return null;
									}
									createdDoc = createNewPrefDoc( tmpDoc, segment, profileName );
								}finally{
									NotesUtils.recycle( tmpDoc );
								}
							}

							NotesUtils.recycle( ve );
							ve = tmpve;
						}
					}
				}
				if ( ve != null ){
					Document retDocument = ve.getDocument();
					if ( !bIsNewProfile ){
						documentId = retDocument.getUniversalID();
						lastModified = NotesUtils.getLastModified( retDocument );
					}
					if ( doInitializePrefDoc( retDocument ) ){
						retDocument.save();
					}
					return retDocument;
				}else if ( createdDoc != null ){
					if ( !bIsNewProfile ){
						documentId = createdDoc.getUniversalID();
						lastModified = NotesUtils.getLastModified( createdDoc );
					}
					return createdDoc;
				}
				return null;
			}finally{
				NotesUtils.recycle( ve );
				NotesUtils.recycle( vn );
			}
		}else{
			//We have a document id
			return be.store.getDocumentByUNID( documentId );
		}
	}

	/**
	 * @param parentDoc
	 * @param segment
	 * @param profileName
	 * @return
	 * @throws NotesException
	 */
	private Document createNewPrefDoc(Document parentDoc, String segment, String profileName) throws NotesException {
		if ( parentDoc == null ){
			if ( !isRoot() ){
				throw new IllegalArgumentException();
			}
		}

		Document retDoc = getBackend().store.createDocument();

		if ( configInitializer == null ){
			//Try to find a configInitializer
			try {
				configInitializer = RunInitConfigurationsJob.findInitializer( segment, isRoot() );
				if ( configInitializer == null ){
					configInitializer = Platform.getDefaultConfigurationInitializer();
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		if ( configInitializer == null ){
			//Use default form
			appendItemValue( retDoc, FORM_FIELD, PLUGIN_PREF_FORM_NAME );
		}else{
			doInitializePrefDoc( retDoc );
		}

		appendItemValue( retDoc, SERVER_FIELD_NAME, getBackend().getServerName() );
		if ( profileName == null ){
			profileName = Platform.getProfileName();
		}
		appendItemValue( retDoc, OSGI_INSTANCE_FIELD_NAME, profileName.toUpperCase() );
		if ( parentDoc != null ){
			appendItemValue( retDoc, PREF_NODE_FIELD_NAME , segment );
			retDoc.makeResponse( parentDoc );
		}
		retDoc.computeWithForm( false, true );
		retDoc.sign();
		retDoc.save();
		return retDoc;
	}

	/**
	 * @param doc
	 * @param fieldName
	 * @param value
	 * @throws NotesException
	 */
	private void appendItemValue(Document doc, String fieldName, Object value ) throws NotesException {
		if ( doc.hasItem( fieldName )){
			doc.removeItem( fieldName );
		}
		if ( !isEmpty( value ) ){
			doc.appendItemValue( fieldName, value);
		}
	}

	/**
	 * @param value
	 * @return
	 */
	private boolean isEmpty(Object value) {
		if ( value == null ){
			return true;
		}
		if ( value instanceof String ){
			return ((String)value).trim().length() == 0;
		}
		return false;
	}

	/**
	 * @param doc
	 * @return
	 * @throws NotesException
	 */
	private boolean doInitializePrefDoc(Document doc ) throws NotesException {
		if ( configInitializer == null && isRoot() ){
			configInitializer = Platform.getRootConfigurationInitializer();
		}
		if ( configInitializer != null ){
			configInitializer.doInitialize( doc );
			configInitializer = null;
			return true;
		}
		return false;
	}

	/**
	 * @param profileName
	 * @param segment
	 * @param vn
	 * @param ve
	 * @return
	 * @throws NotesException
	 */
	private ViewEntry navigate(String profileName, String segment, ViewNavigator vn, ViewEntry ve) throws NotesException {
		boolean bFirstCall = ( ve == null );
		if ( bFirstCall ){
			ve = vn.getFirst();

			//First level is the server
			//Replace the segment with the segment name
			segment = getBackend().getServerName();
		}else{
			ve = vn.getChild();
		}

		//Look for an entry
		while ( ve != null ){
			Vector<?> colValues = ve.getColumnValues();
			try{
				String firstValue = getColumnValue( colValues, bFirstCall ? 0 : 1 );
				if ( segment.equalsIgnoreCase( firstValue ) ){
					break;
				}
				ViewEntry tmpve = vn.getNextSibling();
				NotesUtils.recycle( ve );
				ve = tmpve;
			}finally{
				NotesUtils.recycle( colValues );
			}
		}

		if ( ve != null && bFirstCall ){
			//Navigate to the right osgi profile
			if ( profileName == null ){
				profileName = Platform.getProfileName();
			}
			ve = navigate( profileName, profileName, vn, ve );

		}
		return ve;
	}

	/**
	 * @param columnValues
	 * @param iPos
	 * @return
	 */
	private String getColumnValue(Vector<?> columnValues, int iPos ) {
		if ( columnValues == null ){
			return null;
		}
		for ( int i = iPos; i < columnValues.size(); i++ ){
			Object o = columnValues.get( i );
			if ( o instanceof String && ((String)o).length() > 0 ){
				return (String)o;
			}
		}
		return null;
	}

	/**
	 * @param pluginId
	 * @param dxlPath
	 * @param initializer
	 */
	public void node(String pluginId, String dxlPath, AbstractConfigurationInitializer initializer) {
		//Load the node with context
		try{
			initializer.setDxlPath( pluginId, dxlPath );
			internalNode( pluginId, true, initializer );
		}finally{
			initializer.setDxlPath( null, null );
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.preferences.IDominoPreferences#getDate(java.lang.String, java.util.Date)
	 */
	public Date getDate(String key, Date defValue) {
		Object value = internalGetObject( key );
		if ( value == null ){
			value = defValue;
		}
		if ( value instanceof Date ){
			return (Date)value;
		}
		return null;
	}

	/**
	 * @param key
	 * @return
	 */
	private Object internalGetObject(String key) {
		if (key == null){
			throw new NullPointerException();
		}
		checkRemoved();
		return objectsProperties.get(key);
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.preferences.IDominoPreferences#putDate(java.lang.String, java.util.Date)
	 */
	public void putDate(String key, Date date) {
		if (date == null){
			throw new NullPointerException();
		}
		Object oldDate = internalPutObject(key, date);
		if (!date.equals(oldDate)) {
			makeDirty();
			firePreferenceEvent(key, oldDate, date);
		}
	}

	/**
	 * @param key
	 * @param newValue
	 * @return
	 */
	private Object internalPutObject(String key, Object newValue) {
		checkRemoved();
		//Remove from the properties
		if ( properties.get( key ) != null ){
			properties = properties.removeKey( key );
		}
		Object oldValue = objectsProperties.get(key);
		if (oldValue != null && oldValue.equals(newValue)){
			return oldValue;
		}

		objectsProperties.put(key, newValue);
		return oldValue;
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.preferences.IDominoPreferences#getList(java.lang.String, java.util.List)
	 */
	public List<Object> getList(String key, List<Object> defValue) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.preferences.IDominoPreferences#putList(java.lang.String, java.util.List)
	 */
	public void putList(String key, List<Object> list) {
	}

	/**
	 * @param ci
	 * @param profileName
	 * @throws NotesException
	 * @throws BackingStoreException
	 */
	public static void createProfile(CommandInterpreter ci, String profileName) throws NotesException, BackingStoreException {
		DominoOSGiPreferences pref = DominoOSGiScope.getInstance().getDominoRootPreference();
		Document doc = null;
		try{
			pref.bCreateView = true;
			pref.ci = ci;
			doc = pref.getDocument(profileName, true);
		}finally{
			pref.bCreateView = false;
			pref.ci = null;
			NotesUtils.recycle( doc );
			pref.releaseTLS();
		}
	}

}
