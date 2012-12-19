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
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.NotesException;

import org.eclipse.core.runtime.IProgressMonitor;

import com.ibm.dots.event.IExtensionManagerEvent;
import com.ibm.dots.event.NSFNoteUpdateExtendedEvent;
import com.ibm.dots.task.AbstractServerTask;
import com.ibm.dots.task.RunWhen;
import com.ibm.dots.task.RunWhen.RunUnit;

/**
 * @author dtaieb Example of task triggered by Extension manager event
 */
public class TriggeredExample extends AbstractServerTask {

	private Database triggeredDb;

	/**
	 * 
	 */
	public TriggeredExample() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.task.IServerTaskRunnable#dispose()
	 */
	public void dispose() throws NotesException {
		if (triggeredDb != null) {
			triggeredDb.recycle();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.task.IServerTaskRunnable#run(com.ibm.dots.task.RunWhen, java.lang.String[],
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void run(RunWhen runWhen, String[] args, IProgressMonitor monitor) throws NotesException {
		System.out.println("Running triggered tasklet");
		if (runWhen.getUnit() != RunUnit.triggered) {
			logMessage("Unsupported runWhen " + runWhen.getUnit());
			return;
		}

		IExtensionManagerEvent event = runWhen.getExtensionManagerEvent();
		if (event.getEventId() == IExtensionManagerEvent.EM_NSFNOTEUPDATEXTENDED
				|| event.getEventId() == IExtensionManagerEvent.EM_NSFNOTEUPDATE) {
			NSFNoteUpdateExtendedEvent updateEvent = (NSFNoteUpdateExtendedEvent) event;
			logMessage("Trigger event: EM_NSFNOTEUPDATEXTENDED: with DbPath = " + updateEvent.getDbPath());

			if (updateEvent.getDbPath().equalsIgnoreCase(ScheduledTask.OSGISAMPLE + ".nsf")) {
				if (triggeredDb == null) {
					triggeredDb = getSession().getDatabase("", updateEvent.getDbPath());
				}
				Document doc = triggeredDb.getDocumentByID(updateEvent.getNoteId());
				if (doc != null) {
					logMessage("Document save successfully opened");
					doc.recycle();
				} else {
					DocumentCollection coll = triggeredDb.getAllDocuments();
					doc = coll.getFirstDocument();
					while (doc != null) {
						System.out.println(doc.getNoteID());
						doc = coll.getNextDocument(doc);
					}
				}
			}
		}
		// else if ( event.getEventId() == IExtensionManagerEvent.EM_NSFDBCREATE ){
		// NSFDbCreateEvent dbCreateEvent = (NSFDbCreateEvent)event;
		// logMessage( "Trigger event: EM_NSFDBCREATE with dbPath = " + dbCreateEvent.getDbPath() );
		// }else if ( event.getEventId() == IExtensionManagerEvent.EM_NSFDBDELETE ){
		// NSFDbDeleteEvent dbDeleteEvent = (NSFDbDeleteEvent)event;
		// logMessage( "Trigger event: EM_NSFDBDELETE with dbPath = " + dbDeleteEvent.getDbPath() );
		// }else if ( event.getEventId() == IExtensionManagerEvent.EM_NSFNOTEDELETE ){
		// NSFNoteDeleteEvent noteDeleteEvent = (NSFNoteDeleteEvent)event;
		// logMessage(
		// MessageFormat.format( "Trigger event: EM_NSFNOTEDELETE with dbPath = {0} and NoteId {1}",
		// noteDeleteEvent.getDbPath(), noteDeleteEvent.getNoteId()
		// )
		// );
		// }else{
		// logMessage(
		// MessageFormat.format( "Unchecked Trigger event seen : {0}", event.getEventId() )
		// );
		// }
	}
}
