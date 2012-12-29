/**
 * 
 */
package com.ibm.dots.thread;

import lotus.domino.NotesFactory;
import lotus.domino.Session;
import lotus.notes.NotesThread;

/**
 * @author nfreeman
 * 
 */
public class TrustedLocalSession extends ThreadLocal<Session> {
	private boolean isCleared = false;

	@Override
	protected Session initialValue() {
		Session result = null;
		// if (!isCleared) {
		// try {
		// System.out.println("Creating initial session in local thread " + Thread.currentThread().getName());
		// result = NotesFactory.createTrustedSession();
		// } catch (Throwable t) {
		// t.printStackTrace();
		// }
		// }
		return result;
	};

	public void recycle() {
		try {
			super.get().recycle();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void clear() {
		if (super.get() != null) {
			System.out.println("Clearing a thread local session object for thread " + Thread.currentThread().getName());
			isCleared = true;
			recycle();
			super.set(null);
			NotesThread.stermThread();
		}
	}

	@Override
	public Session get() {
		if (super.get() == null) {
			try {
				System.out.println("Creating a new trusted session in local thread " + Thread.currentThread().getName());
				NotesThread.sinitThread();
				super.set(NotesFactory.createTrustedSession());
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		return super.get();
	}
}
