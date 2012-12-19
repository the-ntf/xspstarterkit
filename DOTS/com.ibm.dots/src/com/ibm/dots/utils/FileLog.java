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
package com.ibm.dots.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.Date;

import org.eclipse.core.runtime.IStatus;

/**
 * @author dtaieb
 * class used to log message in a file
 */
public class FileLog {

	private static final String LOG_FILE_ARCHIVE_EXTENSION = ".bak"; // $NON-NLS-1$
	private static final int DEFAULT_FILE_SIZE_LIMIT = 500 * 1024;  //1 MB limit

	private int fileSizeLimit;
	private File logFile;
	private BufferedWriter logWriter = null;

	/**
	 * @param logFile
	 */
	public FileLog( File logFile ) {
		this( logFile, DEFAULT_FILE_SIZE_LIMIT );
	}

	/**
	 * @param logFile
	 * @param fileSizeLimit
	 */
	public FileLog( File logFile, int fileSizeLimit ){
		this.logFile = logFile;
		this.fileSizeLimit = fileSizeLimit;
	}

	/**
	 * @param message
	 */
	public synchronized void logMessage( String message ){
		logMessage(message, true );
	}

	/**
	 * @param message
	 */
	public synchronized void logMessage( String message, boolean bPrependNewLine ){
		try{
			acquireWriter();
			if  (bPrependNewLine ){
				logWriter.newLine();
			}
			logWriter.write( message );
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			releaseWriter();
		}
	}

	/**
	 * @param status
	 */
	public synchronized void logMessage( IStatus status ){
		try{
			acquireWriter();

			logWriter.write( "> " + new Date() + "  ");
			logWriter.write( status.getMessage() );
			logWriter.write( " ");
			logWriter.newLine();

			Throwable t = status.getException();
			if ( t != null ){
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				t.printStackTrace(pw);
				logWriter.write( sw.toString() );
				logWriter.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			releaseWriter();
		}
	}

	/**
	 * 
	 */
	private void releaseWriter() {
		if ( logWriter != null ){
			try {
				logWriter.flush();
				logWriter.close();
				logWriter = null;
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				//Rotate the file if necessary
				if ( logFile.length() > fileSizeLimit ){
					AccessController.doPrivileged( new PrivilegedAction<Object>() {
						public Object run() {
							File archivedLogFile = new File( logFile.getAbsolutePath() + LOG_FILE_ARCHIVE_EXTENSION );
							if ( archivedLogFile.exists() ){
								archivedLogFile.delete();
							}
							if ( !logFile.renameTo( archivedLogFile ) ){
								System.err.println( MessageFormat.format( "Unable to rotate the logFile {0}", logFile.getAbsolutePath() )); // $NON-NLS-1$
							}
							return null;
						}
					});
				}
			}
		}       
	}

	/**
	 * @return
	 * @throws FileNotFoundException
	 */
	private void acquireWriter() throws FileNotFoundException {
		if ( logWriter != null ){
			return;
		}
		OutputStream os = new FileOutputStream( logFile, true );
		try {
			logWriter = new BufferedWriter(new OutputStreamWriter( os, "UTF-8")); // $NON-NLS-1$
		} catch (UnsupportedEncodingException e) {
			//Try without UTF-8
			logWriter = new BufferedWriter(new OutputStreamWriter(os));
		}
	}

	/**
	 * @return
	 */
	public String getLogPath() {
		return logFile.getAbsolutePath();
	}

}
