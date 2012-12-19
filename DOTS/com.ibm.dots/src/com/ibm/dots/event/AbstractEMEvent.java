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

/**
 * @author dtaieb
 * 
 */
public abstract class AbstractEMEvent implements IExtensionManagerEvent {

	private int eventId;
	private String dbPath;
	private String destDbPath;
	private String noteId;

	/**
	 * @param eventId
	 */
	public AbstractEMEvent(int eventId) {
		this.eventId = eventId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.event.IExtensionManagerEvent#getEventId()
	 */
	@Override
	public final int getEventId() {
		return eventId;
	}

	/**
	 * @param dbPath
	 */
	protected final void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}

	/**
	 * @return
	 */
	public final String getDbPath() {
		return dbPath;
	}

	/**
	 * @return
	 */
	protected final void setDestDbPath(String destDbPath) {
		this.destDbPath = destDbPath;
	}

	/**
	 * @return
	 */
	public final String getDestDbPath() {
		return destDbPath;
	}

	/**
	 * @return
	 */
	public final String getNoteId() {
		return noteId;
	}

	/**
	 * @param noteId
	 */
	protected final void setNoteId(String noteId) {
		int i = Integer.parseInt(noteId, 10);
		this.noteId = Integer.toHexString(i);
	}

	/**
	 * @param values
	 * @param length
	 * @throws InvalidEventException
	 */
	protected void checkValues(String[] values, int length) throws InvalidEventException {
		if (values.length != length) {
			throw new InvalidEventException();
		}
	}

	/**
	 * @param eventBuffer
	 * @return
	 */
	public final boolean parseEventBuffer(String eventBuffer) {
		String[] values = null;
		if (eventBuffer == null || eventBuffer.length() == 0) {
			values = new String[0];
		} else {
			if (eventBuffer.endsWith(",")) {
				eventBuffer += " ";
			}
			values = eventBuffer.split(",");
		}
		try {
			return parseEventBuffer(values);
		} catch (InvalidEventException e) {
			System.out.println("Invalid event found: " + eventBuffer); // $NON-NLS-1$
			return false;
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
	}

	/**
	 * @param value
	 * @return
	 */
	protected int parseInt(String value) {
		return Integer.parseInt(value, 16);
	}

	/**
	 * @param value
	 * @return
	 */
	protected long parseLong(String value) {
		return Long.parseLong(value, 16);
	}

	protected abstract boolean parseEventBuffer(String[] values) throws InvalidEventException;
}
