/**
 * 
 */
package com.ibm.dots.thread;

/**
 * @author nfreeman
 * 
 */
public class SessionThread extends Thread {

	/**
	 * @param threadName
	 */
	public SessionThread(String threadName) {
		super(threadName);
	}

	/**
	 * @param group
	 * @param threadName
	 */
	public SessionThread(ThreadGroup group, String threadName) {
		super(group, threadName);
	}

	/**
	 * @param string
	 * @param paramRunnable
	 */
	public SessionThread(String string, Runnable paramRunnable) {
		super(paramRunnable);
		setName(string);
	}

	public void dispose() {
		SessionContext.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#interrupt()
	 */
	@Override
	public void interrupt() {
		// System.out.println(getName() + " interrupted...");
		dispose();
		super.interrupt();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#start()
	 */
	@Override
	public synchronized void start() {
		System.out.println("Starting new SessionThread!");
		super.start();
		// NotesThread.sinitThread();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		// System.out.println("Begin run in a SessionThread!");
		super.run();
		// System.out.println("End run in a SessionThread!");
	}
}
