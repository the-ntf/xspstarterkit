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
package com.ibm.dots.internal;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import lotus.domino.Session;

/**
 * @author dtaieb
 * Class that proxy calls to the OSGiProfile
 */
public class OSGiProfileProxy {

	private static volatile String profileName;
	private static volatile Object osgiProfile = null;
	private static volatile String confDb;

	/**
	 * 
	 */
	private OSGiProfileProxy() {
	}

	/**
	 * @return
	 */
	public static String getProfileName() {
		if ( profileName == null ){
			synchronized ( OSGiProfileProxy.class ){
				if ( profileName == null ){
					try{
						return (String)getOSGiProfile().getClass().getMethod( "getMQName" ).invoke( osgiProfile ); // $NON-NLS-1$
					}catch( Throwable t ){
						throw new IllegalStateException( t );
					}
				}
			}
		}
		return profileName;
	}

	/**
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private static Object getOSGiProfile() throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if ( osgiProfile == null ){
			synchronized ( OSGiProfileProxy.class ){
				if ( osgiProfile == null ){
					Class<?> osgiLauncherClass = Class.forName( "com.ibm.dots.launcher.OSGILauncher"); // $NON-NLS-1$
					osgiProfile = osgiLauncherClass.getMethod( "getOSGiProfile" ).invoke( null ); // $NON-NLS-1$
				}
			}
		}
		return osgiProfile;
	}

	/**
	 * @return
	 */
	public static String getOSGiConfDb() throws Exception {
		if ( confDb == null ){
			synchronized (OSGiProfileProxy.class ){
				if ( confDb == null ){
					confDb = (String)getOSGiProfile().getClass().getMethod( "getConfDbPath", Session.class ).invoke( getOSGiProfile(), new Object[]{null} ); // $NON-NLS-1$
					if ( confDb == null ){
						confDb = "";
					}
				}
			}
		}
		return "".equals( confDb ) ? null : confDb;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public static String getProfileInfo() throws Exception {
		return (String)getOSGiProfile().getClass().getMethod( "getProfileInfo" ).invoke( getOSGiProfile() ); // $NON-NLS-1$
	}

	/**
	 * @return
	 * @throws Exception 
	 */
	public static boolean hasConfigDb() throws Exception {
		return getOSGiConfDb() != null;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public static boolean isUsingConfigDb() throws Exception{
		return (Boolean)getOSGiProfile().getClass().getMethod( "isUsingConfigDb" ).invoke( getOSGiProfile() ); // $NON-NLS-1$
	}

	/**
	 * @param profileName
	 * @return
	 * @throws Exception
	 */
	public static boolean hasProfile(String profileName) throws Exception {
		InputStream is = null;
		try{
			is = (InputStream) getOSGiProfile().getClass().getMethod( "getProfileFromConfigurationDb", String.class ) // $NON-NLS-1$
			.invoke( getOSGiProfile(), profileName );
			return is != null;
		}finally{
			if ( is != null ){
				is.close();
			}
		}
	}

}
