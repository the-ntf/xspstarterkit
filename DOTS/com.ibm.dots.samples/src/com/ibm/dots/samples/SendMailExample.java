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

import lotus.domino.Database;
import lotus.domino.DbDirectory;
import lotus.domino.Document;
import lotus.domino.NotesException;

import org.eclipse.core.runtime.IProgressMonitor;

import com.ibm.dots.task.AbstractServerTaskExt;
import com.ibm.dots.task.RunWhen;

/**
 * @author dtaieb
 *
 */
public class SendMailExample extends AbstractServerTaskExt {

	/**
	 * 
	 */
	public SendMailExample() {
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.task.IServerTaskRunnable#dispose()
	 */
	public void dispose() throws NotesException {
	}
	
	@Override
	protected void doRun(RunWhen runWhen, IProgressMonitor monitor) throws NotesException {
		String to = getKeyedArgument( "to" );
		if ( to == null ){
			logMessage("Invalid arguments. No to argument specified");
			return;
		}
		Database db = null;
		try{
			db = getSession().getDatabase("", "mail.box");
			if ( !db.isOpen() ){
				db.open();
			}
			
			Document mailDoc = db.createDocument();
			fillMailDocument( mailDoc );
			String cc = getKeyedArgument( "cc" );
			if ( cc != null ){
				mailDoc.appendItemValue( "CopyTo", cc );
			}
			String bcc = getKeyedArgument( "bcc" );
			if ( bcc != null ){
				mailDoc.appendItemValue( "BlindCopyTo", bcc );
			}
			mailDoc.send( to );
		}finally{
			if ( db != null ){
				db.recycle();
			}
		}
	}

	/**
	 * @param mailDoc
	 */
	private void fillMailDocument(Document mailDoc)throws NotesException {
		mailDoc.appendItemValue("Form", "Memo");
		mailDoc.appendItemValue("Subject", "Summary email");
		
		StringBuilder sb = new StringBuilder();
		DbDirectory dir = getSession().getDbDirectory(null);
		Database db = dir.getFirstDatabase(DbDirectory.DATABASE);
		while ( db != null ){
			sb.append( db.getFileName() + " : " + db.getSize() + "\n");
			db = dir.getNextDatabase();
		}
		mailDoc.appendItemValue("Body", sb.toString() );
	}

}
