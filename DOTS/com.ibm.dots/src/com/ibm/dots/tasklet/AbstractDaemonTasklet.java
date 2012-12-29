/**
 * 
 */
package com.ibm.dots.tasklet;

/**
 * @author nfreeman
 * 
 */
public abstract class AbstractDaemonTasklet extends AbstractTasklet {

	/**
	 * 
	 */
	public AbstractDaemonTasklet() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.tasklet.AbstractTasklet#isResident()
	 */
	@Override
	public boolean isResident() {
		return true;
	}

}
