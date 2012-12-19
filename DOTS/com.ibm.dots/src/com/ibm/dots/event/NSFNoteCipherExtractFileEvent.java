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
public class NSFNoteCipherExtractFileEvent extends AbstractEMEvent {
	
	private long extractFlags;
	private String itemName;
	private String fileName;

	/**
	 * @param eventId
	 */
	public NSFNoteCipherExtractFileEvent(int eventId) {
		super(eventId);
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values)throws InvalidEventException {
		// sprintf( szBuffer, "%s,%x,%x,%s,%s", szPathName, noteID, extractFlags, safeCharPtr( item_name ), safeCharPtr( fileName ) );
		checkValues(values, 5);
		setDbPath( values[0]);
		setNoteId( values[1] );
		setExtractFlags( Long.parseLong( values[2] ) );
		setItemName( values[3] );
		setFileName( values[4] );
		return true;
	}

	/**
	 * @return the extractFlags
	 */
	public long getExtractFlags() {
		return extractFlags;
	}

	/**
	 * @param extractFlags the extractFlags to set
	 */
	private void setExtractFlags(long extractFlags) {
		this.extractFlags = extractFlags;
	}

	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName the itemName to set
	 */
	private void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	private void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
