/**
 * 
 */
package com.ibm.dots.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.ibm.dots.tasklet.events.DotsEvent;

/**
 * @author nfreeman
 * 
 */
public class Intimidator extends ThreadPoolExecutor {
	private static final int DEFAULT_MAX_QUEUE = 500;
	public static int FORCED_MAX_QUEUE = 0;

	/**
	 * @param # of core threads
	 * @param maximum
	 *            # threads allowed
	 * @param seconds
	 *            to keep alive overflow threads
	 */
	public Intimidator(int paramInt1, int paramInt2, long paramLong) {

		super(paramInt1, paramInt2, paramLong, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(getMaxQueueSize()));
		setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		this.setThreadFactory(new SessionThreadFactory());
	}

	public static int getMaxQueueSize() {
		if (FORCED_MAX_QUEUE > 0) {
			return FORCED_MAX_QUEUE;
		} else {
			return DEFAULT_MAX_QUEUE;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#beforeExecute(java.lang.Thread, java.lang.Runnable)
	 */
	@Override
	protected void beforeExecute(Thread paramThread, Runnable paramRunnable) {
		// TODO Auto-generated method stub
		super.beforeExecute(paramThread, paramRunnable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
	 */
	@Override
	protected void afterExecute(Runnable paramRunnable, Throwable paramThrowable) {
		// TODO Auto-generated method stub
		super.afterExecute(paramRunnable, paramThrowable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#execute(java.lang.Runnable)
	 */
	@Override
	public void execute(Runnable paramRunnable) {
		if (paramRunnable instanceof DotsEvent) {
			// System.out.println("Executing DotsEvent type: " + ((DotsEvent) paramRunnable).getType());
		}
		super.execute(paramRunnable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#shutdown()
	 */
	@Override
	public void shutdown() {
		System.out.println("Beginning shutdown of Intimidator...");
		((SessionThreadFactory) getThreadFactory()).cleanup();
		super.shutdown();
	}

}
