/*
 * © Copyright IBM Corp. 2009,2010
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

package com.ibm.dots.samples;

import java.text.MessageFormat;

import lotus.domino.Database;
import lotus.domino.DbDirectory;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.View;

import org.eclipse.core.runtime.IProgressMonitor;

import com.ibm.dots.task.AbstractServerTask;
import com.ibm.dots.task.RunWhen;
import com.ibm.dots.task.RunWhen.RunUnit;

/**
 * @author dtaieb
 * Sample task showing task scheduling
 * This task opens the sample database ( creates it if not there)
 * and log a message to a form 
 */
public class ScheduledTask extends AbstractServerTask {
	
	public static final String OSGISAMPLE = "OSGiSample";
	private Database db;
	private Document doc;

	/**
	 * 
	 */
	public ScheduledTask() {
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.dots.task.IServerTaskRunnable#run(com.ibm.dots.task.RunWhen, java.lang.String[], org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void run(RunWhen runWhen, String[] args, IProgressMonitor monitor)throws NotesException {
		if ( db == null ){
			db = getSession().getDatabase("", OSGISAMPLE);
			if ( db == null || !db.isOpen() ){
				//Create one
				logMessage( "Creating Sample Database" );
				DbDirectory dbDirectory = getSession().getDbDirectory( null );
				db = dbDirectory.createDatabase( OSGISAMPLE, true );
				//Create a view and a column, so we can at least open it
				//As an enhancement, we could create a template in Domino Designer and package it as a resource 
				//in the plugin and create the sample db based on the packaged template
				View v = db.createView("OSGiSample");
				v.createColumn();
			}
		}
		
		//Reset the document every day
		if ( runWhen.getUnit() == RunUnit.day ){
			if ( doc != null ){
				doc.recycle();
				doc = null;
			}
		}

		if ( doc == null ){
			//Create a new Document
			doc = db.createDocument();
			doc.appendItemValue("Form", "OSGiSampleForm");		
		}
		
		String message = MessageFormat.format( 
			"Scheduled run: {0}. Last Modified Date for db {1}: {2}",
			runWhen , 
			db.getFileName(),
			db.getLastModified() == null ? "No modified Date" : db.getLastModified().toJavaDate()
		);
		
		//Print to server console
		logMessage( message );
		
		//Also add it to the database
		doc.appendItemValue("Messages", message + "\n");
		doc.save();
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.task.IServerTaskRunnable#dispose()
	 */
	public void dispose() throws NotesException {
		if ( doc != null ){
			doc.recycle();
			doc = null;
		}
		if ( db != null ){
			db.recycle();
			db = null;
		}	
	}

}
