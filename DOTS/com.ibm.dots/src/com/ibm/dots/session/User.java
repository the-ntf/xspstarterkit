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
package com.ibm.dots.session;

import com.ibm.dots.utils.StringUtils;

/**
 * @author dtaieb
 *
 */
public class User {
	
	private static final String ANONYMOUS = "anonymous";
	
	private long userListHandle;
	private String userName;

	/**
	 * @param userName
	 * @param password
	 * @throws UserAuthException
	 */
	private User( String userName, String password ) throws UserAuthException {
		authenticate( userName, password );
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		if ( userName != null ){
			sb.append( "[" + userName + "]");
		}else{
			sb.append( "Empty username");
			return sb.toString();
		}
		
		if ( userListHandle > 0 ){
			sb.append( " - NamesList contents: " );
			getNamesListContents( userListHandle, new Object(){
				@SuppressWarnings("unused")
				public void nextName( String userName ){
					sb.append( "-" + userName );
				}
			});
		}else{
			sb.append( " - Empty Names List " );
		}
		return sb.toString();
	}
	
	/**
	 * @param userListHandle
	 * @param callback 
	 * @return
	 */
	private native void getNamesListContents(long userListHandle, Object callback);

	/**
	 * @param userName
	 * @param password
	 * @throws UserAuthException
	 */
	private void authenticate( String userName, String password ) throws UserAuthException{
		if ( StringUtils.isEmpty( userName )){
			throw new IllegalArgumentException( "userName argument not specified" );
		}
		
		if ( StringUtils.isEmpty( password ) && !ANONYMOUS.equalsIgnoreCase( userName ) ){
			throw new IllegalArgumentException( "password argument not specified" );
		}
		
		//Make sure we dispose any previous authenticated user
		dispose();
		
		this.userName = userName;
		userListHandle = authenticateUser( userName, password );
	}
	
	/**
	 * @param userName
	 * @param password
	 * @return
	 * @throws UserAuthException
	 */
	public static User createUser( String userName, String password ) throws UserAuthException {
		return new User( userName, password );
	}
	
	/**
	 * @return
	 * @throws UserAuthException
	 */
	public static User createAnonymousUser() throws UserAuthException{
		return new User( ANONYMOUS, null );
	}
	
	/**
	 * @return
	 */
	public long getUserListHandle() {
		return userListHandle;
	}
	
	/**
	 * @return
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * @return
	 */
	public boolean isAuthenticated(){
		return userListHandle > 0;
	}
	
	/**
	 * 
	 */
	public void dispose(){
		userName = null;
		if ( userListHandle > 0 ){
			freeUserListHandle( userListHandle );
			userListHandle = 0;
		}
	}

	/**
	 * @param userListHandle
	 */
	private native void freeUserListHandle(long userListHandle);
	
	/**
	 * @param userName
	 * @param password
	 * @return
	 */
	private native long authenticateUser(String userName, String password) throws UserAuthException;

}
