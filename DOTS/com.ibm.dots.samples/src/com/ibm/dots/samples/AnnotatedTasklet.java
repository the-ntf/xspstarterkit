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

import java.util.HashMap;
import java.util.Map;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;

import org.eclipse.core.runtime.IProgressMonitor;

import com.ibm.dots.annotation.Run;
import com.ibm.dots.annotation.RunEvery;
import com.ibm.dots.annotation.RunOnStart;
import com.ibm.dots.annotation.Triggered;
import com.ibm.dots.event.IExtensionManagerEvent;
import com.ibm.dots.event.NSFNoteUpdateExtendedEvent;
import com.ibm.dots.task.AbstractServerTaskExt;
import com.ibm.dots.task.RunWhen;
import com.ibm.dots.task.RunWhen.RunUnit;

/**
 * @author dtaieb
 * 
 */
public class AnnotatedTasklet extends AbstractServerTaskExt {

	private final transient Map<String, Database> dbCache_ = new HashMap<String, Database>();

	/**
	 * 
	 */
	public AnnotatedTasklet() {
	}

	private Database getDatabase(String key) {
		if (!dbCache_.containsKey(key)) {
			try {
				Database db = getSession().getDatabase("", key);
				if (db.isOpen()) {
					dbCache_.put(key, db);
				}
			} catch (Throwable t) {
				this.logException(t);
			}
		}

		return dbCache_.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.task.IServerTaskRunnable#dispose()
	 */
	public void dispose() throws NotesException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.task.AbstractServerTaskExt#doRun(com.ibm.dots.task.RunWhen, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void doRun(RunWhen runWhen, IProgressMonitor monitor) throws NotesException {
	}

	@RunOnStart
	public void runOnStart(IProgressMonitor monitor) {
		logMessage("Annotated onStart method");
	}

	@Run(id = "manual")
	public void runManual(String[] args, IProgressMonitor monitor) {
		logMessage("Annotated run method with id=manual");
	}

	@RunOnStart
	@RunEvery(every = 60, unit = RunUnit.second)
	public void runEvery60seconds(IProgressMonitor monitor) {
		logMessage("Called from annotated method every 60 seconds");
	}

	private int updateCount = 0;

	@Triggered(eventId = { IExtensionManagerEvent.EM_NSFNOTEUPDATEXTENDED, IExtensionManagerEvent.EM_NSFNOTEUPDATE })
	public void runTriggered(NSFNoteUpdateExtendedEvent event, IProgressMonitor monitor) {
		String formName = "";
		Database db = getDatabase(event.getDbPath());

		if (db != null) {
			try {
				Document doc = db.getDocumentByID(event.getNoteId());
				formName = doc.getItemValueString("form");
				doc.recycle();
			} catch (Throwable t) {
				this.logException(t);
			}
		}
		logMessage(updateCount++ + " annotated UPDATE methods " + event.getDbPath() + " : " + event.getNoteId() + " Form: " + formName);
	}

	private int openCount = 0;

	// @Triggered(eventId = { IExtensionManagerEvent.EM_NSFNOTEOPEN })
	// public void runTriggered(NSFNoteOpenEvent event, IProgressMonitor monitor) {
	// logMessage(openCount++ + " annotated OPEN method " + event.getDbPath() + " : " + event.getNoteId());
	// }

}
