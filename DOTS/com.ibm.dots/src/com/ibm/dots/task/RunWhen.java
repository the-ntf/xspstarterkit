/*
 * © Copyright IBM Corp. 2009,2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.dots.task;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Date;

import com.ibm.dots.event.IExtensionManagerEvent;
import com.ibm.dots.utils.TimeUtils;

/**
 * @author dtaieb
 * 
 */
public class RunWhen {

	public enum RunUnit {
		triggered, onStart, once, second, minute, day
	}

	private RunUnit runUnit;
	private int every;
	private long startAtTime;
	private long orgStartAtTime;
	private long stopAtTime;
	private long orgStopAtTime;
	private IExtensionManagerEvent emEvent; // Used only when triggered

	public static final RunWhen RunOnStart = new RunWhen(RunUnit.onStart, 0, 0, 0);
	public static final RunWhen RunOnce = new RunWhen(RunUnit.once, 0, 0, 0);
	private static final long DAY_MS = 24 * 60 * 60 * 1000L;
	protected Method annotatedMethod; // Used only when run is declared via java annotation

	/**
	 * @param runUnit
	 * @param every
	 * @param startAtTime
	 * @param stopAtTime
	 */
	public RunWhen(RunUnit runUnit, int every, long startAtTime, long stopAtTime) {
		this.runUnit = runUnit;
		this.every = every;
		if (stopAtTime > 0) {
			if (stopAtTime < startAtTime) {
				// user mistake? recover by swapping the 2
				long l = stopAtTime;
				stopAtTime = startAtTime;
				startAtTime = l;
			}
		}

		this.orgStopAtTime = stopAtTime;
		this.orgStartAtTime = startAtTime;
		this.startAtTime = startAtTime;
		this.stopAtTime = stopAtTime;

		if (startAtTime > 0) {
			long everyInMs = getNextRunElapsed();
			long now = new Date().getTime();
			if (stopAtTime == 0 || (now >= startAtTime && now <= stopAtTime)) {
				// We don't have a stopAt or we are in range, adjust the startAt time and synchronize it based on the every value
				long delta = this.startAtTime - now;
				while (delta < 0 || Math.abs(delta) > everyInMs) {
					int modulo = new Long(Math.abs(delta) / everyInMs).intValue();
					if (modulo < 1) {
						modulo = 1;
					}
					if (delta > 0) {
						this.startAtTime -= modulo * everyInMs;
					} else {
						this.startAtTime += modulo * everyInMs;
					}
					delta = this.startAtTime - now;
				}
			} else {
				// Adjust the startAtTime to the next day if we have already passed it
				if (startAtTime < now) {
					this.startAtTime += DAY_MS;
				}
			}
		}
	}

	/**
	 * @param emEvent
	 */
	public RunWhen(IExtensionManagerEvent emEvent) {
		this.runUnit = RunUnit.triggered;
		this.every = 0;
		this.emEvent = emEvent;
	}

	/**
	 * @return
	 */
	public RunUnit getUnit() {
		return runUnit;
	}

	/**
	 * @return
	 */
	public int getEvery() {
		return every;
	}

	/**
	 * @return
	 */
	protected long getStartAtTime() {
		return startAtTime;
	}

	/**
	 * 
	 */
	protected void updateToNextStartAtTime() {
		if (runUnit != RunUnit.day && runUnit != RunUnit.minute && runUnit != RunUnit.second) {
			// No need
			return;
		}

		if (every <= 0) {
			return;
		}

		// Compute the number of ms to the next run
		long nextRun = getNextRunElapsed();
		startAtTime += nextRun;

		// Check if we went beyond the stop time
		if (stopAtTime > 0 && stopAtTime < startAtTime) {
			// Next startTime is tomorrow
			startAtTime = TimeUtils.getTimeOfDay(orgStartAtTime);
			if (startAtTime < stopAtTime) {
				// Add 24 hours
				startAtTime += DAY_MS;
			}
		}
	}

	/**
	 * @return
	 */
	private long getNextRunElapsed() {
		if (runUnit == RunUnit.second) {
			return every * 1000L;
		} else if (runUnit == RunUnit.minute) {
			return every * 60 * 1000L;
		} else if (runUnit == RunUnit.day) {
			// 1 day max, regardless of what every
			return DAY_MS;
		}
		return 0;
	}

	/**
	 * @return
	 */
	public IExtensionManagerEvent getExtensionManagerEvent() {
		return emEvent;
	}

	@Override
	public boolean equals(Object obj) {
		boolean bRet = super.equals(obj);
		if (bRet) {
			return bRet;
		}

		if (obj instanceof RunWhen) {
			RunWhen rObj = (RunWhen) obj;
			return rObj.every == every && rObj.runUnit == runUnit && rObj.orgStartAtTime == orgStartAtTime
					&& rObj.orgStopAtTime == orgStopAtTime;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int retHashCode = runUnit.hashCode() + Integer.valueOf(every).hashCode();
		if (orgStartAtTime > 0) {
			retHashCode += Long.valueOf(orgStartAtTime).hashCode();
		}
		return retHashCode;
	}

	@Override
	public String toString() {
		if (runUnit == RunUnit.onStart) {
			return "On Start"; // $NON-NLS-1$
		} else if (runUnit == RunUnit.once) {
			return "Once"; // $NON-NLS-1$
		}
		String unit = "day"; // $NON-NLS-1$
		if (runUnit == RunUnit.second) {
			unit = "second"; // $NON-NLS-1$
		} else if (runUnit == RunUnit.minute) {
			unit = "minute"; // $NON-NLS-1$
		}
		if (startAtTime == 0) {
			return MessageFormat.format("Runs every {0} - {1}", every, unit); // $NON-NLS-1$
		}

		if (stopAtTime == 0) {
			return MessageFormat.format("Runs every {0} - {1} - Next Run will be at {2}", every, unit, new Date(startAtTime)); // $NON-NLS-1$
		}

		return MessageFormat.format("Runs every {0} - {1} between {2} and {3} - Next Run will be at {4}", every, unit,
				TimeUtils.displayTime(orgStartAtTime), TimeUtils.displayTime(orgStopAtTime), new Date(startAtTime)); // $NON-NLS-1$
	}

}
