/**
 * 
 */
package com.ibm.dots.thread;

/**
 * @author nfreeman
 * 
 */
public class SessionDaemonThread extends SessionThread {
	private final Object sleepMonitor = new Object(); // Monitor the sleeping of the thread
	private volatile boolean shouldStop = false; // indicate that the Thread should stop at the next opportunity

	/**
	 * @param threadName
	 */
	public SessionDaemonThread(String threadName) {
		super(threadName);
		init();
	}

	/**
	 * @param group
	 * @param threadName
	 */
	public SessionDaemonThread(ThreadGroup group, String threadName) {
		super(group, threadName);
		init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#start()
	 */
	@Override
	public synchronized void start() {
		super.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
	}

	private void init() {
		setDaemon(true);
	}

	@Override
	public void dispose() {
		System.out.println("Disposing of daemon thread " + getName());
		super.dispose();
		shouldStop = true;
		wakeUp();
	}

	public void wakeUp() {
		synchronized (sleepMonitor) {
			sleepMonitor.notify();
		}
	}

}
