/*
 * � Copyright GBS Inc 2011
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
package org.openntf.xsp.starter.resources;

import javax.faces.context.FacesContext;

import com.ibm.xsp.component.UIViewRootEx;
import com.ibm.xsp.resource.DojoModuleResource;
import com.ibm.xsp.resource.Resource;
import com.ibm.xsp.resource.ScriptResource;
import com.ibm.xsp.resource.StyleSheetResource;

public class Resources {
	public static final DojoModuleResource starterDojoModule1 = new DojoModuleResource("starter.dojo.Module1");
	public static final DojoModuleResource starterDojoModule2 = new DojoModuleResource("starter.dojo.Module2");

	public static final StyleSheetResource starterStyleSheet1 = new StyleSheetResource(ResourceProvider.RESOURCE_PATH
			+ "styles/starterstyle1.css");
	public static final ScriptResource starterClientLibrary1 = new ScriptResource(ResourceProvider.RESOURCE_PATH + "js/starterLibrary1.js",
			true);
	public static final ScriptResource starterClientLibrary2 = new ScriptResource(ResourceProvider.RESOURCE_PATH + "js/starterLibrary2.js",
			true);
	public static final Resource[] starterResourceCollection = { starterDojoModule1, starterDojoModule2, starterStyleSheet1,
			starterClientLibrary1, starterClientLibrary2 };

	// public static void addAllEncodeResources() {
	//
	// }

	public static void addEncodeResources(FacesContext context, Resource[] resources) {
		UIViewRootEx rootEx = (UIViewRootEx) context.getViewRoot();
		addEncodeResources(rootEx, resources);
	}

	public static void addEncodeResources(UIViewRootEx rootEx, Resource[] resources) {
		if (resources != null) {
			for (Resource resource : resources) {
				addEncodeResource(rootEx, resource);
			}
		}
	}

	public static void addEncodeResource(FacesContext context, Resource resource) {
		UIViewRootEx rootEx = (UIViewRootEx) context.getViewRoot();
		addEncodeResource(rootEx, resource);
	}

	public static void addEncodeResource(UIViewRootEx rootEx, Resource resource) {
		rootEx.addEncodeResource(resource);
	}

}
