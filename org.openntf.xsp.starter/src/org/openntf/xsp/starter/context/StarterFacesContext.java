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
package org.openntf.xsp.starter.context;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.openntf.xsp.starter.Activator;

import com.ibm.xsp.application.ComponentData;
import com.ibm.xsp.domino.context.DominoFacesContext;
import com.ibm.xsp.webapp.XspHttpServletResponse;

public class StarterFacesContext extends DominoFacesContext {

	private ExternalContext external;
	private final static boolean _debug = Activator.isDebug();
	static {
		if (_debug)
			System.out.println(StarterFacesContext.class.getName() + " loaded");
	}

	public StarterFacesContext(FacesContext paramFacesContext) {
		super(paramFacesContext);
		if (_debug) {
			System.out.println(getClass().getName() + " created from delegate of " + paramFacesContext.getClass().getName());
			System.out.println("RenderKit: " + getRenderKit().getClass().getName());
		}
	}

	@Override
	public ExternalContext getExternalContext() {
		if (external == null) {
			ExternalContext ectx = super.getExternalContext();
			external = new StarterExternalContext(ectx);
		}
		return external;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.context.FacesContextExImpl#getXspResponse()
	 */
	@Override
	public XspHttpServletResponse getXspResponse() {
		// TODO Auto-generated method stub
		return super.getXspResponse();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.context.FacesContextExImpl#getContextClassLoader()
	 */
	@Override
	public ClassLoader getContextClassLoader() {
		// TODO Auto-generated method stub
		return super.getContextClassLoader();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.context.FacesContextExImpl#getComponentData()
	 */
	@Override
	public ComponentData getComponentData() {
		// TODO Auto-generated method stub
		return super.getComponentData();
	}
}
