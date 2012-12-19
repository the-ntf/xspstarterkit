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
package com.ibm.dots.preferences;

import java.io.IOException;
import java.util.HashSet;
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.NotesException;

import com.ibm.dots.internal.preferences.IPrefConstants;
import com.ibm.dots.utils.NotesUtils;

/**
 * @author dtaieb
 *
 */
public abstract class AbstractConfigurationInitializer implements IPrefConstants {

	private String dxlPath;
	private String pluginId;

	private static final HashSet<String> preservedItemsSet = new HashSet<String>();
	static{
		preservedItemsSet.add( "$ref" ); // $NON-NLS-1$
	}

	/**
	 * 
	 */
	public AbstractConfigurationInitializer() {
	}

	/**
	 * @param dxlPath
	 */
	public void setDxlPath(String pluginId, String dxlPath) {
		this.pluginId = pluginId;
		this.dxlPath = dxlPath;
	}

	/**
	 * @param newDoc
	 * @throws NotesException 
	 */
	public void doInitialize(final Document newDoc) throws NotesException {

		//First initialize the new document
		initializeDefaultConfigurationParameters( newDoc );

		try{
			NotesUtils.processFromDXL( newDoc.getParentDatabase().getParent(), 
					new NotesUtils.IDXLProcessor() {                    
				public void processImportedDoc(Document fromDoc) throws NotesException {
					//Remove all existing items starting with "$"
					removeAllDollarsItems( newDoc );

					Vector<?> items = null;
					try{
						items = fromDoc.getItems();
						for ( Object o : items ){
							Item item = (Item)o;
							String itemName = item.getName();
							if ( itemName.startsWith( "$" ) ){
								newDoc.copyItem( item );
							}
						}
					}finally{
						NotesUtils.recycle( items );
					}                       
				}
			},
			pluginId, dxlPath 
			);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param doc
	 * @throws NotesException
	 */
	private void removeAllDollarsItems(Document doc) throws NotesException {
		Vector<?> items = null;
		try{
			items = doc.getItems();
			for ( Object o : items ){
				Item item = (Item)o;
				String itemName = item.getName();
				if ( itemName.startsWith( "$" ) || 
						"Form".equalsIgnoreCase( itemName ) ){ // $NON-NLS-1$
					if ( !itemName.startsWith( OSGI_PREFIX ) && !preservedItemsSet.contains( itemName.toLowerCase() )){
						item.remove();
					}
				}
			}
		}finally{
			NotesUtils.recycle( items );
		}

	}

	/**
	 * @param newDoc
	 * @throws NotesException
	 */
	protected abstract void initializeDefaultConfigurationParameters(Document newDoc) throws NotesException;

}
