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
public class NSFDbCompactEvent extends AbstractEMEvent {
	public static DotsEventParams[] params = { DotsEventParams.SourceDbpath, DotsEventParams.Options, DotsEventParams.SizeBefore,
			DotsEventParams.SizeAfter };

	@Override
	public DotsEventParams[] getParams() {
		return params;
	}

	private int compactOptions;
	private long beforeSize;
	private long afterSize;

	/**
	 * @param eventId
	 */
	public NSFDbCompactEvent(int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public NSFDbCompactEvent() {
		super(IExtensionManagerEvent.EM_NSFDBCOMPACT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values) throws InvalidEventException {
		// sprintf( szBuffer, "%s,%x,%x,%x", szPathName, options, retStats[0], retStats[1] );
		checkValues(values, 4);

		setDbPath(values[0]);
		setCompactOptions(parseInt(values[1]));
		setBeforeSize(parseLong(values[2]));
		setAfterSize(parseLong(values[3]));
		return true;
	}

	/**
	 * @return the compactOptions
	 */
	public int getCompactOptions() {
		return compactOptions;
	}

	/**
	 * @param compactOptions
	 *            the compactOptions to set
	 */
	private void setCompactOptions(int compactOptions) {
		this.compactOptions = compactOptions;
	}

	/**
	 * @return the beforeSize
	 */
	public long getBeforeSize() {
		return beforeSize;
	}

	/**
	 * @param beforeSize
	 *            the beforeSize to set
	 */
	private void setBeforeSize(long beforeSize) {
		this.beforeSize = beforeSize;
	}

	/**
	 * @return the afterSize
	 */
	public long getAfterSize() {
		return afterSize;
	}

	/**
	 * @param afterSize
	 *            the afterSize to set
	 */
	private void setAfterSize(long afterSize) {
		this.afterSize = afterSize;
	}

}
