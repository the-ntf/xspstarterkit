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
package org.openntf.xsp.starter.implicit;

import java.io.Serializable;

import org.openntf.xsp.starter.Activator;
import org.openntf.xsp.starter.beans.ServerBean;

import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.model.DataObject;

public class ImplicitObject implements DataObject {
	private final static boolean _debug = Activator.isDebug();

	/*
	 * This could be any object you want it to be.
	 */

	public ImplicitObject() {
		_debugOut("created");
	}

	protected void _debugOut(String message) {
		if (_debug)
			System.out.println(getClass().getName() + " " + message);
	}

	public Object getValue(Object paramObject) {
		return ServerBean.getCurrentInstance().get(paramObject);
	}

	public void setValue(Object paramObject1, Object paramObject2) {
		if (paramObject1 instanceof Serializable && paramObject2 instanceof Serializable) {
			ServerBean.getCurrentInstance().put((Serializable) paramObject1, (Serializable) paramObject2);
		} else {
			throw new FacesExceptionEx("Server global storage cannot accept non-serializable values");
		}
	}

	public Class<?> getType(Object paramObject) {
		return getValue(paramObject).getClass();
	}

	public boolean isReadOnly(Object paramObject) {
		return false;
	}

}
