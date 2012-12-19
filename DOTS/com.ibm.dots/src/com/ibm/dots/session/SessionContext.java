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

package com.ibm.dots.session;

/**
 * Object that represents a session context.
 * 
 * @author priand
 */
public class SessionContext {
    
    private int t1;
    private int t2;
    private boolean inContext;
    
    public SessionContext() {
    }


    // ------------------------------------------------------------
    // Thread initialization
    public void initThread() {
        NinitThread();
    }
    public void termThread() {
        NtermThread();
    }
    private native void NinitThread();
    private native void NtermThread();

    
    // ------------------------------------------------------------
    // Session initialization
    public void initSession() {
        if(t1!=0) {
            throw new IllegalStateException("Session is already initialized");
        }
        NinitThread();
        try {
            NinitSession();
        } finally {
            NtermThread();
        }
    }
    public void termSession() {
        if(t1==0) {
            throw new IllegalStateException("Session is not initialized");
        }
        NinitThread();
        try {
            NtermSession();
        } finally {
            NtermThread();
        }
    }
    private native void NinitSession();
    private native void NtermSession();

    
    // ------------------------------------------------------------
    // Context initialization
    public void initContext() {
        if(t1==0) {
            throw new IllegalStateException("Session is not initialized");
        }
        if(inContext) {
            throw new IllegalStateException("Context is already initialized");
        }
        NinitThread();
        NinitContext();
        inContext = true;
    }
    public void termContext() {
        if(!inContext) {
            throw new IllegalStateException("Context is not initialized");
        }
        NtermContext();
        NtermThread();
        inContext = false;
    }
    private native void NinitContext();
    private native void NtermContext();
    
    // ------------------------------------------------------------
    // Helpers
    public void run(Runnable r) {
        initContext();
        try {
            r.run();
        } finally {
            termContext();
        }
    }
}
