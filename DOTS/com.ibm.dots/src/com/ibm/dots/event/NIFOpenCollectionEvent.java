/*
 * © Copyright IBM Corp. 2009,2010
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
package com.ibm.dots.event;

/**
 * @author dtaieb
 *
 */
public class NIFOpenCollectionEvent extends AbstractEMEvent {
	
	private String dataDbPath;
	private String viewNoteID;
	private int openFlags;

	/**
	 * @param eventId
	 */
	public NIFOpenCollectionEvent(int eventId) {
		super(eventId);
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values) throws InvalidEventException {
		//sprintf( szBuffer, "%s,%s,%x,%x", szViewDBPathName, szDataDBPathName, viewNoteID, openFlags );
		checkValues(values, 4 );
		
		setDbPath( values[0] );
		setDataDbPath ( values[1] );
		setViewNoteID( values[2] );
		setOpenFlags( parseInt( values[3] ) );
		
		return true;
	}

	/**
	 * @return the dataDbPath
	 */
	public String getDataDbPath() {
		return dataDbPath;
	}

	/**
	 * @param dataDbPath the dataDbPath to set
	 */
	private void setDataDbPath(String dataDbPath) {
		this.dataDbPath = dataDbPath;
	}

	/**
	 * @return the viewNoteID
	 */
	public String getViewNoteID() {
		return viewNoteID;
	}

	/**
	 * @param viewNoteID the viewNoteID to set
	 */
	private void setViewNoteID(String viewNoteID) {
		this.viewNoteID = viewNoteID;
	}

	/**
	 * @return the openFlags
	 */
	public int getOpenFlags() {
		return openFlags;
	}

	/**
	 * @param openFlags the openFlags to set
	 */
	private void setOpenFlags(int openFlags) {
		this.openFlags = openFlags;
	}

}
