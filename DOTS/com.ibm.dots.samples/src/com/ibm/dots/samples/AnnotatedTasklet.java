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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.Session;

import org.eclipse.core.runtime.IProgressMonitor;

import com.ibm.dots.annotation.Run;
import com.ibm.dots.annotation.RunOnStart;
import com.ibm.dots.annotation.Triggered;
import com.ibm.dots.event.IExtensionManagerEvent;
import com.ibm.dots.tasklet.AbstractTasklet;
import com.ibm.dots.tasklet.events.DotsEvent;
import com.ibm.dots.tasklet.events.DotsEventParams;
import com.ibm.dots.thread.SessionContext;

/**
 * @author dtaieb
 * 
 */
public class AnnotatedTasklet extends AbstractTasklet {

	/**
	 * 
	 */
	public AnnotatedTasklet() {
	}

	private Database getDatabase(String key) {
		Database result = null;
		try {
			Session s = SessionContext.getSession();
			result = s.getDatabase("", key);
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.task.IServerTaskRunnable#dispose()
	 */
	public void dispose() throws NotesException {
	}

	@RunOnStart
	public void runOnStart(IProgressMonitor monitor) {
		logMessage("Annotated onStart method");
	}

	@Run(id = "manual")
	public void runManual(String[] args, IProgressMonitor monitor) {
		logMessage("Annotated run method with id=manual");
	}

	// @RunOnStart
	// @RunEvery(every = 60, unit = TimeUnit.SECONDS)
	// public void runEvery60seconds(IProgressMonitor monitor) {
	// logMessage("Called from annotated method every 60 seconds");
	// }

	private final Map<String, Integer> updateMap_ = new HashMap<String, Integer>();
	private static ThreadLocal<DateFormat> formatter_ = new ThreadLocal<DateFormat>() {
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.ThreadLocal#initialValue()
		 */
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("HHmmSS");
		}
	};

	@Triggered(eventId = { IExtensionManagerEvent.EM_NSFNOTEUPDATEXTENDED, IExtensionManagerEvent.EM_NSFNOTEUPDATE })
	public void runTriggered2(DotsEvent event, IProgressMonitor monitor) {
		System.out.println("Starting runTriggered");
		String formName = "";
		String path = (String) event.getEventParam(DotsEventParams.SourceDbpath);
		String noteid = (String) event.getEventParam(DotsEventParams.Noteid);
		Database db = getDatabase(path);

		if (db != null) {
			try {
				Document doc = db.getDocumentByID(noteid);
				formName = doc.getItemValueString("form");
				doc.recycle();
			} catch (Throwable t) {
				this.logException(t);
			}
		}
		if (!updateMap_.containsKey(path)) {
			updateMap_.put(path, 1);
		} else {
			updateMap_.put(path, updateMap_.get(path) + 1);
		}

		System.out.println(formatter_.get().format(new Date()) + "  " + updateMap_.get(path) + " annotated UPDATE methods " + path + " : "
				+ noteid + " Form: " + formName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.tasklet.AbstractTasklet#isResident()
	 */
	@Override
	public boolean isResident() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.tasklet.AbstractTasklet#localInitialization(java.lang.Object[])
	 */
	@Override
	public void localInitialization(Object... args) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.tasklet.AbstractTasklet#localTeardown(java.lang.Object[])
	 */
	@Override
	public void localTeardown(Object... args) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.tasklet.AbstractTasklet#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Test Annotated Tasklet";
	}

	// @Triggered(eventId = { IExtensionManagerEvent.EM_NSFNOTEUPDATEXTENDED, IExtensionManagerEvent.EM_NSFNOTEUPDATE })
	// public void runTriggered(NSFNoteUpdateExtendedEvent event, IProgressMonitor monitor) {
	// String formName = "";
	// String path = event.getDbPath();
	// Database db = getDatabase(path);
	//
	// if (db != null) {
	// try {
	// Document doc = db.getDocumentByID(event.getNoteId());
	// formName = doc.getItemValueString("form");
	// doc.recycle();
	// } catch (Throwable t) {
	// this.logException(t);
	// }
	// }
	// if (!updateMap_.containsKey(path)) {
	// updateMap_.put(path, 1);
	// } else {
	// updateMap_.put(path, updateMap_.get(path) + 1);
	// }
	// logMessage(updateMap_.get(path) + " annotated UPDATE methods " + path + " : " + event.getNoteId() + " Form: " + formName);
	// }

	// @Triggered(eventId = { IExtensionManagerEvent.EM_NSFNOTEOPEN })
	// public void runTriggered(NSFNoteOpenEvent event, IProgressMonitor monitor) {
	// logMessage(openCount++ + " annotated OPEN method " + event.getDbPath() + " : " + event.getNoteId());
	// }

}
