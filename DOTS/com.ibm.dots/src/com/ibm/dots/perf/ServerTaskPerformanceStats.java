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
package com.ibm.dots.perf;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.core.runtime.PerformanceStats;


/**
 * @author dtaieb
 *
 */
public class ServerTaskPerformanceStats {

	private static final ThreadLocal<ServerTaskPerformanceStats> perfStatsThreadLocal = new ThreadLocal<ServerTaskPerformanceStats>();
	private static final ThreadLocal< Stack<ServerTaskPerformanceStats> > nestedPerfStatsThreadLocal = new ThreadLocal<Stack<ServerTaskPerformanceStats>>();

	private PerformanceStats eclipsePerfStats;

	/**
	 * @param eventName
	 * @param blameObject
	 */
	public ServerTaskPerformanceStats(String eventName, Object blameObject) {
		if ( !PerformanceStats.ENABLED ){
			return;
		}
		eclipsePerfStats = PerformanceStats.getStats(eventName, blameObject );
	}

	/**
	 * @param perfStats
	 */
	public static void initThread(ServerTaskPerformanceStats perfStats) {
		if ( !PerformanceStats.ENABLED ){
			return;
		}
		ServerTaskPerformanceStats currentPerfSats = getCurrentPerfStats();
		if ( currentPerfSats != null ){
			//Add the current one to the stack, but stop its run first
			currentPerfSats.endRun();
			Stack<ServerTaskPerformanceStats> nestedStats = nestedPerfStatsThreadLocal.get();
			if ( nestedStats == null ){
				nestedStats = new Stack<ServerTaskPerformanceStats>();
				nestedPerfStatsThreadLocal.set( nestedStats );
			}
			nestedStats.add( currentPerfSats );
		}

		perfStats.startRun();
		perfStatsThreadLocal.set( perfStats );
	}

	/**
	 * 
	 */
	private void startRun() {
		if ( eclipsePerfStats != null ){
			eclipsePerfStats.startRun();
		}
	}

	/**
	 * 
	 */
	private void endRun() {
		if ( eclipsePerfStats != null ){
			eclipsePerfStats.endRun();
		}
	}

	/**
	 * @param perfStats
	 */
	public static void termThread(ServerTaskPerformanceStats perfStats) {
		if ( !PerformanceStats.ENABLED ){
			return;
		}
		try{
			ServerTaskPerformanceStats currentPerfSats = getCurrentPerfStats();
			checkState( currentPerfSats, true );

			//Check that it's the same one
			if ( currentPerfSats != perfStats ){
				throw new IllegalStateException("Invalid ServerTaskPerformanceStats passed to termThread"); // $NON-NLS-1$
			}           
		}finally{
			perfStats.endRun();
			//Check for nested stats
			Stack<ServerTaskPerformanceStats> nestedStats = nestedPerfStatsThreadLocal.get();
			ServerTaskPerformanceStats toSet = null;
			if ( nestedStats != null ){
				toSet = nestedStats.pop();
				if ( nestedStats.isEmpty() ){
					nestedPerfStatsThreadLocal.set( null );
				}
			}
			if ( toSet != null ){
				toSet.startRun();   //Start or restart the run
			}
			perfStatsThreadLocal.set( toSet );
		}       
	}

	/**
	 * @param customerCtx 
	 * @param bShouldBeSet
	 */
	private static void checkState( ServerTaskPerformanceStats perfStats, boolean bShouldBeSet ) {
		if ( bShouldBeSet && perfStats == null ){
			throw new IllegalStateException( "Server Performance Stats is not set for this thread" ); // $NON-NLS-1$
		}else if ( !bShouldBeSet && perfStats != null ){
			throw new IllegalStateException( "Server Performance Stats is already set for this thread" ); // $NON-NLS-1$
		}
	}

	/**
	 * @return
	 */
	private static ServerTaskPerformanceStats getCurrentPerfStats() {
		return perfStatsThreadLocal.get();
	}

	/**
	 * @param eventName
	 * Clear all the stats for the specified event name
	 */
	public static void reset(String eventName) {
		if ( !PerformanceStats.ENABLED || eventName == null ){
			return;
		}

		List<PerformanceStats> stats = getStats( eventName );
		for ( PerformanceStats stat : stats ){
			PerformanceStats.removeStats( stat.getEvent(), stat.getBlame() );
		}
	}

	/**
	 * @param eventName
	 * @return
	 */
	private static List<PerformanceStats> getStats(String eventName) {
		ArrayList< PerformanceStats > retStats = new ArrayList<PerformanceStats>();
		PerformanceStats[] allStats = PerformanceStats.getAllStats();
		for ( PerformanceStats stat : allStats ){
			if ( eventName.equals( stat.getEvent() )){
				retStats.add( stat );
			}
		}
		return retStats;
	}

	public interface Printer{

		void print(String message);

	}

	public static void printStats( String eventName, Printer printer ) {
		List<PerformanceStats> stats = getStats( eventName );
		for ( PerformanceStats stat : stats ){
			printer.print( "-----------------------------------------------------------------------" );
			printer.print( MessageFormat.format( "Performance Statistics for {0}", stat.getBlame() ) ); // $NON-NLS-1$
			printer.print( MessageFormat.format("Duration (ms): {0}", stat.getRunningTime() ) ); // $NON-NLS-1$
		}
	}

}
