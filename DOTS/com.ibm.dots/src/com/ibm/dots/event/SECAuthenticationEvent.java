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

import com.ibm.dots.tasklet.events.DotsEventParams;

/**
 * @author dtaieb
 * 
 */
public class SECAuthenticationEvent extends AbstractEMEvent {
	public static DotsEventParams[] params = { DotsEventParams.WEvent, DotsEventParams.RemoteName, DotsEventParams.Flag,
			DotsEventParams.NetProtocol, DotsEventParams.NetAddress };

	@Override
	public DotsEventParams[] getParams() {
		return params;
	}

	private int wEvent;
	private String remoteName;
	private long flags;
	private int netProtocol;
	private String netAddress;

	/**
	 * @param eventId
	 */
	public SECAuthenticationEvent(int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public SECAuthenticationEvent() {
		super(IExtensionManagerEvent.EM_SECAUTHENTICATION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values) throws InvalidEventException {
		// sprintf( szBuffer, "%x,%s,%x,%x,%s", wEvent, pRemoteName == NULL ? "" : pRemoteName , dwFlags, wNetProtocol, netAddress == NULL ?
		// "" : netAddress );
		checkValues(values, 5);
		setwEvent(parseInt(values[0]));
		setRemoteName(values[1]);
		setFlags(parseLong(values[2]));
		setNetProtocol(parseInt(values[3]));
		setNetAddress(values[4]);
		return true;
	}

	/**
	 * @return the wEvent
	 */
	public int getwEvent() {
		return wEvent;
	}

	/**
	 * @param wEvent
	 *            the wEvent to set
	 */
	private void setwEvent(int wEvent) {
		this.wEvent = wEvent;
	}

	/**
	 * @return the remoteName
	 */
	public String getRemoteName() {
		return remoteName;
	}

	/**
	 * @param remoteName
	 *            the remoteName to set
	 */
	private void setRemoteName(String remoteName) {
		this.remoteName = remoteName;
	}

	/**
	 * @return the flags
	 */
	public long getFlags() {
		return flags;
	}

	/**
	 * @param flags
	 *            the flags to set
	 */
	private void setFlags(long flags) {
		this.flags = flags;
	}

	/**
	 * @return the netProtocol
	 */
	public int getNetProtocol() {
		return netProtocol;
	}

	/**
	 * @param netProtocol
	 *            the netProtocol to set
	 */
	private void setNetProtocol(int netProtocol) {
		this.netProtocol = netProtocol;
	}

	/**
	 * @return the netAddress
	 */
	public String getNetAddress() {
		return netAddress;
	}

	/**
	 * @param netAddress
	 *            the netAddress to set
	 */
	private void setNetAddress(String netAddress) {
		this.netAddress = netAddress;
	}

}
