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
public class NSFDbCopyEvent extends AbstractEMEvent {
	public static DotsEventParams[] params = { DotsEventParams.SourceDbpath, DotsEventParams.DestDbpath, DotsEventParams.SinceTimeDate,
			DotsEventParams.NoteClass };

	@Override
	public DotsEventParams[] getParams() {
		return params;
	}

	private String sinceTimeDate;
	private int noteClassMask;

	/**
	 * @param eventId
	 */
	public NSFDbCopyEvent(int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public NSFDbCopyEvent() {
		super(IExtensionManagerEvent.EM_NSFDBCOPY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values) throws InvalidEventException {
		// sprintf( szBuffer, "%s,%s,%s,%x", szSrcPathName, szDestPathName, since_text, noteClassMask );
		checkValues(values, 4);
		setDbPath(values[0]);
		setDestDbPath(values[1]);
		setSinceTimeDate(values[2]);
		setNoteClassMask(parseInt(values[3]));
		return true;
	}

	/**
	 * @return the sinceTimeDate
	 */
	public String getSinceTimeDate() {
		return sinceTimeDate;
	}

	/**
	 * @param sinceTimeDate
	 *            the sinceTimeDate to set
	 */
	private void setSinceTimeDate(String sinceTimeDate) {
		this.sinceTimeDate = sinceTimeDate;
	}

	/**
	 * @return the noteClassMask
	 */
	public int getNoteClassMask() {
		return noteClassMask;
	}

	/**
	 * @param noteClassMask
	 *            the noteClassMask to set
	 */
	private void setNoteClassMask(int noteClassMask) {
		this.noteClassMask = noteClassMask;
	}

}
