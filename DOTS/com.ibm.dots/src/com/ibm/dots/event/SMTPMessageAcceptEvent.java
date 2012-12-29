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
public class SMTPMessageAcceptEvent extends AbstractEMEvent {
	public static DotsEventParams[] params = { DotsEventParams.SessionId, DotsEventParams.SourceDbpath, DotsEventParams.Noteid,
			DotsEventParams.SMTPReply, DotsEventParams.SMTPReplyLength };

	@Override
	public DotsEventParams[] getParams() {
		return params;
	}

	private long sessionID;
	private String SMTPReply;
	private long SMTPReplyLength;

	/**
	 * @param eventId
	 */
	public SMTPMessageAcceptEvent(int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public SMTPMessageAcceptEvent() {
		super(IExtensionManagerEvent.EM_SMTPMESSAGEACCEPT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values) throws InvalidEventException {
		// sprintf( szBuffer, "%x,%s,%x,%s,%x", sessionID, szPathName, noteID, safeCharPtr( smtpReply ), SMTPReplyLength );
		checkValues(values, 5);
		setSessionID(Long.parseLong(values[0]));
		setDbPath(values[1]);
		setNoteId(values[2]);
		setSMTPReply(values[3]);
		setSMTPReplyLength(Long.parseLong(values[4]));
		return true;
	}

	/**
	 * @return the sessionID
	 */
	public long getSessionID() {
		return sessionID;
	}

	/**
	 * @param sessionID
	 *            the sessionID to set
	 */
	private void setSessionID(long sessionID) {
		this.sessionID = sessionID;
	}

	/**
	 * @return the sMTPReply
	 */
	public String getSMTPReply() {
		return SMTPReply;
	}

	/**
	 * @param sMTPReply
	 *            the sMTPReply to set
	 */
	private void setSMTPReply(String sMTPReply) {
		SMTPReply = sMTPReply;
	}

	/**
	 * @return the sMTPReplyLength
	 */
	public long getSMTPReplyLength() {
		return SMTPReplyLength;
	}

	/**
	 * @param sMTPReplyLength
	 *            the sMTPReplyLength to set
	 */
	private void setSMTPReplyLength(long sMTPReplyLength) {
		SMTPReplyLength = sMTPReplyLength;
	}

}
