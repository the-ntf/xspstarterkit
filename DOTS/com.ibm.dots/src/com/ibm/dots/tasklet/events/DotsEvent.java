/**
 * 
 */
package com.ibm.dots.tasklet.events;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.EnumMap;
import java.util.Set;

import com.ibm.dots.event.IExtensionManagerEvent;
import com.ibm.dots.tasklet.AbstractTasklet;

/**
 * @author nfreeman
 * 
 */
public class DotsEvent implements Runnable {
	public enum TYPE {
		TRIGGERED, SCHEDULED, MANUAL, STARTUP
	}

	private final TYPE type_;
	private final Date when_;
	private IExtensionManagerEvent refEvent_;
	private final EnumMap<DotsEventParams, Object> params_ = new EnumMap<DotsEventParams, Object>(DotsEventParams.class);
	private AbstractTasklet tasklet_;
	private Method method_;

	public TYPE getType() {
		return type_;
	}

	public DotsEvent(TYPE type) {
		type_ = type;
		when_ = new Date();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// System.out.println("Running DotsEvent!");
		if (method_ != null && tasklet_ != null) {
			try {

				method_.invoke(tasklet_, this, null); // TODO: enable progress monitoring
			} catch (Throwable t) {
				t.printStackTrace();
			}
		} else {
			System.out.println("No task or method defined!!!");
		}

		DotsEventFactory.recycleEvent(this); // return event object to pool for future runs
	}

	public void loadEvent(IExtensionManagerEvent event, String buffer) {
		refEvent_ = event;
		DotsEventParams.populateParamMap(params_, event.getParams(), buffer);
	}

	public void loadMethod(Method method, AbstractTasklet tasklet) {
		tasklet_ = tasklet;
		method_ = method;
	}

	public Object getEventParam(DotsEventParams param) {
		if (params_.containsKey(param)) {
			return params_.get(param);
		}
		return null;
	}

	public Set<DotsEventParams> getAvailableParams() {
		return params_.keySet();
	}

	public void recycle() {
		when_.setTime(0);
		params_.clear();
		tasklet_ = null;
		method_ = null;
		refEvent_ = null;
	}

}
