/**
 * 
 */
package com.ibm.dots.thread;

import lotus.domino.Session;

/**
 * @author nfreeman
 * 
 */
public enum SessionContext {
	;

	private SessionContext() {
		System.out.println("Created new SessionContext?");
	}

	private static TrustedLocalSession localSessionHolder = new TrustedLocalSession();

	public static Session getSession() {
		return localSessionHolder.get();
	}

	public static void dispose() {
		localSessionHolder.clear();
		localSessionHolder.remove();
	}
}
