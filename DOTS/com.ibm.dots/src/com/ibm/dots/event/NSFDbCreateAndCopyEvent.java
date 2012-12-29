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
public class NSFDbCreateAndCopyEvent extends AbstractEMEvent {
	public static DotsEventParams[] params = { DotsEventParams.SourceDbpath, DotsEventParams.DestDbpath, DotsEventParams.NoteClass,
			DotsEventParams.Limit, DotsEventParams.Flag };

	@Override
	public DotsEventParams[] getParams() {
		return params;
	}

	private int noteClass;
	private int limit;
	private long flags;

	/**
	 * @param eventId
	 */
	public NSFDbCreateAndCopyEvent(int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public NSFDbCreateAndCopyEvent() {
		super(IExtensionManagerEvent.EM_NSFDBCREATEANDCOPY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values) throws InvalidEventException {
		// sprintf( szBuffer, "%s,%s,%x,%x,%x", szSrcPathName, szDestPathName, noteClass, limit, flags );
		checkValues(values, 5);
		setDbPath(values[0]);
		setDestDbPath(values[1]);
		setNoteClass(parseInt(values[2]));
		setLimit(parseInt(values[3]));
		setFlags(parseLong(values[4]));
		return true;
	}

	/**
	 * @return the noteClass
	 */
	public int getNoteClass() {
		return noteClass;
	}

	/**
	 * @param noteClass
	 *            the noteClass to set
	 */
	private void setNoteClass(int noteClass) {
		this.noteClass = noteClass;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	private void setLimit(int limit) {
		this.limit = limit;
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

}
