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
package com.ibm.dots.internal.logging;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import com.ibm.dots.task.ServerTaskManager;

/**
 * @author dtaieb
 *
 */
public class DOTSConsoleHandler extends Handler {

	/**
	 * 
	 */
	public DOTSConsoleHandler() {
	}

	@Override
	public void close() {
		flush();
	}

	@Override
	public void flush() {	
	}

	@Override
	public void publish(LogRecord record) {
		String formattedMessage = getFormatter().format(record);
		try {
            if (this.isLoggable(record)) {
            	ServerTaskManager.getInstance().logMessageText( formattedMessage );
            }
        } catch (Throwable t) {
        	if (this.isLoggable(record)) {
        		System.out.println( formattedMessage );
        	}
        }
	}
	
    @Override
    public boolean isLoggable(LogRecord record) {
        return true;
    }
    
    @Override
    public Formatter getFormatter() {
        return new Formatter() {            
            @Override
            public String format(LogRecord record) {
                return formatMessage( record );
            }
        };
    }
}
