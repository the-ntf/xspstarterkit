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
package com.ibm.dots.internal.preferences;

/**
 * @author dtaieb
 *
 */
public interface IPrefConstants {

	public static final String CONFIGURATION_VIEW_NAME = "OSGi Configuration";   // $NON-NLS-1$

	public static final String ROOT_CONFIG_FORM_DXLPATH = "res/rootConfigForm.dxl"; // $NON-NLS-1$
	public static final String DEFAULT_CONFIG_FORM_DXLPATH = "res/defaultConfigForm.dxl"; // $NON-NLS-1$
	public static final String CONFIGURATION_VIEW_DXL = "res/configurationView.dxl"; // $NON-NLS-1$

	public static final String FIELD_PREF_PREFIX = "pref_"; // $NON-NLS-1$
	public static final String FIELD_PREF_PREFIX_OBJECT = "pref_o_"; // $NON-NLS-1$
	public static final String FIELD_DISPLAY_PREFERENCES = "DisplayPreferences"; // $NON-NLS-1$
	public static final String FORM_FIELD = "Form"; // $NON-NLS-1$
	public static final Object PLUGIN_PREF_FORM_NAME = "PluginPref"; // $NON-NLS-1$
	public static final String PREF_NODE_FIELD_NAME = "Name"; // $NON-NLS-1$
	public static final String SERVER_FIELD_NAME = "Server"; // $NON-NLS-1$

	public static final String OSGI_PREFIX = "$OSGi_"; // $NON-NLS-1$
	public static final String OSGI_INSTANCE_FIELD_NAME = OSGI_PREFIX + "Profile"; // $NON-NLS-1$
}
