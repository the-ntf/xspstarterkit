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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.ibm.dots.Activator;

/**
 * @author dtaieb
 * Remote Console adaptor class used by remote commands controller for type=embedded 
 */
public class OSGiRemoteConsoleAdaptor extends OSGIConsoleAdaptor {

	private PrintWriter printWriter;
	private Socket socket;

	/**
	 * @param socket
	 * @throws IOException
	 */
	public OSGiRemoteConsoleAdaptor(Socket socket) throws IOException {
		this.socket = socket;
		socket.setSoTimeout( 2000 );    //2 seconds timeout
		printWriter = new PrintWriter( socket.getOutputStream() );

		runCommands();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.osgi.framework.console.CommandInterpreter#print(java.lang.Object)
	 */
	public void print(Object o) {
		if ( o != null ){
			String s = o.toString();
			String[] lines = s.split("[\r\n]"); // $NON-NLS-1$
			if ( lines.length == 0 ){
				println();
			}else{
				for ( String line : lines ){
					if ( line.length() > 0 ){
						//No need for the leading \t
						line = line.trim();
						printWriter.write( line );
						println();
					} 
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.osgi.framework.console.CommandInterpreter#println()
	 */
	public void println() {
		printWriter.write( "\r\n" ); // $NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.osgi.framework.console.CommandInterpreter#printStackTrace(java.lang.Throwable)
	 */
	public void printStackTrace(Throwable t) {
		if ( t == null ){
			return;
		}
		String message = t.getMessage();
		if ( message == null ){
			message = t.getClass().getName();
		}

		StringWriter sw = new StringWriter();
		t.printStackTrace( new PrintWriter( sw ) );
		print( sw.toString() );
	}

	/**
	 * 
	 */
	public void runCommands(){
		new Job("Remote Console Session") {          // $NON-NLS-1$
			@Override
			protected IStatus run(IProgressMonitor arg0) {
				try {
					BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
					String cmdline = bufferedReader.readLine();
					if (cmdline != null){
						if ( !runCommand( cmdline.split( " " ) ) ){
							print("Unknown command"); // $NON-NLS-1$
						}
						printWriter.flush();
					}

					//Shutdown the connection
					socket.shutdownOutput();
				} catch (IOException e) {
					return new Status( Status.ERROR, Activator.PLUGIN_ID, Status.ERROR, e.getMessage(), e );
				}
				return Status.OK_STATUS;
			}
		}.schedule();
	}

}
