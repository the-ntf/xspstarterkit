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
public class NSFNoteExtractFileEvent extends AbstractEMEvent {
	private static DotsEventParams[] params = { DotsEventParams.SourceDbpath, DotsEventParams.Noteid, DotsEventParams.Itemname,
			DotsEventParams.Filename };

	@Override
	public DotsEventParams[] getParams() {
		return params;
	}

	private String itemName;
	private String fileName;

	/**
	 * @param eventId
	 */
	public NSFNoteExtractFileEvent(int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public NSFNoteExtractFileEvent() {
		super(IExtensionManagerEvent.EM_NSFNOTEEXTRACTFILE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values) throws InvalidEventException {
		// sprintf( szBuffer, "%s,%x,%s,%s", szPathName, noteID, item_name, file_name );
		checkValues(values, 4);
		setDbPath(values[0]);
		setNoteId(values[1]);
		setItemName(values[2]);
		setFileName(values[3]);
		return true;
	}

	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName
	 *            the itemName to set
	 */
	private void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	private void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
