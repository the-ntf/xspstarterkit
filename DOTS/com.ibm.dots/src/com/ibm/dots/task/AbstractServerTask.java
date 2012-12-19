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

import lotus.domino.Session;

/**
 * @author dtaieb
 * 
 */
public abstract class AbstractServerTask implements IServerTaskRunnable {

	private Session session;
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
		return session;
	}

	/**
	 * @param session
	 */
	public void setSession(Session session) {
		if (this.session != null && this.session != session) {

			// throw new DotsException("Session already set"); // $NON-NLS-1$
		}
		this.session = session;
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
