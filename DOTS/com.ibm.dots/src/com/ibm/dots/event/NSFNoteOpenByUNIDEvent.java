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
public class NSFNoteOpenByUNIDEvent extends AbstractEMEvent {
	public static DotsEventParams[] params = { DotsEventParams.SourceDbpath, DotsEventParams.Unid, DotsEventParams.Flag };

	@Override
	public DotsEventParams[] getParams() {
		return params;
	}

	private String unid;
	private int openFlag;

	/**
	 * @param eventId
	 */
	public NSFNoteOpenByUNIDEvent(int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public NSFNoteOpenByUNIDEvent() {
		super(IExtensionManagerEvent.EM_NSFNOTEOPENBYUNID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values) throws InvalidEventException {
		// sprintf( szBuffer, "%s,%x%x%x%x,%x", szPathName, pUNID->File.Innards[0], pUNID->File.Innards[1], pUNID->Note.Innards[0],
		// pUNID->Note.Innards[1], openFlags);
		checkValues(values, 6);

		setDbPath(values[0]);
		setUnid(values[1] + values[2] + values[3] + values[4]);
		setOpenFlag(parseInt(values[5]));
		return true;
	}

	/**
	 * @return
	 */
	public String getUnid() {
		return unid;
	}

	/**
	 * @param unid
	 */
	public void setUnid(String unid) {
		this.unid = unid;
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
