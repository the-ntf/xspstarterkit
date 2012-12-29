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
public class FTIndexEvent extends AbstractEMEvent {
	public static DotsEventParams[] params = { DotsEventParams.SourceDbpath, DotsEventParams.Options, DotsEventParams.Stopfile,
			DotsEventParams.DocsAdded, DotsEventParams.DocsUpdated, DotsEventParams.DocsDelete, DotsEventParams.BytesIndexed };

	@Override
	public DotsEventParams[] getParams() {
		return params;
	}

	private int options;
	private String stopFile;
	private long docsAdded;
	private long docsUpdated;
	private long docsDeleted;
	private long bytesIndexed;

	/**
	 * @param eventId
	 */
	public FTIndexEvent(int eventId) {
		super(eventId);
	}

	/**
 * 
 */
	public FTIndexEvent() {
		super(IExtensionManagerEvent.EM_FTINDEX);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values) throws InvalidEventException {
		// sprintf( szBuffer, "%s,%x,%s,%x,%x,%x,%x", szPathName, options, stopFile, retStats->DocsAdded, retStats->DocsUpdated,
		// retStats->DocsDeleted, retStats->BytesIndexed );
		checkValues(values, 7);

		setDbPath(values[0]);
		setOptions(parseInt(values[1]));
		setStopFile(values[2]);
		setDocsAdded(parseLong(values[3]));
		setDocsUpdated(parseLong(values[4]));
		setDocsDeleted(parseLong(values[5]));
		setBytesIndexed(parseLong(values[6]));
		if (values.length > 7) {
			setUsername(values[7]);
		}
		return true;
	}

	/**
	 * @param options
	 */
	private void setOptions(int options) {
		this.options = options;
	}

	/**
	 * @return
	 */
	public int getOptions() {
		return options;
	}

	/**
	 * @param stopFile
	 */
	private void setStopFile(String stopFile) {
		this.stopFile = stopFile;
	}

	/**
	 * @return
	 */
	public String getStopFile() {
		return stopFile;
	}

	/**
	 * @param docsAdded
	 */
	public void setDocsAdded(long docsAdded) {
		this.docsAdded = docsAdded;
	}

	/**
	 * @return
	 */
	public long getDocsAdded() {
		return docsAdded;
	}

	/**
	 * @param docsDeleted
	 */
	public void setDocsDeleted(long docsDeleted) {
		this.docsDeleted = docsDeleted;
	}

	/**
	 * @return
	 */
	public long getDocsDeleted() {
		return docsDeleted;
	}

	/**
	 * @param docsUpdated
	 */
	public void setDocsUpdated(long docsUpdated) {
		this.docsUpdated = docsUpdated;
	}

	/**
	 * @return
	 */
	public long getDocsUpdated() {
		return docsUpdated;
	}

	/**
	 * @param bytesIndexed
	 */
	public void setBytesIndexed(long bytesIndexed) {
		this.bytesIndexed = bytesIndexed;
	}

	/**
	 * @return
	 */
	public long getBytesIndexed() {
		return bytesIndexed;
	}

}
