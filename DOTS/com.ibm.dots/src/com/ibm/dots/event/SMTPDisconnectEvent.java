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
public class SMTPDisconnectEvent extends AbstractEMEvent {
	
	private long sessionID;

	/**
	 * @param eventId
	 */
	public SMTPDisconnectEvent(int eventId) {
		super(eventId);
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values)throws InvalidEventException {
		// sprintf( szBuffer, "%x", sessionID);
		checkValues(values, 1);
		setSessionID( Long.parseLong( values[0] ) );
		return true;
	}
	
	/**
	 * @param sessionID
	 */
	private void setSessionID(long sessionID) {
		this.sessionID = sessionID;
	}
	
	/**
	 * @return
	 */
	public long getSessionID() {
		return sessionID;
	}
}
