/*
 * � Copyright IBM Corp. 2009,2010
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
public class NSFNoteDetachFileEvent extends AbstractEMEvent {
	private static DotsEventParams[] params = { DotsEventParams.SourceDbpath, DotsEventParams.Noteid, DotsEventParams.Filename };

	@Override
	public DotsEventParams[] getParams() {
		return params;
	}

	private String fileName;

	/**
	 * @param eventId
	 */
	public NSFNoteDetachFileEvent(int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public NSFNoteDetachFileEvent() {
		super(IExtensionManagerEvent.EM_NSFNOTEDETACHFILE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values) throws InvalidEventException {
		// sprintf( szBuffer, "%s,%x,%s", szPathName, noteID, item_name );
		checkValues(values, 3);
		setDbPath(values[0]);
		setNoteId(values[1]);
		setFileName(values[2]);
		return true;
	}

	/**
	 * @param fileName
	 */
	private void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}

}
