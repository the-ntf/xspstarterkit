/*
 * © Copyright IBM Corp. 2009,2011
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
package com.ibm.dots.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import com.ibm.dots.internal.logging.LoggingGroups;

/**
 * @author dtaieb
 *
 */
public class TimeUtils {

	private static final long SECOND = 1000L;
	private static final long MINUTE = 60 * SECOND;
	private static final long HOUR = 60 * MINUTE;
	private static final long DAY = 24 * HOUR;

	/**
	 * 
	 */
	private TimeUtils() {
	}

	/**
	 * @param time
	 * @return
	 */
	public static String formatTime(long time) {
		if ( time < MINUTE ){
			return (time / SECOND + " seconds"); // $NON-NLS-1$
		}else if ( time < HOUR ){
			long mn = time / MINUTE;
			return (mn + " minute(s) " + formatTime( time - (mn * MINUTE) )); // $NON-NLS-1$
		}else if ( time < DAY ){
			long hours = time / HOUR;
			return ( hours + " hour(s) " + formatTime( time - (hours * HOUR) ) ); // $NON-NLS-1$
		}else{
			long days = time / DAY;
			return ( days + " day(s) " + formatTime ( time - ( days * DAY ) ) ); // $NON-NLS-1$
		}
	}
	
	/**
	 * @param sDate
	 * @return
	 */
	public static long getTimeOfDay(String sDate) {
		if ( sDate == null || sDate.length() == 0 ){
			return 0;
		}
		
		try{
			Date d = SimpleDateFormat.getTimeInstance( DateFormat.SHORT ).parse( sDate );
			return getTimeOfDay( d.getTime() );
		}catch( Throwable t ){
			LoggingGroups.getServerTaskManagerLogger().logp( 
				Level.WARNING, TimeUtils.class.getName(), "getTimeOfDay", t.getMessage(), t
			);
		}
		return 0;
	}

	/**
	 * @param time
	 * @return
	 */
	public static long getTimeOfDay(long time) {
		Calendar cl = Calendar.getInstance();
		cl.setTimeInMillis( time );
		
		Calendar cl2 = Calendar.getInstance();
		cl2.set( Calendar.HOUR_OF_DAY, cl.get( Calendar.HOUR_OF_DAY ));
        cl2.set( Calendar.MINUTE, cl.get( Calendar.MINUTE ));
        cl2.set( Calendar.SECOND, cl.get( Calendar.SECOND ) );
        
		return cl2.getTimeInMillis();
	}

	/**
	 * @param lTime
	 * @return
	 */
	public static String displayTime(long lTime) {
		Date d = new Date( lTime );
		SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
		return sdf.format( d );
	}

}
