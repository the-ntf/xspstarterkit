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
public class AdminPProcessRequestEvent extends AbstractEMEvent {

	private String noteIdResponse;
	private String noteIdRequest;

	/**
	 * @param eventId
	 */
	public AdminPProcessRequestEvent(int eventId) {
		super(eventId);
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values) throws InvalidEventException {
		checkValues( values, 3 );		

		setDbPath( values[0] );
		setNoteIdRequest( values[1] );
		setNoteIdResponse( values[2] );
		
		return true;
	}

	/**
	 * @param noteId
	 */
	private void setNoteIdResponse(String noteId) {
		this.noteIdResponse = noteId;
	}
	
	/**
	 * @return
	 */
	public String getNoteIdResponse() {
		return noteIdResponse;
	}
	
	/**
	 * @return
	 */
	public String getNoteIdRequest() {
		return noteIdRequest;
	}

	/**
	 * @param noteId
	 */
	private void setNoteIdRequest(String noteId) {
		this.noteIdRequest = noteId;
	}

}
