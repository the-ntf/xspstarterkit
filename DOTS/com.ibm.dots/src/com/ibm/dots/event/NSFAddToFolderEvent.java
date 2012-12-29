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
public class NSFAddToFolderEvent extends AbstractEMEvent {
	private static DotsEventParams[] params = { DotsEventParams.SourceDbpath, DotsEventParams.DataDbpath, DotsEventParams.FolderNoteid,
			DotsEventParams.Noteid, DotsEventParams.AddOperation };

	@Override
	public DotsEventParams[] getParams() {
		return params;
	}

	private String dataDbPath;
	private String folderNoteID;
	private String noteID;
	private boolean isAddOperation;

	/**
	 * @param eventId
	 */
	public NSFAddToFolderEvent(int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public NSFAddToFolderEvent() {
		super(IExtensionManagerEvent.EM_NSFADDTOFOLDER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values) throws InvalidEventException {
		// sprintf( szBuffer, "%s,%s,%x,%x,%x", szViewDbPathName, szDataDbPathName, folderNoteID, noteID, isAddOperation );
		checkValues(values, 5);
		setDbPath(values[0]);
		setDataDbPath(values[1]);
		setFolderNoteID(values[2]);
		setNoteID(values[3]);
		setAddOperation(Boolean.parseBoolean(values[4]));
		return true;
	}

	/**
	 * @return the dataDbPath
	 */
	public String getDataDbPath() {
		return dataDbPath;
	}

	/**
	 * @param dataDbPath
	 *            the dataDbPath to set
	 */
	private void setDataDbPath(String dataDbPath) {
		this.dataDbPath = dataDbPath;
	}

	/**
	 * @return the folderNoteID
	 */
	public String getFolderNoteID() {
		return folderNoteID;
	}

	/**
	 * @param folderNoteID
	 *            the folderNoteID to set
	 */
	private void setFolderNoteID(String folderNoteID) {
		this.folderNoteID = folderNoteID;
	}

	/**
	 * @return the noteID
	 */
	public String getNoteID() {
		return noteID;
	}

	/**
	 * @param noteID
	 *            the noteID to set
	 */
	private void setNoteID(String noteID) {
		this.noteID = noteID;
	}

	/**
	 * @return the isAddOperation
	 */
	public boolean isAddOperation() {
		return isAddOperation;
	}

	/**
	 * @param isAddOperation
	 *            the isAddOperation to set
	 */
	private void setAddOperation(boolean isAddOperation) {
		this.isAddOperation = isAddOperation;
	}

}
