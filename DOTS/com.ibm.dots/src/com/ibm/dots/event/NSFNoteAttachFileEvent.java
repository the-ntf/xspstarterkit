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
public class NSFNoteAttachFileEvent extends AbstractEMEvent {
	private static DotsEventParams[] params = { DotsEventParams.SourceDbpath, DotsEventParams.Noteid, DotsEventParams.Filename,
			DotsEventParams.OriginalFilename, DotsEventParams.EncodingType };

	@Override
	public DotsEventParams[] getParams() {
		return params;
	}

	private String fileName;
	private String orgFileName;
	private int encodingType;

	/**
	 * @param eventId
	 */
	public NSFNoteAttachFileEvent(int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public NSFNoteAttachFileEvent() {
		super(IExtensionManagerEvent.EM_NSFNOTEATTACHFILE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values) throws InvalidEventException {
		// sprintf( szBuffer, "%s,%x,%s,%s,%x", szPathName, noteID,file_name, orgPathName, encodingType );
		checkValues(values, 5);
		setDbPath(values[0]);
		setNoteId(values[1]);
		setFileName(values[2]);
		setOrgFileName(values[3]);
		setEncodingType(parseInt(values[4]));
		return true;
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

	/**
	 * @return the orgFileName
	 */
	public String getOrgFileName() {
		return orgFileName;
	}

	/**
	 * @param orgFileName
	 *            the orgFileName to set
	 */
	private void setOrgFileName(String orgFileName) {
		this.orgFileName = orgFileName;
	}

	/**
	 * @return the encodingType
	 */
	public int getEncodingType() {
		return encodingType;
	}

	/**
	 * @param encodingType
	 *            the encodingType to set
	 */
	private void setEncodingType(int encodingType) {
		this.encodingType = encodingType;
	}

}
