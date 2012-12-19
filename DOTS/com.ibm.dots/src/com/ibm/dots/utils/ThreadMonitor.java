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

import java.util.LinkedList;


/**
 * @author dtaieb
 *
 */
public class ThreadMonitor {
	
	private static final int DEFAULT_SAMPLE_SIZE = 20;

	private Thread thread;
	private LIFO<StackTraceInfo> stackTraceSamples;

	private static class LIFO<T> extends LinkedList< T >{
		private static final long serialVersionUID = 1L;
		private int max;
		LIFO( int max ){
			this.max = max;
		}
		
		public boolean add(T object) {
			boolean bRet = super.add( object );
			while( size() > max ){
				remove();
			}
			return bRet;
		}
	}
	
	private static class StackTraceInfo {
		private int iPos;
		private StackTraceElement[] elements;
		
		public StackTraceInfo(int iPos, StackTraceElement[] elements ){
			this.iPos = iPos;
			this.elements = elements;
		}

		public StackTraceElement[] getStackTrace() {
			if ( iPos >= elements.length - 1){
				return elements;
			}
			
			StackTraceElement[] retElements = new StackTraceElement[ iPos + 1 ];
			System.arraycopy( elements, elements.length - iPos - 1, retElements, 0, iPos + 1 );
			return retElements;
		}
	}
	/**
	 * @param thread 
	 * 
	 */
	public ThreadMonitor(Thread thread) {
		this( thread, DEFAULT_SAMPLE_SIZE );
		this.thread = thread;
	}

	/**
	 * @param thread
	 * @param sampleSize
	 */
	public ThreadMonitor(Thread thread, int sampleSize) {
		this.thread = thread;
		stackTraceSamples = new LIFO<ThreadMonitor.StackTraceInfo>( sampleSize );
	}

	/**
	 * 
	 */
	public synchronized int takeSample() {
		StackTraceElement[] traces = thread.getStackTrace();
		StackTraceInfo sti = getSTIWithShortestCommonFrames();
		StackTraceElement[] lastSample = sti == null ? null : sti.elements;
		int iPos = traces.length - 1;
		if ( lastSample != null ){
			//Start from the bottom, up to the point of divergence
			for ( int i = 0; i < traces.length ; i++ ){
				StackTraceElement newSTE = safeArrayAccessFromBottom( i, traces );
				StackTraceElement lastSTE = safeArrayAccessFromBottom( i, lastSample );
				
				if ( newSTE == null || lastSTE == null || !newSTE.equals( lastSTE )){
					iPos = i;
					break;
				}
			}
		}
			
		//add the traces to the stack history
		stackTraceSamples.add( new StackTraceInfo( iPos, traces ) );
		
		return stackTraceSamples.size();
	}

	/**
	 * @return
	 */
	private StackTraceInfo getSTIWithShortestCommonFrames() {
		StackTraceInfo retSti = null;
		for ( StackTraceInfo sti : stackTraceSamples ){
			if ( retSti == null || retSti.iPos > sti.iPos ){
				retSti = sti;
			}
		}
		return retSti;
	};

	/**
	 * @param iOffset
	 * @param elements
	 * @return
	 */
	private StackTraceElement safeArrayAccessFromBottom(int iOffset, StackTraceElement[] elements) {
		int iPos = elements.length - 1 - iOffset;
		if ( iPos < 0 ){
			return null;
		}
		return elements[ iPos ];
	}

	/**
	 * 
	 */
	public synchronized void reset() {
		stackTraceSamples.clear();
	}

	/**
	 * @return
	 */
	public StackTraceElement[] getStackTrace() {
		//If the code is in an infinite loop, then the lowest position is where the loop is
		StackTraceInfo loopingSTI = null;
		for ( StackTraceInfo sti : stackTraceSamples ){
			if ( loopingSTI == null || loopingSTI.iPos > sti.iPos ){
				loopingSTI = sti;
			}
		}
		return loopingSTI == null ? thread.getStackTrace() : loopingSTI.getStackTrace(); 
	}

	/**
	 * @return
	 */
	public boolean isLooping() {
		//To know if it's looping, look at the stackTrace samples to determine if the iPos varies
		int iPos = -1;
		for ( StackTraceInfo sti : stackTraceSamples ){
			if ( iPos == -1 ){
				iPos = sti.iPos;
			}else if ( iPos != sti.iPos ){
				return true;
			}
		}
		return false;
	}

}
