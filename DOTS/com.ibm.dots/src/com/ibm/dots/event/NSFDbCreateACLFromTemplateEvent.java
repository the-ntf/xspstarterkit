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
public class NSFDbCreateACLFromTemplateEvent extends AbstractEMEvent {
	public static DotsEventParams[] params = { DotsEventParams.SourceDbpath, DotsEventParams.DestDbpath, DotsEventParams.Manager,
			DotsEventParams.DefaultAccess };

	@Override
	public DotsEventParams[] getParams() {
		return params;
	}

	private String manager;
	private int defaultAccessLevel;

	/**
	 * @param eventId
	 */
	public NSFDbCreateACLFromTemplateEvent(int eventId) {
		super(eventId);
	}

	/**
	 * 
	 */
	public NSFDbCreateACLFromTemplateEvent() {
		super(IExtensionManagerEvent.EM_NSFDBCREATEACLFROMTEMPLATE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values) throws InvalidEventException {
		// sprintf( szBuffer, "%s,%s,%s,%x", szSrcPathName, szDestPathName, (Manager==NULL?"":Manager), defaultAccessLevel );
		checkValues(values, 4);
		setDbPath(values[0]);
		setDestDbPath(values[1]);
		setManager(values[2]);
		setDefaultAccessLevel(parseInt(values[3]));
		return true;
	}

	/**
	 * @return the manager
	 */
	public String getManager() {
		return manager;
	}

	/**
	 * @param manager
	 *            the manager to set
	 */
	private void setManager(String manager) {
		this.manager = manager;
	}

	/**
	 * @return the defaultAccessLevel
	 */
	public int getDefaultAccessLevel() {
		return defaultAccessLevel;
	}

	/**
	 * @param defaultAccessLevel
	 *            the defaultAccessLevel to set
	 */
	private void setDefaultAccessLevel(int defaultAccessLevel) {
		this.defaultAccessLevel = defaultAccessLevel;
	}
}