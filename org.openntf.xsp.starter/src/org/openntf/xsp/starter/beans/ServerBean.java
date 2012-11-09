/*
 * © Copyright GBS Inc 2011
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
package org.openntf.xsp.starter.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ServerBean extends AbstractBean {
	private static ServerBean _instance;
	private static final long serialVersionUID = 1548144829008721911L;
	private final Map<Serializable, Serializable> _map = new HashMap<Serializable, Serializable>();

	private ServerBean() {

	}

	public static ServerBean getCurrentInstance() {
		if (_instance == null) {
			_instance = new ServerBean();
		}
		return _instance;
	}

	public Serializable get(Object key) {
		return _map.get(key);
	}

	public Serializable put(Serializable key, Serializable value) {
		return _map.put(key, value);
	}

}
