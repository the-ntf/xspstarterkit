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
package com.ibm.dots.task;

import lotus.domino.NotesException;
import lotus.domino.Session;

/**
 * @author dtaieb
 * 
 */
public abstract class AbstractServerTask implements IServerTaskRunnable {
	private static ThreadLocal<Session> localSession = new ThreadLocal<Session>() {
		@Override
		protected Session initialValue() {
			return null;
		}

	};

	// private Session session;
	private ServerConsole serverConsole;

	/**
	 * 
	 */
	public AbstractServerTask() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.task.IServerTaskRunnable#init(com.ibm.dots.task.ServerTaskInfo)
	 */
	@Override
	public void init(ServerTaskInfo serverTaskInfo) {
		// Prefix for log events
		serverConsole = new ServerConsole("(" + serverTaskInfo.getId() + ") ");
	}

	/**
	 * @return
	 */
	public Session getSession() {
		return localSession.get();
		// return session;
	}

	/**
	 * @param session
	 */
	public void setSession(Session session) {
		if (localSession.get() != null) {
			Session curSess = localSession.get();
			if (curSess == session) {
				// all good. We're just re-entrant on the same thread
			} else {
				serverConsole
						.logMessage("WARNING!! Attempting to set a session on a task that already has a different thread local session!!!! This will not end well.");
				localSession.set(session);
			}
		} else {
			localSession.set(session);
		}
	}

	public void clearSession() {
		localSession.set(null);
	}

	public void recycleSession() throws NotesException {
		localSession.get().recycle();
		localSession.set(null);
	}

	/**
	 * @param message
	 */
	@Override
	public void logMessage(String message) {
		if (serverConsole != null) {
			serverConsole.logMessage(message);
		}
	}

	/**
	 * @param t
	 */
	@Override
	public void logException(Throwable t) {
		if (t == null) {
			return;
		}
		if (serverConsole != null) {
			serverConsole.logException(t);
		}
	}
}
