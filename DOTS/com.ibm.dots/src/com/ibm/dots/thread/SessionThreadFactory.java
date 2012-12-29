/**
 * 
 */
package com.ibm.dots.thread;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author nfreeman
 * 
 */
public class SessionThreadFactory implements ThreadFactory {
	private AtomicInteger count_ = new AtomicInteger();
	private final Set<SessionThread> threads_ = Collections.synchronizedSet(new HashSet<SessionThread>());

	/**
	 * 
	 */
	public SessionThreadFactory() {
		// System.out.println("Created new session thread factory");
	}

	public void cleanup() {
		System.out.println("Cleaning up SessionThreadFactory...");
		for (SessionThread thread : threads_) {
			thread.dispose();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
	 */
	@Override
	public Thread newThread(Runnable paramRunnable) {
		int cur = count_.incrementAndGet();
		SessionThread result = new SessionThread("Dots Thread " + cur, paramRunnable);
		synchronized (threads_) {
			threads_.add(result);
		}
		// System.out.println("Creating new thread for " + paramRunnable.toString());
		result.setPriority(Thread.NORM_PRIORITY);
		return result;
	}

}
