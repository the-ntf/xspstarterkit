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
		// TODO Auto-generated constructor stub
	}

}
