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
public class SMTPConnectEvent extends AbstractEMEvent {
	
	private long sessionID;
	private String remoteIP;
	private String remoteHost;
	private boolean possibleRelay;
	private String greeting;
	private long maxGreetingLen;

	/**
	 * @param eventId
	 */
	public SMTPConnectEvent(int eventId) {
		super(eventId);
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values)	throws InvalidEventException {
		// sprintf( szBuffer, "%x,%s,%s,%x,%s,%x", sessionID, safeCharPtr( remoteIP ), safeCharPtr( remoteHost ), possibleRelay, safeCharPtr( greeting ), maxGreetingLength );
		checkValues(values, 6);
		setSessionID( Long.parseLong( values[0] ) );
		setRemoteIP( values[1] );
		setRemoteHost( values[2] );
		setPossibleRelay( Boolean.parseBoolean( values[3] ) );
		setGreeting( values[4] );
		setMaxGreetingLen( Long.parseLong( values[5] ) );
		return true;
	}

	/**
	 * @return the sessionID
	 */
	public long getSessionID() {
		return sessionID;
	}

	/**
	 * @param sessionID the sessionID to set
	 */
	private void setSessionID(long sessionID) {
		this.sessionID = sessionID;
	}

	/**
	 * @return the remoteIP
	 */
	public String getRemoteIP() {
		return remoteIP;
	}

	/**
	 * @param remoteIP the remoteIP to set
	 */
	private void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}

	/**
	 * @return the remoteHost
	 */
	public String getRemoteHost() {
		return remoteHost;
	}

	/**
	 * @param remoteHost the remoteHost to set
	 */
	private void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	/**
	 * @return the possibleRelay
	 */
	public boolean isPossibleRelay() {
		return possibleRelay;
	}

	/**
	 * @param possibleRelay the possibleRelay to set
	 */
	private void setPossibleRelay(boolean possibleRelay) {
		this.possibleRelay = possibleRelay;
	}

	/**
	 * @return the greeting
	 */
	public String getGreeting() {
		return greeting;
	}

	/**
	 * @param greeting the greeting to set
	 */
	private void setGreeting(String greeting) {
		this.greeting = greeting;
	}

	/**
	 * @return the maxGreetingLen
	 */
	public long getMaxGreetingLen() {
		return maxGreetingLen;
	}

	/**
	 * @param maxGreetingLen the maxGreetingLen to set
	 */
	private void setMaxGreetingLen(long maxGreetingLen) {
		this.maxGreetingLen = maxGreetingLen;
	}

}
