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
public class MediaRecoveryEvent extends AbstractEMEvent {
	
	private boolean isInsert;
	private boolean isUpdate;
	private boolean isDelete;

	/**
	 * @param eventId
	 */
	public MediaRecoveryEvent(int eventId) {
		super(eventId);
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values)throws InvalidEventException {
		// sprintf( szBuffer, "%s,%x,%x,%x,%x", szPathName, noteID, isInsert, isUpdate, isDelete );
		checkValues(values, 5);
		setDbPath( values[0] );
		setNoteId( values[1] );		
		setInsert( Boolean.parseBoolean( values[2] ) ) ;
		setUpdate( Boolean.parseBoolean( values[3] ) ) ;
		setDelete( Boolean.parseBoolean( values[4] ) ) ;
		return true;
	}

	/**
	 * @return the isInsert
	 */
	public boolean isInsert() {
		return isInsert;
	}

	/**
	 * @param isInsert the isInsert to set
	 */
	private void setInsert(boolean isInsert) {
		this.isInsert = isInsert;
	}

	/**
	 * @return the isUpdate
	 */
	public boolean isUpdate() {
		return isUpdate;
	}

	/**
	 * @param isUpdate the isUpdate to set
	 */
	private void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	/**
	 * @return the isDelete
	 */
	public boolean isDelete() {
		return isDelete;
	}

	/**
	 * @param isDelete the isDelete to set
	 */
	private void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

}
