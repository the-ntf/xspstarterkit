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
public class MailSendNoteEvent extends AbstractEMEvent {
	
	private String from;
	private int flags;

	/**
	 * @param eventId
	 */
	public MailSendNoteEvent(int eventId) {
		super(eventId);
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values)	throws InvalidEventException {
		// sprintf( szBuffer, "%s,%x", szFrom, flags );
		checkValues(values, 2 );
		setFrom( values[0] );
		setFlags( parseInt( values[1] ) );
		return true;
	}
	
	/**
	 * @param from
	 */
	private void setFrom(String from) {
		this.from = from;
	}
	
	/**
	 * @param flags
	 */
	private void setFlags(int flags) {
		this.flags = flags;
	}
	
	/**
	 * @return
	 */
	public String getFrom() {
		return from;
	}
	
	/**
	 * @return
	 */
	public int getFlags() {
		return flags;
	}

}
