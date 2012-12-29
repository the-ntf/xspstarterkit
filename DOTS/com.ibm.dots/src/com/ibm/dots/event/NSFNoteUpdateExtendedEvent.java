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
public class NSFNoteUpdateExtendedEvent extends AbstractEMEvent {
	public static DotsEventParams[] params = { DotsEventParams.SourceDbpath, DotsEventParams.Noteid, DotsEventParams.Flag };

	@Override
	public DotsEventParams[] getParams() {
		return params;
	}

	private int updateFlag;

	public NSFNoteUpdateExtendedEvent() {
		super(IExtensionManagerEvent.EM_NSFNOTEUPDATEXTENDED);
	}

	/**
	 * @param eventId
	 */
	public NSFNoteUpdateExtendedEvent(int eventId) {
		super(eventId);
	}

	@Override
	protected boolean parseEventBuffer(String[] values) throws InvalidEventException {
		checkValues(values, 3);

		setDbPath(values[0]);
		setNoteId(values[1]);
		setFlag(values[2]);
		if (values.length > 3) {
			setUsername(values[3]);
		}

		return true;
	}

	/**
	 * @param sFlag
	 */
	private void setFlag(String sFlag) {
		this.updateFlag = parseInt(sFlag);
	}

	/**
	 * @return
	 */
	public int getUpdateFlag() {
		return updateFlag;
	}
}
