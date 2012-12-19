/*
 * © Copyright IBM Corp. 2009,2011
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
package com.ibm.dots.preferences;

import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;

/**
 * @author dtaieb
 * Interfaces for Domino Preferences 
 */
public interface IDominoPreferences extends IEclipsePreferences {
	
	/**
	 * @param key
	 * @param defValue
	 * @return
	 */
	public Date getDate( String key, Date defValue );

	/**
	 * @param key
	 * @param date
	 */
	public void putDate(String key, Date date);
	
	/**
	 * @param key
	 * @param defValue
	 * @return
	 */
	public List<Object> getList( String key, List<Object> defValue );
	
	/**
	 * @param key
	 * @param list
	 */
	public void putList(String key, List<Object> list );
}
