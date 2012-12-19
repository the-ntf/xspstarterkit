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

import java.net.URI;

import com.ibm.dots.task.ServerConsole;

/**
 * @author dtaieb
 *
 */
public class OSGiServerConsoleAdaptor extends OSGIConsoleAdaptor {

	private ServerConsole serverConsole;

	/**
	 * @param serverConsole
	 */
	public OSGiServerConsoleAdaptor(ServerConsole serverConsole) {
		this.serverConsole = serverConsole;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.osgi.framework.console.CommandInterpreter#print(java.lang.Object)
	 */
	public void print(Object o) {
		if ( o != null ){
			String s = toString( o );
			String[] lines = s.split("[\r\n]"); // $NON-NLS-1$
			for ( String line : lines ){
				if ( line.length() > 0 ){
					//No need for the leading \t
					line = line.trim();
					serverConsole.logMessage( line );
				} 
			}
		}
	}

	private String toString(Object o) {
		if ( o instanceof URI ){
			URI uri = (URI)o;
			StringBuffer sb = new StringBuffer();
	        if (uri.getScheme() != null) {
	            sb.append( uri.getScheme() );
	            sb.append(':');
	        }
	        if ( uri.isOpaque()) {
	            sb.append( uri.getSchemeSpecificPart() );
	        } else {
	        	if (uri.getHost() != null) {
	                sb.append("//");
	                if ( uri.getUserInfo() != null) {
	                    sb.append( uri.getUserInfo() );
	                    sb.append('@');
	                }
	                sb.append( uri.getHost() );
	                if (uri.getPort() != -1) {
	                    sb.append(':');
	                    sb.append( uri.getPort() );
	                }
	            } else if ( uri.getAuthority() != null) {
	                sb.append("//");
	                sb.append( uri.getAuthority() );
	            }
	        	
	            if ( uri.getPath() != null){
	                sb.append( uri.getPath() );
	            }
	            
	            if ( uri.getQuery() != null) {
	                sb.append('?');
	                sb.append( uri.getQuery() );
	            }
	        }
	        
	        if ( uri.getFragment() != null) {
	            sb.append('#');
	            sb.append( uri.getFragment() );
	        }
			return sb.toString();
		}
		return o.toString();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.osgi.framework.console.CommandInterpreter#printStackTrace(java.lang.Throwable)
	 */
	public void printStackTrace(Throwable t) {
		serverConsole.logException( t );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.osgi.framework.console.CommandInterpreter#println()
	 */
	public void println() {
		serverConsole.logMessage( "" );
	}

}
