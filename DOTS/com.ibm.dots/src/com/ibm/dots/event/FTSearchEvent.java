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
public class FTSearchEvent extends AbstractEMEvent {
	
	private String query;
	private long options;
	private int limit;
	private long retNumDocs;

	/**
	 * @param eventId
	 */
	public FTSearchEvent(int eventId) {
		super(eventId);
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.event.AbstractEMEvent#parseEventBuffer(java.lang.String[])
	 */
	@Override
	protected boolean parseEventBuffer(String[] values) throws InvalidEventException {
		//sprintf( szBuffer, "%s,%s,%x,%x,%x", szPathName, query, options, limit, retNumDocs == NULL ? 0 : *retNumDocs );
		checkValues(values, 5);

		setDbPath( values[0] );
		setQuery( values[1] );
		setOptions( parseLong( values[2] ) );
		setLimit( parseInt( values[3] ) );
		setRetNumDocs( Long.parseLong( values[4] ) ) ;
		return true;
	}
	
	/**
	 * @param query
	 */
	private void setQuery(String query) {
		this.query = query;
	}
	
	/**
	 * @return
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @return the options
	 */
	public long getOptions() {
		return options;
	}

	/**
	 * @param options the options to set
	 */
	private void setOptions(long options) {
		this.options = options;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	private void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return the retNumDocs
	 */
	public long getRetNumDocs() {
		return retNumDocs;
	}

	/**
	 * @param retNumDocs the retNumDocs to set
	 */
	private void setRetNumDocs(long retNumDocs) {
		this.retNumDocs = retNumDocs;
	}
	
	

}
