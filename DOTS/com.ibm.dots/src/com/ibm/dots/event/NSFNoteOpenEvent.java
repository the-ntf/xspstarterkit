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
 * @author dtaieb signature: STATUS LNPUBLIC NSFNoteOpen(DBHANDLE db_handle,NOTEID note_id,WORD open_flags,NOTEHANDLE far *note_handle);
 */
public class NSFNoteOpenEvent extends AbstractEMEvent {
	public static DotsEventParams[] params = { DotsEventParams.SourceDbpath, DotsEventParams.Noteid, DotsEventParams.Flag };

	@Override
	public DotsEventParams[] getParams() {
		return params;
	}

	private int openFlag;

	/**
	 * @param eventId
	 */
	public NSFNoteOpenEvent(int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public NSFNoteOpenEvent() {
		super(IExtensionManagerEvent.EM_NSFNOTEOPEN);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values) throws InvalidEventException {
		checkValues(values, 3);

		setDbPath(values[0]);
		setNoteId(values[1]);
		setOpenFlag(parseInt(values[2]));

		return true;
	}

	/**
	 * @return
	 */
	public int getOpenFlag() {
		return openFlag;
	}

	/**
	 * @param openFlag
	 */
	public void setOpenFlag(int openFlag) {
		this.openFlag = openFlag;
	}

}
