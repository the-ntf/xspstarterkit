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
import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.View;

import org.eclipse.core.runtime.IProgressMonitor;

import com.ibm.dots.task.AbstractServerTask;
import com.ibm.dots.task.RunWhen;

/**
 * @author dtaieb
 * Sample task that executes only on demand and take a database name as argument
 * This sample list all the views in the database
 */
public class ListViewsExample extends AbstractServerTask {

	public ListViewsExample() {
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.task.IServerTaskRunnable#dispose()
	 */
	public void dispose() throws NotesException {
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.dots.task.IServerTaskRunnable#run(com.ibm.dots.task.RunWhen, java.lang.String[], org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void run(RunWhen runWhen, String[] args, IProgressMonitor monitor) throws NotesException {
		if ( args.length == 0 ){
			logMessage( "Database Path required" );
			return;
		}
		
		Database db = getSession().getDatabase("", args[0] );
		try{
			try{
				if ( db == null ){
					logMessage("Database doesn't exist: " + args[0] );
					return;
				}
				if ( !db.isOpen() ){
					db.open();
				}
			}catch(NotesException ex ){
				logMessage( MessageFormat.format( "Error while opening the database: {0}. Error code is {1}", args[0], ex.id ));
				return;
			}
			Vector<?> views = db.getViews();
			monitor.beginTask( "List the views", views.size() );
			logMessage("List of views for db: " + db.getTitle() );
			for ( Object view : views ){
				if ( monitor.isCanceled() ){
					break;
				}
				logMessage("\t" + ((View)view).getName() );
				try {
					Thread.sleep( 1000 );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				monitor.worked( 1 );
			}
		}finally{
			if ( db != null ){
				db.recycle();
			}
		}
	}

}
