/**
 * 
 */
package com.ibm.dots.thread;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author nfreeman
 * 
 */
public class Guardian extends ScheduledThreadPoolExecutor {

	/**
	 * @param arg0
	 * @param arg1
	 */
	public Guardian(int corePoolSize) {
		super(corePoolSize, new SessionThreadFactory());
	}

	@Override
	public void shutdown() {
		System.out.println("Beginning shutdown of Guardian...");
		((SessionThreadFactory) getThreadFactory()).cleanup();
		super.shutdown();
	}

}
