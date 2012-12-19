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
public class NSFNoteOpenExtendedEvent extends AbstractEMEvent {
	
	private long flags;
	private long sinceSeqNum;

	/**
	 * @param eventId
	 */
	public NSFNoteOpenExtendedEvent(int eventId) {
		super(eventId);
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values)throws InvalidEventException {
		// sprintf( szBuffer, "%s,%x,%x,%x", szPathName, noteid, flags, sinceSeqNum );
		checkValues(values, 4);
		setDbPath( values[0] );
		setNoteId( values[1] );
		setFlags( Long.parseLong( values[2] ) );
		setSinceSeqNum( Long.parseLong( values[3] ) );
		return true;
	}

	/**
	 * @param flags
	 */
	private void setFlags(long flags) {
		this.flags = flags;
	}
	
	/**
	 * @return
	 */
	public long getFlags() {
		return flags;
	}
	
	/**
	 * @param sinceSeqNum
	 */
	private void setSinceSeqNum(long sinceSeqNum) {
		this.sinceSeqNum = sinceSeqNum;
	}
	
	/**
	 * @return
	 */
	public long getSinceSeqNum() {
		return sinceSeqNum;
	}
}
