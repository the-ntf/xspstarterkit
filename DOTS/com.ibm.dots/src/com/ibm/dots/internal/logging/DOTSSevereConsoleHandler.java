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

import java.text.MessageFormat;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 * @author dtaieb
 * Handler used in non-debug mode to tell the admin where to look when a severe message happened
 */
public class DOTSSevereConsoleHandler extends StreamHandler {
	
	private static String moreDetailsMessage;   //Appended to each message on the screen, indicating where the administrator can find more info

	/**
	 * 
	 */
	public DOTSSevereConsoleHandler() {
        setOutputStream(System.err);
        
        if ( moreDetailsMessage == null ){
            String logDirectory = System.getProperty("rcp.data", "" )  // $NON-NLS-1$
                + "/logs"; // $NON-NLS-1$
            moreDetailsMessage = MessageFormat.format(". For more detailed information, please consult error-log-0.xml located in {0}", logDirectory ); // $NON-NLS-1$
        }	
	}

    @Override
    public boolean isLoggable(LogRecord record) {
        return record.getLevel() == Level.SEVERE;
    }
    
    @Override
    public Formatter getFormatter() {
        return new Formatter() {            
            @Override
            public String format(LogRecord record) {
                StringBuffer sb = new StringBuffer();
                sb.append( formatMessage( record ));
                sb.append( moreDetailsMessage );
                return sb.toString();
            }
        };
    }
    
    @Override
    public synchronized void publish(LogRecord record) {
        super.publish(record);
        flush();
    }
    
    @Override
    public synchronized void close() throws SecurityException {
        flush();
    }

}
